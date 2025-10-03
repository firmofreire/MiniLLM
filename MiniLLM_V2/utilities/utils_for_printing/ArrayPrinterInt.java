package MiniLLM_V2.utilities.utils_for_printing;

import MiniLLM_V2.debug.Debug;

public class ArrayPrinterInt
{
	public static void printArrayInt(String title, int[] array)
	{
		if(Debug.ArrayPrinterIntFlag)
		{
			System.out.println("\n"  + title  + ": "  + "\n");

        	for (int i = 0; i < array.length; i++)
        	{
				System.out.print(array[i]);

				if(i < array.length - 1)
					System.out.print(", ");
        	}
		}
	}
}


