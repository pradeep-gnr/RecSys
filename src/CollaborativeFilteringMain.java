import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

import Jama.Matrix;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
public class CollaborativeFilteringMain {
	
	/*
	 * Main class for all the collaborative Filtering experiments. 
	 */
	public static HashMap<String,String> params;
	public static Matrix userItemMatrix;
	public static Matrix userSimMatrix;
	public static HashMap<Integer,Boolean> userCheckMap; // List of users in the training corpus.
	public static HashMap<Integer,Boolean> itemCheckMap; //List of items in the training corpus.
	
	static{
		userCheckMap = new HashMap<Integer,Boolean>();
		 itemCheckMap = new HashMap<Integer,Boolean>();
	}
	public void CollaborativeFilteringMain()
	{
		/*
		 * Construtor
		 */
		userItemMatrix = null;
		userSimMatrix=null;		
	}
	
	public static void runMemoryBasedExperiment()
	{
		/*
		 * Run a memory based collaborative filtering model and write prediction outputs to file.
		 */		
		
		// Read the training file and generate user-item matrix
		TrainingFileReader trainReader = new TrainingFileReader(params.get("trainFile"));		
		trainReader.processTrainingFile();	
		userItemMatrix = trainReader.userItemMatrix;
		
		String userSimilarityMatrixFile=null;
		
		// Compute User-User similarty matrix or re-use existing matrix.
		if(params.get("similarity").equals("cos"))
		{
			userSimilarityMatrixFile= params.get("UserSimilarityCosMatrixPath");
		}
		
		else if(params.get("similarity").equals("dot"))
		{
			userSimilarityMatrixFile= params.get("UserSimilarityDotMatrixPath");
		}
		
		
		if(new File(userSimilarityMatrixFile).isFile())
		{
			/*
			 * Similarity File exists. Has already been cached. No need to reCompute similarity matrix. Just Load it 
			 */

				FileInputStream fin;
				try {
					fin = new FileInputStream(userSimilarityMatrixFile);
					 ObjectInputStream ois;
					try {
						ois = new ObjectInputStream(fin);
						try {
							
							/*
							 * Main code.
							 */
							userSimMatrix = (Matrix) ois.readObject();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						  ois.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  
			
		}
		
		else{
				/*
				 * File does not exist. Must recompute the similarity matrix and cache it for future experiments.
				 */
			System.out.println("Computing User-User similariy matrix");
			UserSimilarityMatrixGenerator test = new UserSimilarityMatrixGenerator(userItemMatrix);			
			//UserSimilarityMatrixGenerator test = new UserSimilarityMatrixGenerator(A);
			test.generateUserSimilarityMatrix(params.get("similarity"));
			
			//MatrixHelper.printMatrix(test.userSimMatrix);
			// Write the Matrix to File
			MatrixHelper.writeMatrixToFile(test.userSimMatrix, userSimilarityMatrixFile );	
			userSimMatrix = test.userSimMatrix;
			}
		
		/*
		 * 
		 */	
		//MatrixHelper.printMatrix(userItemMatrix);
		//MatrixHelper.printMatrix(userSimMatrix);
		try {
			MemoryBasedExperimenter.runExperiment();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void runModelBasedExperiment()
	{
		/*
		 * Run a memory based collaborative filtering model and write prediction outputs to file.
		 */
	}	
	
	public static void main(String args[])
	{
		/*
		 * Read and initialize the parameter File 
		 */
		params = new HashMap<String, String>();
		
	    Scanner scan;
		try {
			scan = new Scanner(new File(args[0]));
		     String line = null;
			    do {
			      line = scan.nextLine();
			      String[] pair = line.split("=");
			      params.put(pair[0].trim(), pair[1].trim());
			    } while (scan.hasNext());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * Must add error handling capabilities for missing parameters.
		 */		
		System.out.println(params.toString());
		
		if(params.get("experiment").equals("memory"))
		{
			/*
			 * Run a Memory based collaborative filtering experiment and Output predictions to prediction output File
			 */
			runMemoryBasedExperiment();
		}
		
		if(params.get("experiment").equals("model"))
		{
			/*
			 * Run a Model based collaborative filtering experiment using parameters and Output predictions to prediction output File.
			 */
			runModelBasedExperiment();
		}
		
		
		
		System.out.println(params.toString());
	   
	}
	

}
