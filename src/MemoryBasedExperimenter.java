
import Jama.Matrix;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.TreeMap;
import java.util.LinkedHashMap;
public class MemoryBasedExperimenter {
	/*
	 * Experiments for memory based collaborative Filtering 
	 */	
	
	public MemoryBasedExperimenter()
	{
		/*
		 * Initialize Constructor
		 */
		
	}
	
	public static void runExperiment()
	{
		/*
		 * Main Experimenter. This method does the follows.
		 * 1) Load from a file the pre-computed user-user similarity ratings.
		 * 2) Read a testing/file line by line in MovieId, userId format, compute predictions for each movieId, userId pair.
		 * 		2.1) Generate K-Most similar users for the corresponding user
		 * 		2.2) User mean/weighted Mean to generate movie ratings.
		 * 		2.3) Write prediction to File
		 */
		
		String testFile = CollaborativeFilteringMain.params.get("testFile");
		try {
			BufferedReader reader;
			System.out.println("Staring Experiment");
			reader = new BufferedReader(new FileReader(testFile));
			
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
					
					// Get top K profiles for the given user
					int K = Integer.parseInt(CollaborativeFilteringMain.params.get("K"));
					LinkedHashMap<Integer,Vector> topKProfiles =  KSimilarUsersComputer.getKTopProfiles(user,K);
					
				}
				
				
				
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
