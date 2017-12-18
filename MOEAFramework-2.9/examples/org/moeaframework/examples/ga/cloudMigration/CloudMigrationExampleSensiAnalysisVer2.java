package org.moeaframework.examples.ga.cloudMigration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.Instrumenter;
import org.moeaframework.analysis.collector.Accumulator;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.util.Vector;

@SuppressWarnings("unused")
public class CloudMigrationExampleSensiAnalysisVer2 {
	//keep track of csp number used
	public static int count = 3;
	public static int sobolindex = 0;

	//DECLARE ALL THE FIXED PARAMETERS HERE
	int evaluationNumber = 8500;
	int populationSize = 250;
	double crossoverRate = 0.6;
	double mutationRate = 0.1;
	double mutationDistIndex = 10.00;
	double crossDistIndex = 15.00;



	private void nonFederatedExperiments(){
		String outputFileName = null;
		switch(count){
		//"C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\objectiveValues.txt";
		
		//C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\outputNormalCSP3.csv";
		case 1: outputFileName ="C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\objectiveValuesNormalCSP1.txt";
		break;
		case 2: outputFileName = "C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\objectiveValuesNormalCSP2.txt";
		break;
		case 3: outputFileName ="C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\objectiveValuesNormalCSP3.txt";
		break;
		case 4: outputFileName = "C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\objectiveValuesFtCSP1.txt";
		break;
		case 5:outputFileName ="C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\objectiveValuesFtCSP2.txt";
		break;
		case 6:outputFileName = "C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\objectiveValuesFtCSP3.txt";
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
			//int iteration = 0;
			//YOU JUST NEED ONE FOR LOOP HERE; SINCE YOU WILL VARY ONE PARAMETER EVERY RUN.
			//for( populationSize = 40; populationSize <= 520; populationSize+=iteration*80){//no of evaluations
				//iteration++;
			//for(sobolindex = 1; sobolindex <= 30000; sobolindex++){
				NondominatedPopulation result = new Executor()
						//.withProblemClass(CloudMigrationNonFederated.class)
						.withProblemClass(CloudMigrationNonFederated.class)
						.withAlgorithm("NSGAII")
						.withMaxEvaluations(evaluationNumber)
						.withProperty("populationSize", populationSize)	//16383
						.withProperty("sbx.rate", crossoverRate)
						.withProperty("sbx.distributionIndex",crossDistIndex)
						.withProperty("pm.rate", mutationRate)
						.withProperty("pm.distributionIndex", mutationDistIndex)
						.distributeOnAllCores()
						.run();

				
				new Plot()
				.add("NSGAII", result)
				.show();			
				 
				//System.out.println("Solution \t Cost \t Security \t Cloud Migration Plan \n");
				//System.out.println("NOTE THAT TWO MIGRATION CONSTRAINT ELEMENTS ARE MISSING FROM PLAN DISPLAY BELOW \n\n");
				
				//######################################################
				for (int i = 0; i < result.size(); i++) {
					Solution solution = result.get(i);
					double[] objectives = solution.getObjectives();			

					// negate objectives to return them to their maximized form
					//objectives = Vector.negate(objectives);
					//bw.write(evaluationNumber + ","+ populationSize + ","+ crossoverRate + "," + mutationRate +"," + crossDistIndex + ","+ mutationDistIndex + "," +  objectives[0] + "," + new DecimalFormat("#0.00").format(objectives[1]) + "," + solution.getVariable(0));
					bw.write(objectives[0] + " " + new DecimalFormat("#0.00").format(objectives[1]) + " ");
					//bw.newLine();

					//System.out.print(i+1 + "\t");
					System.out.print(objectives[0] + "\t");
								System.out.print(new DecimalFormat("#0.00").format(objectives[1]) + "\t");
								System.out.print(solution.getVariable(0) + "\n");		//USe for non federated
								System.out.println();

				} //END OF PARETO FRONT PRINTING FOR LOOP
				bw.newLine();
				//######################################################
				// FOR PRINTING JUST ONE OBJECTIVE VALUE
				
				/*	Solution solution = result.get(0);
					double[] objectives = solution.getObjectives();			

					
					bw.write(objectives[0] + " " + new DecimalFormat("#0.00").format(objectives[1]) + " ");
						
				bw.newLine();*/


			//}//END FOR EVALUATION #

			//bw.write(content);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}



	}//END FUNCTION NON federated

	private void federatedExperiments(){
		String outputFileName = null;

		outputFileName = "C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\objectiveValuesFtFederated.txt";


		try {



			File file = new File(outputFileName);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			//System.out.println("Initializing instrumentor");
			
			/*Instrumenter instrumenter = new Instrumenter()
					.withProblemClass(CloudMigrationFederated.class)
					.withFrequency(100)
					.attachAll();*/
			
			
			//String[] algorithms = {"NSGAII", "eNSGAII", "eMOEA", "GDE3", "NSGAIII", "OMOPSO", "PAES", "PESA2", "SMPSO", "SPEA2", "VEGA"};

			//for(sobolindex = 1; sobolindex <= 30000; sobolindex++){//no of evaluations based on sample generator 
							NondominatedPopulation result = new Executor()
									.withProblemClass(CloudMigrationFederated.class)
									.withAlgorithm("NSGAII")
									.withMaxEvaluations(evaluationNumber)
									//.withInstrumenter(instrumenter)
									.withProperty("populationSize", populationSize)	
									.withProperty("sbx.rate", crossoverRate)
									.withProperty("sbx.distributionIndex", crossDistIndex)
									.withProperty("pm.rate", mutationRate)
									.withProperty("pm.distributionIndex", mutationDistIndex)
									.distributeOnAllCores()
									.run();
							
							
				/*			Executor executor = new Executor()
									.withProblemClass(CloudMigrationFederated.class)
									.withMaxEvaluations(1000);
							
							Analyzer analyzer = new Analyzer()
									.withProblemClass(CloudMigrationFederated.class)
									.includeHypervolume()
									.includeGenerationalDistance()
									.includeSpacing()
									.includeMaximumParetoFrontError()
									.includeContribution()
								    .showStatisticalSignificance();
				*/			
							//run each algorithm for 50 seeds
				/*			System.out.println("Initiating Analyzer Execution...");
							for (String tempAlgorithm : algorithms) {
							    analyzer.addAll(tempAlgorithm, 
							    		executor.withAlgorithm(tempAlgorithm).runSeeds(50));
							}
				*/			 
							//print the results
				/*			System.out.println("Finished Analyzing");
							analyzer.printAnalysis();
				*/			
							
							
							new Plot()
							.add("NSGAII", result)
							.show();
							 

							//System.out.println("Solution \t Cost \t Security \t Cloud Migration Plan \n");
							//System.out.println("NOTE THAT TWO MIGRATION CONSTRAINT ELEMENTS ARE MISSING FROM PLAN DISPLAY BELOW \n\n");
							
							//#######################################
							for (int i = 0; i < result.size(); i++) {
								Solution solution = result.get(i);
								double[] objectives = solution.getObjectives();			
								String outputValue = "";
								
							//#######################################

								//System.out.print(i+1 + "\t");


								outputValue += objectives[0] + ",";
								outputValue += new DecimalFormat("#0.00").format(objectives[1]) + ",";
								
								
								//USE FOLLOWING FOR SOBOL ANALYSIS 
								/*outputValue += objectives[0] + " ";
								outputValue += new DecimalFormat("#0.00").format(objectives[1]) + " ";*/

								
								//USE THE FOLLOWING FOR LOOP TO PRINT MIGRATION PLANS CODING UTILS 
								for (int k = 0; k < 14; k++){
									outputValue += EncodingUtils.getInt(solution.getVariable(k));
								}
								
								//bw.write(evaluationNumber + ","+ populationSize + ","+ crossoverRate + "," + mutationRate+"," + crossDistIndex + "," + mutationDistIndex + "," + outputValue);
								//Print output split by <space> 
								System.out.println(outputValue);
								bw.write(outputValue);
							}//END of RESULT SIZE FOR LOOP
							bw.newLine();						
		//	}//END FOR EVALUATION # OF SAMPLE GENERATOR

			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}



	}//END FUNCTION federated

	public static void main(String[] args) throws IOException {

		//for(count = 1; count<=7; count++){
			if (count <= 6){
				new CloudMigrationExampleSensiAnalysisVer2().nonFederatedExperiments();
			}
			else{
				new CloudMigrationExampleSensiAnalysisVer2().federatedExperiments();
				
			}
		//}//COUNT FOR
		System.out.println("\n\n\n !!!END OF EXECUTION!!! \n");

	}//END MAIN

}//END CLASS 
