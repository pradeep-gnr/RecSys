import java.util.HashMap;
import java.util.LinkedHashMap;


public class SimilarUserMetrics {
	/*
	 * Utility class to return multiple values that is used in LazyKSimilarUsers fucntion.
	 */
	public LinkedHashMap<Integer,Vector> resultMap;
	public HashMap<Integer,Double> userSimilarityMap;
	
	public SimilarUserMetrics(LinkedHashMap<Integer,Vector> resultMap, HashMap<Integer,Double> userSimilarityMap)
	{
		this.resultMap =  resultMap;
		this.userSimilarityMap = userSimilarityMap;
	}
	
	

}
