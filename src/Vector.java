
public class Vector {
	/*
	 * A class to define a Vector 
	 */
	int dim; // Dimensions of the Vector
	double[] elements; //Elements of the vector.
	
	public Vector(double[] elements)
	{
		/*
		 * Initialize the Vector.	  
		 */
		dim = elements.length;
		this.elements = elements;		
	}

}
