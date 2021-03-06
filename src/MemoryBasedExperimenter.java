
import Jama.Matrix;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.io.File;
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
	
	public static void runExperiment() throws IOException
	{
		/*
		 * Lazy learner. Memory based experimenter.
		 * This method does the following.
		 * 1) Read a test file
		 * 2) Read each line, for each user, compute K-nearest neighbors
		 * 		2.1) If a user, item is not in training data, then do error handling.
		 * 		
		 * 3) Compute weighted/normal mean for the K-nearest neighbors
		 * 
		 */
		
		String testFile = CollaborativeFilteringMain.params.get("testFile");
		File file = new File(CollaborativeFilteringMain.params.get("predictionFile"));
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			BufferedReader reader;
			System.out.println("Staring Experiment");
			reader = new BufferedReader(new FileReader(testFile));
			
			/*
			 * Read each line and build the Item-User  matrix 
			 */
			String line;
			try {
				int k=0;
				while ((line = reader.readLine()) != null) {
					
					line = line.trim();
					Boolean ratingSet = false;
					String[] items = line.split(",");
					double rating=10000;					
					int item = Integer.parseInt(items[0]);
					int user =  Integer.parseInt(items[1]);
					//int rating = Integer.parseInt(items[2]);
					
					// Get top K profiles for the given user
					/*
					 * If both user and Item do not exist in the corpus, then return average rating.
					 */
					
					//System.out.println(CollaborativeFilteringMain.itemCheckMap);					
					if(!CollaborativeFilteringMain.userCheckMap.containsKey(user) && !CollaborativeFilteringMain.itemCheckMap.containsKey(item))
					{
						System.out.println("Both user and Item not found");
						rating=3;
						ratingSet=true;
					}
					
					
					/*
					 * First check if a user exists in the corpus. If the user is a new user, and we want a prediction for an item,
					 * take the average rating for the item across all users.
					 */
					
					if(!CollaborativeFilteringMain.userCheckMap.containsKey(user) && CollaborativeFilteringMain.itemCheckMap.containsKey(item))
					{
						/*
						 * get the average rating for the item. 
						 */
						//float rating = computeAverageItemRating(itemId);						
						try {
							System.out.println("user not found");
							rating = MatrixHelper.computeAverageinColumn(CollaborativeFilteringMain.userItemMatrix,item)+3;
							rating = new Float( Math.round(rating));
							ratingSet=true;
						} catch (ColumnOutOfBoundsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}			
							
					}
					
					if(CollaborativeFilteringMain.userCheckMap.containsKey(user) && !CollaborativeFilteringMain.itemCheckMap.containsKey(item))
					{
						/*
						 * If the item is not present in the corpora, get average rating for the user. 
						 */
						//float rating = computeAverageItemRating(itemId);						
						try {
							System.out.println("item not found");
							rating = MatrixHelper.computeAverageinRow(CollaborativeFilteringMain.userItemMatrix, user)+3;
							rating = new Float( Math.round(rating));
							ratingSet=true;
						} catch (RowOutOfBoundsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}		
					
					if(ratingSet==false)
					{
					int K = Integer.parseInt(CollaborativeFilteringMain.params.get("K"));
					
					/*
					 * For a given user, iterate through all users and get all similarity scores.
					 */
					SimilarUserMetrics profileMetrics =  LazyKSimilarUsersComputer.getKTopProfiles(user,K);	
					//System.out.println(topKProfiles);
					rating = MemoryRatingPredictor.predictRating(profileMetrics.resultMap, profileMetrics.userSimilarityMap,item, user);
					}
					/*
					 * Write the predictions to file.
					 */
					//int rat = (Integer) rating;
					System.out.println(k+"  "+rating);					
					bw.write(rating+"\n");
					k+=1;
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
		bw.close();
		
	}

}
