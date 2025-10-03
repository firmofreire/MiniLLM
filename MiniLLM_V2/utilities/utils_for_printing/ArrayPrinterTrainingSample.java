package MiniLLM_V2.utilities.utils_for_printing;

import MiniLLM_V2.debug.Debug;
import MiniLLM_V2.create.Tokenizer;

/******************************************************************************************************************/

public class ArrayPrinterTrainingSample
{

/******************************************************************************************************************/

	public static void printArrayTrainingSample(int trainingSample, int[] tokenArrayInput, int[] tokenArrayTarget)
    {
		if(Debug.ArrayPrinterTrainingSampleFlag)
		{
			System.out.println("\n\n"  + "Training Sample: "  + trainingSample + "\n");
			//
			// Input Sample
			//
			System.out.println("   Input Sample: "  + "\n");
			//
			// Token IDs
			//
			System.out.print("      Token IDs : ");

			for (int i = 0; i < tokenArrayInput.length; i++)
			{
				System.out.print(tokenArrayInput[i]);

	    		if (i < tokenArrayInput.length - 1) System.out.print(", ");
			}
			System.out.println();
			//
			// Tokens
			//
			System.out.print("      Tokens    : ");

			for (int i = 0; i < tokenArrayInput.length; i++)
			{
	    		System.out.print(Tokenizer.decode(new int[] { tokenArrayInput[i] }));

	   			 if (i < tokenArrayInput.length - 1) System.out.print(", ");
			}

			System.out.println();
			//
			// Target Sample
			//
			System.out.println("\n\n"  + "   Target Sample: "  + "\n");
			//
			// Token IDs
			//
			System.out.print("      Token IDs : ");

			for (int i = 0; i < tokenArrayTarget.length; i++)
			{
				System.out.print(tokenArrayTarget[i]);

	    		if (i < tokenArrayTarget.length - 1) System.out.print(", ");
			}
			System.out.println();
			//
			// Tokens
			//
			System.out.print("      Tokens    : ");

			for (int i = 0; i < tokenArrayTarget.length; i++)
			{
	    		System.out.print(Tokenizer.decode(new int[] { tokenArrayTarget[i] }));

	   			 if (i < tokenArrayTarget.length - 1) System.out.print(", ");
			}

			System.out.println();
		}
	}

/******************************************************************************************************************/

}
