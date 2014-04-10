import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Iterator;
import Jama.Matrix;
import java.util.Map;

public class MemoryRatingPredictor {
	/*
	 * Predict rating of a movie based on Nearest neighbor profiles
	 */
	
	public void RatingPredictor()
	{
		/*
		 * Empty Constructor
		 */
	}
	
	public static float predictRating(LinkedHashMap<Integer, Vector> userProfileMap, HashMap<Integer,Double> userSimScoreMap, int itemId, int user)
	{
		/*
		 * @userProfileMap - A Map - Key=userId, value=userProfileVector
		 * @itemId - the item for which we want to predict the rating.
		 * Returns a rating for the item by looking at the user Profiles. 
		 */
		String mean = CollaborativeFilteringMain.params.get("mean");
		
			
		if(mean.equals("normal"))
		{
			/*
			 * Compute Normal mean.
			 */			
			Iterator it = userProfileMap.entrySet().iterator();
			int noNeighbors=0;
			double totalSum=0;			
			while (it.hasNext()) {
				/*
				 *  For all neighbors
				 */
				Map.Entry pairs = (Map.Entry)it.next();
				Vector curUserProfile = (Vector) pairs.getValue();
				double curScore = curUserProfile.elements[itemId]+3;
				//System.out.println(curScore);
				totalSum=totalSum+curScore;
				noNeighbors+=1;				
			}
			
			double rating = totalSum/noNeighbors;
			return new Float( Math.round(rating));			
		}
		
		if(mean.equals("weighted"))
		{
			/*
			 * Compute Weighted mean.			 * 
			 */
			
			/*
			 * Iterate once to compute minimum and maximum scores in the data points for normalization
			 * We want to normalize cosine similarities such that they fall in the range of 0-1
			 */
			//System.out.println("WEIGHTED");
			Iterator it = userProfileMap.entrySet().iterator();
			double minScore=Float.POSITIVE_INFINITY;
			double maxScore=Float.NEGATIVE_INFINITY;
			
			
			while (it.hasNext()) {
				
				Map.Entry pairs = (Map.Entry)it.next();
				//Vector curUserProfile = (Vector) pairs.getValue();
				//double curScore = curUserProfile.elements[itemId]+3;
				//double simScore =
				int curUser = (Integer) pairs.getKey();
				//double simScore = CollaborativeFilteringMain.userSimMatrix.get(user,curUser);
				double simScore = userSimScoreMap.get(curUser);
				
				if(simScore<minScore)
					minScore=simScore;
				
				if(simScore>maxScore)
					maxScore=simScore;				
				
			  }
			
			it = userProfileMap.entrySet().iterator();
			LinkedHashMap<Integer,Double> weightMap = new LinkedHashMap<Integer, Double>(); // Weights for each ith user in the k nearest neighbors
			
			double totalSum=0;
			while (it.hasNext()) {
				/*
				 *  For all neighbors
				 */
				Map.Entry pairs = (Map.Entry)it.next();
				int curUser = (Integer) pairs.getKey();
				//double simScore = CollaborativeFilteringMain.userSimMatrix.get(user,curUser);
				double simScore = userSimScoreMap.get(curUser);
				
				double normScore = (simScore-minScore)/(maxScore-minScore); // Normalize values between 0 and 1
				weightMap.put(curUser, normScore );
				totalSum+=normScore;
			}		
			
			/*
			 *Divide by the total sum to convert to probabilities 
			 */
			it = weightMap.entrySet().iterator();
			while (it.hasNext()) {
				/*
				 *  For all neighbors
				 */
				Map.Entry pairs = (Map.Entry)it.next();
				int curUser = (Integer) pairs.getKey();
				double score = (Double) pairs.getValue();
				//System.out.println(score);
				
				// generate new scores.
				weightMap.put(curUser,score/totalSum);			
		}
			
			/*
			 * Now compute Weighted mean using weights.
			 */
			
			it = userProfileMap.entrySet().iterator();
			double totalRating = 0; 
			
			while (it.hasNext()) {
				/*
				 *  For all neighbors
				 */
				Map.Entry pairs = (Map.Entry)it.next();
				int curUser = (Integer) pairs.getKey(); 
				Vector curUserProfile = (Vector) pairs.getValue();
				double curScore = curUserProfile.elements[itemId]+3;
				//System.out.println(weightMap.get(curUser) +"  " +curScore);
				
				/*
				 * Use current user's weight to generate weighted rating.
				 */
				totalRating = totalRating + (weightMap.get(curUser) *curScore);	
			}		
			return new Float( Math.round(totalRating));
		}
				
		return 0;
	}

}
