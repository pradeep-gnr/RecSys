import java.math.*;
public class VectorOperations {
	/*
	 * Class methods for various vector operations.
	 */
	
	static double getVectorMean(Vector vector)
	{
		/*
		 * Compute vector mean
		 */
		double totalSum = 0;
		
		for(int i=0;i<vector.elements.length;i++)
		{
			totalSum+=vector.elements[i];			
		}		
		return totalSum/vector.dim;
	}
	
	static double getVectorVariance(Vector vector)
	{
		/*
		 * Compute vector variance
		 */
		double totalSum = 0;
		int l=0;
		double mean =  getVectorMean(vector); 
		for(int i=0;i<vector.elements.length;i++)
		{
			double element = vector.elements[i];
			
			double diff  = element-mean;			
			totalSum+=(diff*diff);
			//l+=1;
		}		
		return Math.pow( totalSum,0.5);		
	}	
	
	static Vector standardizeVector(Vector vector)
	{
		/*
		 * Standardizes a vector
		 */
		double mean = getVectorMean(vector);
		//System.out.println(mean);
		
		double variance = getVectorVariance(vector);
		if(variance==0)
			return vector;
		
		//System.out.println(variance);
		
		/*
		 * Change the vector
		 */
		
		for(int i=0;i<vector.elements.length;i++)
		{
			double element = vector.elements[i];
			element = (element - mean) / variance;
			vector.elements[i] = element;			
		}
		return vector;
	}
	
	
	static Vector reStandardizeVector(double mean, double variance, Vector vector)
	{
		/*
		 * Restandardize a vector into original format.
		 */
		
		for(int i=0;i<vector.elements.length; i++)
		{
			/*
			 * Re standardize 
			 */
			double element  = vector.elements[i];
			vector.elements[i] = (element*variance)+mean;			 
		}
		return vector;
	}
	
	public static void main(String args[])
	{
		/*
		 * 
		 */
		double[] array = new double[]{1,2,3};		
		
		Vector v = new Vector(array);
		double mean = getVectorMean(v);		
		double variance = getVectorVariance(v);
		System.out.println("STUFF  " +mean+"  "+variance);
		
		double[] a = standardizeVector(v).elements;
		
		for(int i=0;i<a.length;i++)
		{
			System.out.println(a[i]);
		}
		System.out.println(a);
		
		Vector newVect = new Vector(a);
		
		Vector reStandard = reStandardizeVector(mean, variance , newVect );
		
		a = reStandard.elements;
		
		for(int i=0;i<a.length;i++)
		{
			System.out.println(a[i]);
		}
		System.out.println(a);
		
	}

}
