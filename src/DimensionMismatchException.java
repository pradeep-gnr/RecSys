
class DimensionMismatchException extends Exception
{
      //Parameterless Constructor
      public DimensionMismatchException() {}

      //Constructor that accepts a message
      public DimensionMismatchException(String message)
      {
         super(message);
      }
 }