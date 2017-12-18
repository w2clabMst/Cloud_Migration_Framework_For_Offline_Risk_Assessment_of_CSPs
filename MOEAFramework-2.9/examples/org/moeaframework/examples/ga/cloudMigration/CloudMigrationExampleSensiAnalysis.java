package org.moeaframework.examples.ga.cloudMigration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import org.moeaframework.Executor;
import org.moeaframework.Instrumenter;
import org.moeaframework.analysis.collector.Accumulator;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.util.Vector;

@SuppressWarnings("unused")
public class CloudMigrationExampleSensiAnalysis {
	//keep track of csp number used
	public static int count = 1;
	
	private void nonFederatedExperiments(){
		String outputFileName = null;
		switch(count){
		case 1: outputFileName = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/outputNormalCSP1.csv";
		break;
		case 2: outputFileName = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/outputNormalCSP2.csv";
			break;
		case 3: outputFileName = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/outputNormalCSP3.csv";
			break;
		case 4: outputFileName = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/outputFtCSP1.csv";
			break;
		case 5:outputFileName = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/outputFtCSP2.csv";
			break;
		case 6:outputFileName = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/outputFtCSP3.csv";
			break;
		default: System.out.println("Incorrect case number");
			break;
		}
		
		try {

			

			File file = new File(outputFileName);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int evaluationNumber = 10000; evaluationNumber <= 50000; evaluationNumber+=10000){//no of evaluations
				for(int populationSize = 10000; populationSize<=50000; populationSize+=10000){//populationSize
					for(double crossoverRate = 0.0; crossoverRate <= 1.0; crossoverRate+= 0.1){//crossover
						for(double mutationRate=0.0; mutationRate <= 1.0; mutationRate+=0.1){//mutation
							NondominatedPopulation result = new Executor()
									//.withProblemClass(CloudMigrationNonFederated.class)
									.withProblemClass(CloudMigrationNonFederated.class)
									.withAlgorithm("NSGAII")
									.withMaxEvaluations(evaluationNumber)
									.withProperty("populationSize", populationSize)	//16383
									.withProperty("sbx.rate", crossoverRate)
									.withProperty("sbx.distributionIndex", 15.0)
									.withProperty("pm.rate", mutationRate)
									.withProperty("pm.distributionIndex", 20.0)
									.distributeOnAllCores()
									.run();
							
							//System.out.println("Solution \t Cost \t Security \t Cloud Migration Plan \n");
							//System.out.println("NOTE THAT TWO MIGRATION CONSTRAINT ELEMENTS ARE MISSING FROM PLAN DISPLAY BELOW \n\n");
							for (int i = 0; i < result.size(); i++) {
								Solution solution = result.get(i);
								double[] objectives = solution.getObjectives();			

								// negate objectives to return them to their maximized form
								//objectives = Vector.negate(objectives);
								bw.write(evaluationNumber + ","+ populationSize + ","+ crossoverRate + "," + mutationRate+"," + objectives[0] + "," + new DecimalFormat("#0.00").format(objectives[1]) + "," + solution.getVariable(0));
								bw.newLine();
								
								//System.out.print(i+1 + "\t");
								/*System.out.print(objectives[0] + "\t");
								System.out.print(new DecimalFormat("#0.00").format(objectives[1]) + "\t");
								System.out.print(solution.getVariable(0) + "\n");		//USe for non federated
								System.out.println();*/



							}//END FOR RESULT SIZEZ
							
						}//END FOR MUTATION RATE
						
					}//END FOR CROSSOVER RATE
					
				}//END FOR POP SIZE
			}//END FOR EVALUATION #
			
			//bw.write(content);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}//END FUNCTION NON federated
	
	private void federatedExperiments(){
		String outputFileName = null;
		
		outputFileName = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/outputFederatedCSP.csv";
		
		
		try {

			

			File file = new File(outputFileName);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int evaluationNumber = 10000; evaluationNumber <= 50000; evaluationNumber+=10000){//no of evaluations
				for(int populationSize = 10000; populationSize<=50000; populationSize+=10000){//populationSize
					for(double crossoverRate = 0.0; crossoverRate <= 1.0; crossoverRate+= 0.1){//crossover
						for(double mutationRate=0.0; mutationRate <= 1.0; mutationRate+=0.1){//mutation
							NondominatedPopulation result = new Executor()
									//.withProblemClass(CloudMigrationNonFederated.class)
									.withProblemClass(CloudMigrationFederated.class)
									.withAlgorithm("NSGAII")
									.withMaxEvaluations(evaluationNumber)
									.withProperty("populationSize", populationSize)	//16383
									.withProperty("sbx.rate", crossoverRate)
									.withProperty("sbx.distributionIndex", 15.0)
									.withProperty("pm.rate", mutationRate)
									.withProperty("pm.distributionIndex", 20.0)
									.distributeOnAllCores()
									.run();
							
							//System.out.println("Solution \t Cost \t Security \t Cloud Migration Plan \n");
							//System.out.println("NOTE THAT TWO MIGRATION CONSTRAINT ELEMENTS ARE MISSING FROM PLAN DISPLAY BELOW \n\n");
							for (int i = 0; i < result.size(); i++) {
								Solution solution = result.get(i);
								double[] objectives = solution.getObjectives();			
								String outputValue = "";
								
								//System.out.print(i+1 + "\t");
								
								
								outputValue += objectives[0] + ",";
								outputValue += new DecimalFormat("#0.00").format(objectives[1]) + ",";
								
								for (int k = 0; k < 14; k++){
									outputValue += EncodingUtils.getInt(solution.getVariable(k));
								}
								bw.write(evaluationNumber + ","+ populationSize + ","+ crossoverRate + "," + mutationRate+"," + outputValue);
								bw.newLine();
							}//END FOR RESULT SIZEZ
							
						}//END FOR MUTATION RATE
						
					}//END FOR CROSSOVER RATE
					
				}//END FOR POP SIZE
			}//END FOR EVALUATION #
			
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}//END FUNCTION federated
	
	public static void main(String[] args) throws IOException {
		
		for(count = 1; count<=7; count++){
			if (count <= 6){
				new CloudMigrationExampleSensiAnalysis().nonFederatedExperiments();
			}
			else{
				new CloudMigrationExampleSensiAnalysis().federatedExperiments();
			}
		}//COUNT FOR
		System.out.println("\n\n\n !!!END OF EXECUTION!!! \n");

	}//END MAIN

}//END CLASS 
