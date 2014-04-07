
import java.util.*;
public class ValueIndexPair implements Comparable<ValueIndexPair> {
	/*
	 * Utility class to keep track of an index and value of an element for sorting
	 */
	int index;
	double value;
	
	public ValueIndexPair(int index, double value)
	{
		/*
		 * Initialize constructor
		 */
		this.index = index;
		this.value = value;
	}
	
	public int compareTo(ValueIndexPair curTerm) {
		 
		double compareQuantity = curTerm.value; 
 
		//ascending order
		//return this.quantity - compareQuantity;
 
		//descending order
		if( (compareQuantity - this.value) > 0)
			return 1;
		
		else return -1;
 
	}
	
	

}
