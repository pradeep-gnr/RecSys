import java.lang.Math;

import Jama.Matrix;

public class ItemSimilarityMatrixGenerator {
	
	/*
	 * Computes similarity matrix for all users based on their profiles.
	 */
	public Matrix itemSimMatrix;
	public Matrix itemUserMatrix;
	public int dim;
	
	public ItemSimilarityMatrixGenerator(Matrix itemUserMatrix)
	{
		/*
		 * Initialize the dimension of the User Similarity matrix 
		 */
		this.dim = itemUserMatrix.getRowDimension();
		itemSimMatrix = new Matrix(dim,dim); // Initialize the similarity matrix
		this.itemUserMatrix = itemUserMatrix;
		
	}
	
	public void generateItemSimilarityMatrix(String simType)
	{
		/*
		 * Computes the similarity score for each user pair
		 * @simType - the type of similarity metric to be used 'dot' for dot product and 'cos' for cosine similarity.		 *  
		 */
		Vector itemI;
		Vector itemJ;
		for(int i=0;i<this.dim;i++)
			for(int j=i;j<this.dim;j++)
				
			{
				/*
				 *  Compute similarity sim(u(i), u(j))
				 */
				
				System.out.println(i+"  "+j);
				try {
					itemI = MatrixHelper.fetchIthRow(itemUserMatrix,i);
				} catch (RowOutOfBoundsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
					
				} 
				try {
					itemJ = MatrixHelper.fetchIthRow(itemUserMatrix,j);
				} catch (RowOutOfBoundsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
					
				}
				if(simType.equals("dot"))
						{
							try {
								double simScore = SimilarityComputer.getDotProduct(itemI, itemJ);
								itemSimMatrix.set(i, j,simScore);								
								itemSimMatrix.set(j,i,simScore);
							} catch (DimensionMismatchException e) {
								// TODO Auto-generated catch block
								
								e.printStackTrace();
								return;
							}
						}
				else
					{
					try {						
						double simScore = SimilarityComputer.getCosineSimilarity(itemI, itemJ);
						itemSimMatrix.set(i, j,simScore);
						itemSimMatrix.set(j, i,simScore);
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
		//UserSimilarityMatrixGenerator test = new UserSimilarityMatrixGenerator(r.itemUserMatrix);			
		ItemSimilarityMatrixGenerator test = new ItemSimilarityMatrixGenerator(A);
	//	test.generateUserSimilarityMatrix("cos");
		
		MatrixHelper.printMatrix(A);
		MatrixHelper.printMatrix(test.itemUserMatrix);
		//MatrixHelper.writeMatrixToFile(test.itemUserMatrix, "/home/pradeep/matrix.ser");
		System.out.println("Written to File");
		
	}
	 
	
	
	
	 
	
	

}

