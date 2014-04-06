
import Jama.Matrix;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
public class MatrixHelper {
	/*
	 * Helper functions for matrix utilities 
	 */
	
	static Vector fetchIthRow(Matrix matrix, int rowId) throws RowOutOfBoundsException
	{
		/*
		 * Fetch the ith Row in a matrix
		 * @matrix 
		 * @rowId - the ith row to fetch
		 * @return - returns a row in a vector format
		 */
		int maxRowLen = matrix.getRowDimension();
		int maxColLen = matrix.getColumnDimension();
		double[] rowElements = new double[maxColLen];
		if(rowId>maxRowLen-1)
		{
			throw new RowOutOfBoundsException("Row id exceeds total number of rows in the matrix");
		}
		
		else
		{
			for(int j=0;j<maxColLen;j++)
			{
				rowElements[j] = matrix.get(rowId,j);
			}
		}
		
		return new Vector(rowElements);
	}
	
	static void printMatrix(Matrix matrix) 
	{
		/*
		 * Utility function to print a matrix.
		 */
		for(int i=0;i<matrix.getRowDimension();i++)
		{
			for(int j=0;j<matrix.getColumnDimension();j++)
			{
				System.out.print(matrix.get(i, j)+ " ");
			}
			System.out.println("");
		}
	}
	
	static Matrix getSubtractedMatrix(Matrix matrix, int num)
	{
		/*
		 * Subtracts 'num' from each entry in the matrix
		 * @return - modified matrix
		 */
		for(int i=0;i<matrix.getRowDimension();i++)
		{
			for(int j=0;j<matrix.getColumnDimension();j++)
			{
				double curValue = matrix.get(i,j);
				matrix.set(i, j, curValue-num);
				//System.out.print(matrix.get(i, j)+ " ");
			}
			//System.out.println("");
		}
		return matrix;
	}
	
	static void writeMatrixToFile(Matrix matrix, String path)
	{
		/*
		 * Writes a matrix as an object to a File
		 * @path - the path of the matrix outpout
		 */
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(path);
			try {
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(matrix);
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
	}

}
