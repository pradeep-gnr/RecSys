import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.HashMap;


public class LazyKSimilarUsersComputer {
	/*
	 * A lazy computation of the K-nearest neighbors of a given user
	 */
	
	public static Vector computeSimilarityVector(int userId)
	{
		Vector userI;
		Vector userJ;
		
		int noUsers=CollaborativeFilteringMain.userItemMatrix.getRowDimension();
		String simType = CollaborativeFilteringMain.params.get("similarity");
		double[] simScoreArray= new double[noUsers];
		
		try {
			  userI = MatrixHelper.fetchIthRow(CollaborativeFilteringMain.userItemMatrix,userId);
			} catch (RowOutOfBoundsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
				
			} 
		for(int j=0;j<noUsers;j++)
				
			{
				/*
				 *  Compute similarity sim(u(i), u(j))
				 */				
			
			
				try {
					userJ = MatrixHelper.fetchIthRow(CollaborativeFilteringMain.userItemMatrix,j);
				} catch (RowOutOfBoundsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
					
				}
				if(simType.equals("dot"))
						{
							try {
								double simScore = SimilarityComputer.getDotProduct(userI, userJ);
								simScoreArray[j] = simScore;							
								
							} catch (DimensionMismatchException e) {
								// TODO Auto-generated catch block
								
								e.printStackTrace();
								return null;
							}
						}
				else
					{
					try {						
						double simScore = SimilarityComputer.getCosineSimilarity(userI, userJ);		
						simScoreArray[j] = simScore;
						//System.out.println(SimilarityComputer.getCosineSimilarity(userI, userJ));
						} catch (DimensionMismatchException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
						return null;
					}
				}
			}
		
		return new Vector(simScoreArray);
	}
	
	public static ArrayList<ValueIndexPair> getValuePairList(Vector userSimilarityVector)
	{
		/*
		 * Takes a vector of similarity scores of a user i with all users in the corpora and converts each
		 * entry into an value index pair entry
		 */
		ArrayList<ValueIndexPair> userSimilarityList = new ArrayList<ValueIndexPair>();
		
		for(int i=0;i<userSimilarityVector.elements.length;i++)
		{
			userSimilarityList.add(new ValueIndexPair(i,userSimilarityVector.elements[i] ));
		}		
		return userSimilarityList;
		
	}
		
	public static SimilarUserMetrics getKTopProfiles(int userId, int K)
	{
		/*
		 * This method does the following.
		 * @ K - number of nearest neighbors to fetch.
		 * 1) From the pre-computed user-user similarity matrix, it fetches the K-most similar users for a given user
		 * specified by a user-Id ui.
		 * 2) If the userId is not in the system, then special handling is required. 
		 */
		
		
		try {
			//Vector userSimilarityVector = MatrixHelper.fetchIthRow(CollaborativeFilteringMain.userSimMatrix, userId);			
			Vector userSimilarityVector = computeSimilarityVector(userId);
			ArrayList<ValueIndexPair> userSimilarityList = getValuePairList(userSimilarityVector);
			HashMap<Integer,Double> userSimilarityMap = new HashMap<Integer,Double> ();// HashMap where key=userId and value=similarity score of a user with a target user.
			Collections.sort(userSimilarityList);
			//ArrayList<Integer> userIndexList = new ArrayList<Integer>();
			LinkedHashMap<Integer,Vector> resultMap = new LinkedHashMap<Integer,Vector>();	
			int maxSize=K;
			
			for(int i=0;i<userSimilarityList.size();i++)
			{
				if(resultMap.size()<maxSize)
				{
					int curUserIndex = userSimilarityList.get(i).index; // For the ith top user in K most similar users
					double score = userSimilarityList.get(i).value;					
					Vector curUserProfile = MatrixHelper.fetchIthRow(CollaborativeFilteringMain.userItemMatrix, curUserIndex ); // The profile of the current user
					
					if(curUserIndex!=userId)
						resultMap.put(curUserIndex,curUserProfile);
						userSimilarityMap.put(curUserIndex, score);
				}
				else
					break;
			}

		//	System.out.println(userId + "  "+ resultMap.toString());
			return new SimilarUserMetrics(resultMap, userSimilarityMap);	
		} catch (RowOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	} 

}
