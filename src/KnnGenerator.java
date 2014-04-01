import java.util.HashMap;
import Jama.Matrix;
public class KnnGenerator {
	/*
	 * Class methods to compute the K-nearest neighbors 
	 */
	
	private Matrix dataMatrix; // Data Matrix where each row is a vector.
	private int K; // Internal parameter for number of nearest neighbors to fetch.
	
	public KnnGenerator(Matrix matrix, int k)
	{
		/*
		 * Initialize the Class
		 */
		dataMatrix = matrix;
		K=k;
	}
	
	public void computeSimilarity()
	{
		/*
		 * Computes similarity between two vectors
		 */
	}
	
	public void getTopKVectors()
	{
		/*
		 * 
		 */
		
	}
	

}
