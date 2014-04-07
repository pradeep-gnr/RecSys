
import java.util.TreeMap;

import Jama.Matrix;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;


public class KSimilarUsersComputer {
	/*
	 * Computes K-nearest neighbors of a given user. In this case, we assume that pairwise similariy measures
	 * between all pairs of users have been computed.  
	 */
	
	public KSimilarUsersComputer()
	{
		/*
		 * Initialize the constructor
		 */
	}
	
	/*
	public static int[] getIndices(double[] originalArray)
	{
	    int len = originalArray.length;

	    double[] sortedCopy = originalArray.clone();
	    int[] indices = new int[len];

	    // Sort the copy
	    Arrays.sort(sortedCopy);
	    
	    ArrayUtils.reverse(sortedCopy);
	    System.out.println(Arrays.toString(sortedCopy));
	    //Arrays.sort(sortedCopy,Collections.reverseOrder());

	    // Go through the original array: for the same index, fill the position where the
	    // corresponding number is in the sorted array in the indices array
	    for (int index = 0; index < len; index++)
	    {
	        indices[index] = Arrays.binarySearch(sortedCopy, originalArray[index]);
	    	//System.out.println(Arrays.binarySearch(sortedCopy, originalArray[index]));
	    }
	    	

	    return indices;
	}
	*/
	
	public static void printArray(int[] array)
	{
		for(int i=0;i<array.length;i++)
			System.out.println(array[i]);
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
	
	public static LinkedHashMap<Integer,Vector> getKTopProfiles(int userId, int K)
	{
		/*
		 * This method does the following.
		 * @ K - number of nearest neighbors to fetch.
		 * 1) From the pre-computed user-user similarity matrix, it fetches the K-most similar users for a given user
		 * specified by a user-Id ui.
		 * 2) If the userId is not in the system, then special handling is required. 
		 */
		
		try {
			Vector userSimilarityVector = MatrixHelper.fetchIthRow(CollaborativeFilteringMain.userSimMatrix, userId);
			ArrayList<ValueIndexPair> userSimilarityList = getValuePairList(userSimilarityVector);
			
			Collections.sort(userSimilarityList);
			//ArrayList<Integer> userIndexList = new ArrayList<Integer>();
			LinkedHashMap<Integer,Vector> resultMap = new LinkedHashMap<Integer,Vector>();	
			int maxSize=K;
			
			for(int i=0;i<userSimilarityList.size();i++)
			{
				if(resultMap.size()<=maxSize)
				{
					int curUserIndex = userSimilarityList.get(i).index; // For the ith top user in K most similar users
					Vector curUserProfile = MatrixHelper.fetchIthRow(CollaborativeFilteringMain.userItemMatrix, curUserIndex ); // The profile of the current user
					
					if(curUserIndex!=userId)
						resultMap.put(curUserIndex,curUserProfile);					
				}
				else
					break;
			}

			System.out.println(userId + "  "+ resultMap.toString());
			return resultMap;	
		} catch (RowOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	public static void main(String args[])
	{
		/*
		 * Some testing
		 */
		double[] myIntArray = new double[]{1,2,3};		
		//System.out.println(Arrays.toString(getIndices(myIntArray)));
	}

}
