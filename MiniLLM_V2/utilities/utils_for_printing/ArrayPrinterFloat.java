package MiniLLM_V2.utilities.utils_for_printing;

import java.util.Arrays;

import MiniLLM_V2.debug.Debug;

public class ArrayPrinterFloat
{
	public static void printArrayFloat(String title, float[] array)
	{
		if(Debug.ArrayPrinterFloatFlag)
		{
			System.out.println("\n"  + title  + ": ");

			String arrStr1 = Arrays.toString(array);

			System.out.println("\n"  +  arrStr1.substring(1, arrStr1.length() - 1));
		}
	}
}