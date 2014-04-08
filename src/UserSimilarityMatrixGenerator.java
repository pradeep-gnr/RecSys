import java.lang.Math;

import Jama.Matrix;

public class UserSimilarityMatrixGenerator {
	
	/*
	 * Computes similarity matrix for all users based on their profiles.
	 */
	public Matrix userSimMatrix;
	public Matrix userItemMatrix;
	public int dim;
	
	public UserSimilarityMatrixGenerator(Matrix userItemMatrix)
	{
		/*
		 * Initialize the dimension of the User Similarity matrix 
		 */
		this.dim = userItemMatrix.getRowDimension();
		userSimMatrix = new Matrix(dim,dim); // Initialize the similarity matrix
		this.userItemMatrix = userItemMatrix;
		
	}
	
	public void generateUserSimilarityMatrix(String simType)
	{
		/*
		 * Computes the similarity score for each user pair
		 * @simType - the type of similarity metric to be used 'dot' for dot product and 'cos' for cosine similarity.		 *  
		 */
		Vector userI;
		Vector userJ;
		for(int i=0;i<this.dim;i++)
			for(int j=i;j<this.dim;j++)
				
			{
				/*
				 *  Compute similarity sim(u(i), u(j))
				 */
				
				System.out.println(i+"  "+j);
				try {
					userI = MatrixHelper.fetchIthRow(userItemMatrix,i);
				} catch (RowOutOfBoundsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
					
				} 
				try {
					userJ = MatrixHelper.fetchIthRow(userItemMatrix,j);
				} catch (RowOutOfBoundsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
					
				}
				if(simType.equals("dot"))
						{
							try {
								double simScore = SimilarityComputer.getDotProduct(userI, userJ);
								userSimMatrix.set(i, j,simScore);								
								userSimMatrix.set(j,i,simScore);
							} catch (DimensionMismatchException e) {
								// TODO Auto-generated catch block
								
								e.printStackTrace();
								return;
							}
						}
				else
					{
					try {						
						double simScore = SimilarityComputer.getCosineSimilarity(userI, userJ);
						userSimMatrix.set(i, j,simScore);
						userSimMatrix.set(j, i,simScore);
						//System.out.println(SimilarityComputer.getCosineSimilarity(userI, userJ));
						} catch (DimensionMismatchException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
						return;
					}
				}
			}
	}
	
	
	
	public static void main(String args[])
	{
		/*
		 * Test Similarity computation Speed
		 */
		TrainingFileReader r = new TrainingFileReader("/home/pradeep/courses/IR/HW4/HW3_data/train.csv");
		r.processTrainingFile();		
		System.out.println("Built a Matrix");
		double[][] array = {{1,2,3},{-1,-2,-3},{8,11,13}};
		Matrix A = new Matrix(array);
		A = MatrixHelper.getSubtractedMatrix(A, 3);
		//UserSimilarityMatrixGenerator test = new UserSimilarityMatrixGenerator(r.userItemMatrix);			
		UserSimilarityMatrixGenerator test = new UserSimilarityMatrixGenerator(A);
		test.generateUserSimilarityMatrix("cos");
		
		MatrixHelper.printMatrix(A);
		MatrixHelper.printMatrix(test.userSimMatrix);
		//MatrixHelper.writeMatrixToFile(test.userSimMatrix, "/home/pradeep/matrix.ser");
		System.out.println("Written to File");
		
	}
	 
	
	
	
	 
	
	

}

