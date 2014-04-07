
import Jama.Matrix;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.TreeMap;
public class MemoryBasedExperimenter {
	/*
	 * Experiments for memory based collaborative Filtering 
	 */
	
	public String ipFile;
	public String opFile;
	public static Matrix userSimMatrix; 
	public static Matrix userItemMatrix;
	
	
	public MemoryBasedExperimenter(String ipFile, String opFile, String userSimMatrixFile)
	{
		/*
		 * Initialize Constructor
		 */
		this.ipFile =ipFile;
		this.opFile = opFile;
		
		readUserSimMatrix(userSimMatrixFile); // Read and intitiaize the user Similarity matrix		
		
	}
	
	public void readUserSimMatrix(String userSimMatrixFile)
	{
		/*
		 * Read a the pre-computed similarity matrix from file
		 */
	    FileInputStream fin;
		try {
			fin = new FileInputStream(userSimMatrixFile);
			 ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(fin);
				 try {
					Matrix userSimMatrix  = (Matrix) ois.readObject();
					this.userSimMatrix=  userSimMatrix;
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
	
	public void runExperiment()
	{
		/*
		 * Main Experimenter. This method does the follows.
		 * 1) Load from a file the pre-computed user-user similarity ratings.
		 * 2) Read a testing/file line by line in MovieId, userId format, compute predictions for each movieId, userId pair.
		 * 		2.1) Generate K-Most similar users for the corresponding user
		 * 		2.2) User mean/weighted Mean to generate movie ratings.
		 * 		2.3) Write prediction to File
		 */
		
		try {
			BufferedReader reader;
			System.out.println("Staring Experiment");
			reader = new BufferedReader(new FileReader(this.ipFile));
			
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
					//int rating = Integer.parseInt(items[2]);
					
					// Set the corresponding user-item entry in the matrix
					TreeMap<Integer,Vector> topKProfiles;
					
					
				}
				
				/*
				 * Perform imputation. To handle missing values and zero computation in the dot Product.
				 */
				
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

}
