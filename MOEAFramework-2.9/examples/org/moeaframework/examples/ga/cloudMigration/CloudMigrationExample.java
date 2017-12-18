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
public class CloudMigrationExample {

	public static void main(String[] args) throws IOException {
		/*
		 * The Analyzer provides end-of-run analysis. This analysis focuses on the
resulting Pareto approximation set and how it compares against a known
reference set. The Analyzer is particularly useful for statistically comparing
the results produced by two or more algorithms, or possibly the same algo-
rithm with different parameter settings.
Analyzer analyzer = new Analyzer()
.withProblem("UF1")
.includeAllMetrics()
.showStatisticalSignificance();

Executor executor = new Executor()
.withProblem("UF1")
.withMaxEvaluations(10000);

analyzer.addAll("NSGAII",
executor.withAlgorithm("NSGAII").runSeeds(50));

analyzer.addAll("GDE3",
executor.withAlgorithm("GDE3").runSeeds(50));

analyzer.printAnalysis();


NOTE IMP: 
Statistical significance is important when comparing multiple algorithms,
particularly when the results will be reported in scientific papers. Running
an algorithm will likely produce different results each time it is run. This is
because many algorithms are stochastic (meaning that they include sources of randomness). 
Because of this, it is a common practice to run each algorithm
for multiple seeds, with each seed using a different random number sequence.
As a result, an algorithm does not produce a single result. It produces a dis-
tribution of results. When comparing algorithms based on their distributions,
it is necessary to use statistical tests. Statistical tests allow us to determine
if two distributions (i.e., two algorithms) are similar or different.
		 */


		/*Instrumenter instrumenter = new Instrumenter()
				.withProblem("ZDT1")
				.withFrequency(100)
				.attachAll();*/
		NondominatedPopulation result = new Executor()
				//.withProblemClass(CloudMigrationNonFederated.class)
				.withProblemClass(CloudMigrationFederated.class)
				.withAlgorithm("NSGAII")
				.withMaxEvaluations(2000)
				//.withInstrumenter(instrumenter)
				.withProperty("populationSize", 150)	//16383
				.withProperty("sbx.rate", 0.99)
				.withProperty("sbx.distributionIndex", 50.0)
				.withProperty("pm.rate", 1.0)
				.withProperty("pm.distributionIndex", 100.0)
				.distributeOnAllCores()
				.run();


//		try{
//
//			String solutionNumber = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/solutionNumber.txt";
//			String costObjective = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/costObjective.txt";
//			String securityObjective = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/securityObjective.txt";
//			String migrationPlan = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/migrationPlan.txt";
//
//			File fileSolutionNum = new File(solutionNumber);
//			File fileCostObj = new File(costObjective);
//			File fileSecurityObj = new File(securityObjective);
//			File fileMigrationPlan = new File(migrationPlan);
//
//			if ((!fileCostObj.exists())	||
//					(!fileSecurityObj.exists())){
//
//				fileCostObj.createNewFile();
//				fileSecurityObj.createNewFile();
//			}
			/*FileWriter solutionNum_fw = new FileWriter(fileSolutionNum, true);
			BufferedWriter solutionNum_bw = new BufferedWriter(solutionNum_fw);*/

//			FileWriter costObj_fw = new FileWriter(fileCostObj.getAbsoluteFile());
//			BufferedWriter costObj_bw = new BufferedWriter(costObj_fw);
//
//			FileWriter securityObj_fw = new FileWriter(fileSecurityObj.getAbsoluteFile());
//			BufferedWriter securityObj_bw = new BufferedWriter(securityObj_fw);

			/*FileWriter migrationPlan_fw = new FileWriter(fileMigrationPlan, true);
			BufferedWriter migrationPlan_bw = new BufferedWriter(migrationPlan_fw);*/
			// print the results

			System.out.println("Solution \t Cost \t Security \t Cloud Migration Plan \n");
			System.out.println("NOTE THAT TWO MIGRATION CONSTRAINT ELEMENTS ARE MISSING FROM PLAN DISPLAY BELOW \n\n");
			for (int i = 0; i < result.size(); i++) {
				Solution solution = result.get(i);
				double[] objectives = solution.getObjectives();


				/*solutionNum_bw.write(i+1);
				solutionNum_bw.newLine();*/

//				costObj_bw.write(Double.toString(objectives[0]));
//				costObj_bw.newLine();
//
//				securityObj_bw.write(Double.toString(objectives[1]));
//				securityObj_bw.newLine();

				/*migrationPlan_bw.write(decisionVariable);
				migrationPlan_bw.newLine();*/

				// negate objectives to return them to their maximized form
				//objectives = Vector.negate(objectives);

				System.out.print(i+1 + "\t");
				System.out.print(objectives[0] + "\t");
				System.out.print(new DecimalFormat("#0.00").format(objectives[1]) + "\t");
				//System.out.print(solution.getVariable(0) + "\n");		//USe for non federated
				//for federated :: below
				for (int k = 0; k < 14; k++){
					System.out.print(EncodingUtils.getInt(solution.getVariable(k)));
				}
				System.out.println();



			}//END FOR
			//solutionNum_bw.close();
//			costObj_bw.close();
//			securityObj_bw.close();
			//migrationPlan_bw.close();
		}//END TRY
//		catch(IOException e){
//			e.printStackTrace();
//		}

		/*//data collected by the instrumentor is stored in the accumulator object
		//GenerationalDistance
		Accumulator accumulator = instrumenter.getLastAccumulator();
		for (int i=0; i<accumulator.size("NFE"); i++) {
			System.out.println(accumulator.get("NFE", i) + "\t" +
					accumulator.get("GenerationalDistance", i));
		}*/

	}//END MAIN

//}//END CLASS 
