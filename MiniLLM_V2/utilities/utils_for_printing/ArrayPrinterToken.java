package MiniLLM_V2.utilities.utils_for_printing;

import MiniLLM_V2.debug.Debug;

import MiniLLM_V2.create.Tokenizer;

/******************************************************************************************************************/

public class ArrayPrinterToken
{
	//
	// Takes an Integer Array of Tokens and Prints:
	//
	//		- A Title
	//		- The Length of the Array
	//		- The Tokens in the Array
	//		- The Words Corresponding to the Tokens
	//

/******************************************************************************************************************/

	public static void printArrayToken(String title, int[] tokenArray)
    {
		if(Debug.ArrayPrinterTokenFlag)
		{
			System.out.println("\n\n"  + title  + "\n");
			//
			// Token IDs
			//
			System.out.print(" Token IDs : ");

			for (int i = 0; i < tokenArray.length; i++)
			{
				System.out.print(tokenArray[i]);

	    		if (i < tokenArray.length - 1) System.out.print(", ");
			}
			System.out.println();
			//
			// Decoded Tokens
			//
			System.out.print(" Tokens    : ");

			for (int i = 0; i < tokenArray.length; i++)
			{
	    		System.out.print(Tokenizer.decode(new int[] { tokenArray[i] }));

	   			 if (i < tokenArray.length - 1) System.out.print(", ");
			}

			System.out.println();
		}
	}

/******************************************************************************************************************/

}
