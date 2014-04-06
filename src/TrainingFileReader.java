/*
 * @author - Pradeep Prabakar (ppravind@cs.cmu.edu)
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import Jama.Matrix;

public class TrainingFileReader {
	
	/*
	 * Class Methods to read a file for Collaborative filtering and converts into suitable internal structures
	 * for produces recommendations
	 */
	
	class MatrixDimensions
	{
		public int rowLen;
		public int colLen;
		
		public MatrixDimensions(int rLen,int cLen)
		{
			rowLen = rLen;
			colLen = cLen;
		}
		
		public void printDimensions()
		{
			System.out.print(rowLen+" "+colLen);			
		}
	}
	
	private String ipFile;		//The input File	
	public Matrix userItemMatrix; // The user Item matrix 
	
	public TrainingFileReader(String fileName)
	{
		/*
		 * Initialize the input File to be read  
		 */
		ipFile = fileName;		
		
	}
	
	public MatrixDimensions fetchUserItemMatrixDimensions()
	{
		/*
		 *  Prior to initializing the matrix, we need to get N X M dimensions.
		 *  N - number of users in the training data
		 *  M - number of items in training data.
		 *  Read through input file and get count of unique number of items and users.
		 */
		System.out.println("Fetching dimensions of the matrix");
		BufferedReader reader;
		HashMap<Integer,Boolean> itemCheckMap = new HashMap<Integer,Boolean>(); 
		HashMap<Integer,Boolean> userCheckMap = new HashMap<Integer,Boolean>();
		
		int userCount = 0;
		int itemCount = 0;
		int maxItem = 0;
		int maxUser = 0;
				
		
		try {
			reader = new BufferedReader(new FileReader(ipFile));			
			/*
			 * Read each line and build the Item-User  matrix 
			 */
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					   // process the line.
					line = line.trim();
					String[] items = line.split(",");
					
					int item = Integer.parseInt(items[0]);
					int user =  Integer.parseInt(items[1]);
					//System.out.println(item + " " +user);
					
					if(item>maxItem)
						maxItem = item;
					if(user>maxUser)
						maxUser = user;						
					
					/*
					if(!itemCheckMap.containsKey(item))
					{
						itemCount+=1;
						itemCheckMap.put(item, true);
					}
					
					if(!userCheckMap.containsKey(user))
					{
						userCount+=1;
						userCheckMap.put(user, true);
					}
					*/
					
					}
				return new MatrixDimensions(maxUser,maxItem);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
			e.printStackTrace();
			return null;
		}
	}
	
	public void processTrainingFile()
	{
		/*
		 * Main method that reads the file and converts the file input to internal data structures for fast 
		 * similarity computation.
		 * 
		 * Each line in the target file is of the following pattern: itemId, userId, rating, info1, info2,..info 
		 * Each line is comma separated with itemId,userID and rating being compulsory attributes.
		 * After these compulsory attributes - additional metadata can be added such as "date" etc. (Eg info1 could
		 * be the date of the rating.)
		 */
		
		BufferedReader reader;
		MatrixDimensions matrixDimensions = fetchUserItemMatrixDimensions();
		matrixDimensions.printDimensions();
		userItemMatrix = new Matrix(matrixDimensions.rowLen+1, matrixDimensions.colLen+1); // An Empty matrix of zeros
		
		try {
			System.out.println("Building a Matrix");
			reader = new BufferedReader(new FileReader(ipFile));
			
			/*
			 * Read each line and build the Item-User  matrix 
			 */
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					
					line = line.trim();
					String[] items = line.split(",");
					
					int item = Integer.parseInt(items[0]);
					int user =  Integer.parseInt(items[1]);
					int rating = Integer.parseInt(items[2]);
					
					// Set the corresponding user-item entry in the matrix					
					userItemMatrix.set(user,item,rating);					
				}
				
				/*
				 * Perform imputation. To handle missing values and zero computation in the dot Product.
				 */
				userItemMatrix = MatrixHelper.getSubtractedMatrix(userItemMatrix, 3);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
			e.printStackTrace();
			
		}		
		
		
	}
	
	public static void main(String args[])
	{
		/*
		 * Test the Matrix !!
		 */
		
		TrainingFileReader r = new TrainingFileReader("/home/pradeep/courses/IR/HW4/HW3_data/train.csv");
		r.processTrainingFile();		
		System.out.println("Built a Matrix");
		
	}

}
