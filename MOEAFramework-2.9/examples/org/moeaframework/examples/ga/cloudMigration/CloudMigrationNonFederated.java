package org.moeaframework.examples.ga.cloudMigration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

public class CloudMigrationNonFederated implements Problem{
	/*
	 * Declare the variables that will be involved in the
	 * problem
	 */
	private String[] C_sw; 					//Cost to buy software for an element implementation
	private String[] C_hw; 					//Cost to buy hardware for an element hosting 
	private String[] C_legacy;				//Cost to rewrite a legacy element
	private String[] C_scc_client_cloud;	//Cost to implement a security measure on the cloud by the client (IaaS)
	private String[] C_scc_vendor;			//Cost of extra security measure implemented by the cloud 
	private String[] C_scc_client;			//Cost of security measures when hosted by client
	private String[] C_vendor;				//Cost of vendor services per month
	private String[] scalability;			//Elasticity factor involved for an element
	private String[] cloudSecurityCoverageFlag;	//Whether cloud or client implements security on the cloud platform
	private String[] legacySystemFlag;
	private int totalNodes;
	private String[] securityRiskImpactLevel;
	private String[] securityCoverage;

	//private double securityRiskImpactLevel;

	//Threshold values
	private double totalThresholdCost;
	private double totalResidualDamageThreshold;

	//Migration constraint flag
	private String[] migrationConstraint;
	private int noOfMigrationConstraints = 0;

	//clustering label
	private String[] clusteringLabel;

	//maximum number of clusters 
	private int maximumCluster = 0;

	//Array of boolean for clustering computations 
	private boolean[] clustersPrivateHosting;
	private boolean[] clusterCloudHosting;
	
	
	//Sobol parameter storage array 
	private String[] sobol_parameters;

	private int count;



	/*
	 * READ FILE and get values into variables 
	 */
	public CloudMigrationNonFederated() {
		count = CloudMigrationExampleSensiAnalysisVer2.count;
		String UserInput = null;
		String sobolParameter = null;
		
		sobolParameter = "C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\sobolParameterSets.txt";
		
		switch(count){
		case 1: UserInput = "C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\inputNormalCSP1.txt";
			break;
		case 2: UserInput ="C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\inputNormalCSP2.txt";
			break;
		case 3: UserInput = "C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\inputNormalCSP3.txt";
			break;
		case 4: UserInput = "C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\inputFtCSP1.txt";
			break;
		case 5:UserInput = "C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\inputFtCSP2.txt";
			break;
		case 6:UserInput = "C:\\Users\\asrp6\\Google Drive\\Workspace\\MOEAFramework-2.9\\examples\\org\\moeaframework\\examples\\ga\\cloudMigration\\inputFtCSP3.txt";
			break;
		default: System.out.println("Incorrect case number");
			break;
			
		
		/*FOR LINUX */	
		/*case 1: UserInput = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/inputNormalCSP1.txt";
		break;
		case 2: UserInput = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/inputNormalCSP2.txt";
			break;
		case 3: UserInput = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/inputNormalCSP3.txt";
			break;
		case 4: UserInput = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/inputFtCSP1.txt";
			break;
		case 5:UserInput = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/inputFtCSP2.txt";
			break;
		case 6:UserInput = "/home/joshua/Workspace/MOEAFramework-2.6/examples/org/moeaframework/examples/ga/cloudMigration/inputFtCSP3.txt";
			break;
		default: System.out.println("Incorrect case number");
			break;*/
		}
		String line = null;

		int lineNumber = 0;

		try{
			FileReader filereader = new FileReader(UserInput);			
			BufferedReader bufferedReader = new BufferedReader(filereader);

			while((line = bufferedReader.readLine()) != null){
				lineNumber++;
				//Nine different inputs
				switch(lineNumber){
				case 1: C_sw = line.split(" ");
				break;
				case 2: C_hw = line.split(" ");
				break;
				case 3: C_legacy = line.split(" ");
				break;
				case 4: C_scc_client_cloud = line.split(" "); 
				break;
				case 5: C_scc_vendor = line.split(" "); 
				break;
				case 6: C_scc_client = line.split(" "); 
				break;
				case 7: C_vendor = line.split(" "); 
				break;
				case 8: scalability = line.split(" "); 
				break;
				case 9: cloudSecurityCoverageFlag = line.split(" "); 
				break;
				case 10: legacySystemFlag = line.split(" ");
				break;
				case 11: totalNodes = new Integer(line);
				break;
				case 12: securityRiskImpactLevel = line.split(" ");
				break;
				case 13: totalThresholdCost = new Double(line);
				break;
				case 14: totalResidualDamageThreshold = new Double(line);
				break;
				case 15: securityCoverage = line.split(" ");
				break;
				case 16: migrationConstraint = line.split(" ");
				break;
				case 17: clusteringLabel = line.split(" ");
				break;
				default: System.out.println("Invalid Line number \n Check file.\n");
				break;

				}//SWITCH CASE
			}//WHILE
			
			//READ SOBOL parameters 
			/*FileReader filereader_sobol = new FileReader(sobolParameter);			
			BufferedReader bufferedReader_sobol = new BufferedReader(filereader_sobol);

			int linenumber_sobol = 0; 
			
			while((line = bufferedReader_sobol.readLine()) != null){
				linenumber_sobol++;
				
					if (linenumber_sobol == CloudMigrationExampleSensiAnalysisVer2.sobolindex){
						sobol_parameters = line.split(" ");
					}
						
			}//END OF SOBOL READ WHILE
			
			totalThresholdCost = new Double(sobol_parameters[0]);
			totalResidualDamageThreshold = new Double(sobol_parameters[1]);*/
			

			//FOR MIGRATION CONSTRAINT COMPUTATIONS		
			for (int i = 0; i < totalNodes; i++){
				if(Double.parseDouble(migrationConstraint[i]) == 1)
					noOfMigrationConstraints++; 
			}
			//FINDING THE NUMBER OF CLUSTERS - MAX cluster value gives the number of cluster
			// with DBs starting from 1
			for (int i = 0; i < totalNodes; i++){

				if (maximumCluster < (Integer.parseInt(clusteringLabel[i]))){
					maximumCluster = Integer.parseInt(clusteringLabel[i]);
				}

			}

			clustersPrivateHosting = new boolean[maximumCluster+1];
			clusterCloudHosting = new boolean[maximumCluster+1];

			for (int i = 0; i <= maximumCluster; i++){
				clustersPrivateHosting[i] = false;
				clusterCloudHosting[i] = false;
			}


			bufferedReader.close();
		//	bufferedReader_sobol.close();
		}//TRY
		catch(FileNotFoundException ex) {
			System.out.println(
					"Unable to open file '" + 
							UserInput + "'");                
		}
		catch(IOException ex) {
			System.out.println(
					"Error reading file '" 
							+ UserInput + "'");
		}

	}//CONSTRUCTOR END

