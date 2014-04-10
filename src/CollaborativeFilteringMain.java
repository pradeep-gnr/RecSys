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
	public static Matrix itemUserMatrix;
	public static Matrix itemSimMatrix; 
	public static Matrix origUserItemMatrix;
	public static HashMap<Integer,Boolean> userCheckMap; // List of users in the training corpus.
	public static HashMap<Integer,Boolean> itemCheckMap; //List of items in the training corpus.
	public static HashMap<Integer,HashMap<String,Double>> userMeanVarianceMap;
	
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
	
	public static void runMemBasedExperiment()
	{
		/*
		 * Run a memory based collaborative filtering model and write prediction outputs to file.
		 */		
		
		// Read the training file and generate user-item matrix
		TrainingFileReader trainReader = new TrainingFileReader(params.get("trainFile"));		
		trainReader.processTrainingFile();	
		userItemMatrix = trainReader.userItemMatrix;
		userMeanVarianceMap = trainReader.userMeanVarianceMap;
	//	System.out.println(userMeanVarianceMap);
		//origUserItemMatrix = trainReader.origUserItemMatrix;
		
		String userSimilarityMatrixFile=null;
		
		// Compute User-User similarty matrix or re-use existing matrix.
		if(params.get("similarity").equals("cos"))			
		{
			if(params.get("Standardization").equals("yes"))
				userSimilarityMatrixFile=params.get("StandardUserSimilarityCosMatrixPath");
			else
				userSimilarityMatrixFile= params.get("UserSimilarityCosMatrixPath");
		}
		
		else if(params.get("similarity").equals("dot"))
		{
			if(params.get("Standardization").equals("yes"))
				userSimilarityMatrixFile=params.get("StandardUserSimilarityDotMatrixPath");
			else
				userSimilarityMatrixFile= params.get("UserSimilarityDotMatrixPath");
		}
		
		System.out.println(userSimilarityMatrixFile);
		
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
	//	MatrixHelper.printMatrix(userItemMatrix);
		System.out.println("\n\n");
//		MatrixHelper.printMatrix(userSimMatrix);
		System.out.println("\n\n");
		try {
			NewMemoryBasedExperimenter.runExperiment();
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

		// Read the training file and generate user-item matrix		
		TrainingFileReader trainReader = new TrainingFileReader(params.get("trainFile"));		
		trainReader.processTrainingFile();	
		itemUserMatrix = trainReader.userItemMatrix.transpose();
		

		String itemSimilarityMatrixFile=null;
		
		// Compute User-User similarty matrix or re-use existing matrix.
		if(params.get("similarity").equals("cos"))
		{
			itemSimilarityMatrixFile= params.get("ItemSimilarityCosMatrixPath");
		}
		
		else if(params.get("similarity").equals("dot"))
		{
			itemSimilarityMatrixFile= params.get("ItemSimilarityDotMatrixPath");
		}
		
//		System.out.println(itemSimilarityMatrixFile);
		
		if(new File(itemSimilarityMatrixFile).isFile())
		{
			/*
			 * Similarity File exists. Has already been cached. No need to reCompute similarity matrix. Just Load it 
			 */

				FileInputStream fin;
				try {
					fin = new FileInputStream(itemSimilarityMatrixFile);
					 ObjectInputStream ois;
					try {
						ois = new ObjectInputStream(fin);
						try {
							
							/*
							 * Main code.
							 */
							System.out.println("Fetching rrrr");
							itemSimMatrix = (Matrix) ois.readObject();
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
			System.out.println("Computing Item-Item similariy matrix");
			System.out.println("  " + itemUserMatrix.getRowDimension() + "  "+itemUserMatrix.getColumnDimension());
			ItemSimilarityMatrixGenerator test = new ItemSimilarityMatrixGenerator(itemUserMatrix);			
			//UserSimilarityMatrixGenerator test = new UserSimilarityMatrixGenerator(A);
			test.generateItemSimilarityMatrix(params.get("similarity"));
			itemSimMatrix = test.itemSimMatrix;
			//MatrixHelper.printMatrix(test.itemSimMatrix);
			// Write the Matrix to File
			
			//System.out.println("PRior to writing matrix");
			//System.out.println(itemSimMatrix);
			MatrixHelper.writeMatrixToFile(test.itemSimMatrix, itemSimilarityMatrixFile );	
			itemSimMatrix = test.itemSimMatrix;
			}
		
		try {
			System.out.println(itemSimMatrix);
			NewModelBasedExperimenter.runExperiment();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
 
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
			runMemBasedExperiment();	
			
			System.out.println("DONE : File is writtem");
			
		}
		
		if(params.get("experiment").equals("model"))
		{
			/*
			 * Run a Model based collaborative filtering experiment using parameters and Output predictions to prediction output File.
			 */
			runModelBasedExperiment();
			System.out.println("DONE : File is writtem");
			/*
			 * Corpus Exploration.
			 */
			/*
			if(params.get("CorpusExploration").equals("yes"))
			{
				
				exploreCorpus();				
			}
		*/
		}
		
		
		
		
		System.out.println(params.toString());
	   
	}
	
	public static void exploreCorpus()
	{
		/*
		 * Corpus explorer code.
		 */
		
		// Number of items.
		System.out.println("Number of movies :"+itemCheckMap.size());
		System.out.println("Number of movies :"+userCheckMap.size());
		
		//Reverse imputation before computing.
		
		// Number of times an item was rated a certain value.
		System.out.println("Number of times a movie was rated 1 :" + MatrixHelper.countInMatrix(userItemMatrix, 1));
		System.out.println("Number of times a movie was rated 3 :" + MatrixHelper.countInMatrix(userItemMatrix, 3));
		System.out.println("Number of times a movie was rated 5 :" + MatrixHelper.countInMatrix(userItemMatrix, 5));
		
		// The average rating across all movies.
		// compute per user statistics.
		try {
			Vector userRow = MatrixHelper.fetchIthRow(userItemMatrix, 4321);
			/*
			 * Compute user statistics.
			 */
			int totalRatings = 0;
			HashMap<Double,Integer> userItemCountMap= new HashMap<Double,Integer>();
			
			double totalScore = 0;
			int count;
			
			for(int i=0;i<userRow.elements.length;i++)
			{
				if(userRow.elements[i]!=0)
				{
					totalRatings+=1;					
					double rating =userRow.elements[i];
					totalScore=totalScore+rating;
					if(userItemCountMap.containsKey(rating))
					{
						
						count =  userItemCountMap.get(rating);
						userItemCountMap.put(rating, count+1);
					}
					else
					{
						userItemCountMap.put(rating, 1);
					}
				}					
			}
			
			System.out.println(userItemCountMap);
			System.out.println("Number of movies rated by 4321  :"+totalRatings);
			System.out.println("Number of movies with rating 1 : " + userItemCountMap.get(1));
			System.out.println("Number of movies with rating 3 : " + userItemCountMap.get(3));
			System.out.println("Number of movies with rating 5 : " + userItemCountMap.get(5));
			
			System.out.println("The average rating for this user : "+ totalScore/totalRatings);
			
		} catch (RowOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Per Item statistics.
		try {
			Vector itemColumn = MatrixHelper.fetchIthColumn(userItemMatrix,3);			
			int totalRatings = 0;
			HashMap<Double,Integer> itemUserCountMap= new HashMap<Double,Integer>();
			
			double totalScore = 0;
			
			for(int i=0;i<itemColumn.elements.length;i++)
			{
				if(itemColumn.elements[i]!=0)
				{
					totalRatings+=1;
					double rating =itemColumn.elements[i];
					totalScore=totalScore+rating;
					if(itemUserCountMap.containsKey(rating))
					{
						int count =  itemUserCountMap.get(rating);
						itemUserCountMap.put(rating, count+1);
					}
					else
					{
						itemUserCountMap.put(rating,1);
					}
				}					
			}
			
			System.out.println(itemUserCountMap);
			
			System.out.println("The number of users rating this movie : "+totalRatings);
			System.out.println("The number of times a user gave a rating 1 : "+itemUserCountMap.get(1.0));
			System.out.println("The number of times a user gave a rating 3 : "+itemUserCountMap.get(3.0));
			System.out.println("The number of times a user gave a rating 5 : "+itemUserCountMap.get(5.0));
			System.out.println("The average rating for this movie : "+ totalScore/totalRatings);
		} catch (ColumnOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * Nearest neighbor 
		 */		
		
		}
	

}