	/*
	 * (non-Javadoc)
	 * @see org.moeaframework.core.Problem#getName()
	 * Beneath this point I took compiler suggestion and 
	 * added unimplemented methods
	 */
	@Override
	public String getName() {

		return "CloudMigration";
	}

	@Override
	public int getNumberOfVariables() {

		return 1;
	}

	@Override
	public int getNumberOfObjectives() {

		return 2;
	}

	@Override
	public int getNumberOfConstraints() {

		return 2;
	}

	@Override
	public void evaluate(Solution solution) {

		//Where did I specify the size of P? who gives a shit?!
		boolean[] P = EncodingUtils.getBinary(solution.getVariable(0));
		double[] f = new double[2]; 
		double[] C_vendorTemp = new double[totalNodes];
		double[] C_legacyTemp = new double[totalNodes];
		double[] C_cloud= new double[totalNodes];
		double[] C_Venscc= new double[totalNodes];
		double[] C_client= new double[totalNodes];


		double residualDamage = 0.0;
		double totalCost = 0.0;
		//double[] migrationConstraintTemp = new double[totalNodes];
		int currentNumberOfMigConstraintsOccured = 0;

		double[] constraint = new double[2];



		//Calculate Cost for migrated and not migrated elements 
		for (int i = 0; i < totalNodes; i++){
			//If mig constraint is there; evaluate as client hosted
			if(Double.parseDouble(migrationConstraint[i]) == 1)
			{
				currentNumberOfMigConstraintsOccured++;
				C_client[i] = Double.parseDouble(C_scc_client[i]);
				//C_client[i] = Double.parseDouble(C_sw[i]) + Double.parseDouble(C_hw[i]);
				residualDamage += Double.parseDouble(securityRiskImpactLevel[i]);

				if (Integer.parseInt(clusteringLabel[i]) == 1){
					C_client[i] += Double.parseDouble(C_sw[i]) + Double.parseDouble(C_hw[i]);						
				}
				else{
					if (!(clustersPrivateHosting[Integer.parseInt(clusteringLabel[i])])){
						C_client[i] += Double.parseDouble(C_sw[i]) + Double.parseDouble(C_hw[i]);
						clustersPrivateHosting[Integer.parseInt(clusteringLabel[i])] = true;
					}//eND IF
				}//END ELSE

			}//END MIGRATION IF
			else{//IF NO MIG CONSTRAINT
				if (P[i - currentNumberOfMigConstraintsOccured]){

					C_legacyTemp[i] = Double.parseDouble(C_legacy[i])*Double.parseDouble(legacySystemFlag[i]);

					//DATABESE label
					if (Integer.parseInt(clusteringLabel[i]) == 1){
						C_vendorTemp[i] = Double.parseDouble(C_vendor[i])*Double.parseDouble(scalability[i])*60;					
					}
					else{
						if (!(clusterCloudHosting[Integer.parseInt(clusteringLabel[i])])){
							C_vendorTemp[i] = Double.parseDouble(C_vendor[i])*Double.parseDouble(scalability[i])*60;
							clusterCloudHosting[Integer.parseInt(clusteringLabel[i])] = true;
						}//eND IF
					}//END ELSE						

					/*C_vendorTemp[i] = Double.parseDouble(C_vendor[i])*Double.parseDouble(scalability[i])*60;	//For 5 year period of depericiation cost 
						C_legacyTemp[i] = Double.parseDouble(C_legacy[i])*Double.parseDouble(legacySystemFlag[i]);*/
					
					//If security coverage isn't present
					if(new Integer(securityCoverage[i]) == 0){
						//residualDamage += Double.parseDouble(securityRiskImpactLevel[i]);
						C_Venscc[i] = (Double.parseDouble(C_scc_client_cloud[i])*(1-Double.parseDouble(cloudSecurityCoverageFlag[i])))
								+ (Double.parseDouble(cloudSecurityCoverageFlag[i])* Double.parseDouble(C_scc_vendor[i]));
						//NEW ADDITION HERE.
						//residualDamage += Double.parseDouble(securityRiskImpactLevel[i]);
					}else{	//SC present
						C_Venscc[i] = 0;
					}
					C_cloud[i] = C_vendorTemp[i]+ C_legacyTemp[i] + C_Venscc[i];



				}else{//NOT migrated
					//else 0

					C_client[i] = Double.parseDouble(C_scc_client[i]);
					//C_client[i] = Double.parseDouble(C_sw[i]) + Double.parseDouble(C_hw[i]);
					residualDamage += Double.parseDouble(securityRiskImpactLevel[i]);

					//DATABESE label
					if (Integer.parseInt(clusteringLabel[i]) == 1){
						C_client[i] += Double.parseDouble(C_sw[i]) + Double.parseDouble(C_hw[i]);						
					}
					else{
						if (!(clustersPrivateHosting[Integer.parseInt(clusteringLabel[i])])){
							C_client[i] += Double.parseDouble(C_sw[i]) + Double.parseDouble(C_hw[i]);
							clustersPrivateHosting[Integer.parseInt(clusteringLabel[i])] = true;
						}//eND IF
					}//END ELSE

				}//ESLE NOT MIGRATED
			}//ELSE NO MIGRATION CONSTRAINT

			//Total for each i 
			totalCost += C_cloud[i] + C_client[i];
		}//END FOR





		f[0] = totalCost;
		f[1] = residualDamage;


		//Print Check Output 
		//System.out.println(totalCost + "    " +  residualDamage + "     "+  totalThresholdCost);

		//CONSTRAINT CHECK		
		if(totalCost <= totalThresholdCost)
			constraint[0] = 0;
		else
			constraint[0] = totalCost - totalThresholdCost;

		if(residualDamage <= totalResidualDamageThreshold)
			constraint[1] = 0;
		else
			constraint[1] = residualDamage - totalResidualDamageThreshold;

		solution.setObjectives(f);	//minimize	
		solution.setConstraints(constraint);	//constraint


	}//END EVALUate()

	@Override
	public Solution newSolution() {
		//1 decision variable, 2 objectives, 2 constraints
		Solution solution = new Solution(1, 2, 2);
		//solution.setVariable(0, EncodingUtils.newBinary(totalNodes));
		solution.setVariable(0, EncodingUtils.newBinary(totalNodes - noOfMigrationConstraints));
		if(solution.violatesConstraints())
			System.out.println("Violated");
		
		return solution;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		//Do nothing

	}

}//END CLASS
