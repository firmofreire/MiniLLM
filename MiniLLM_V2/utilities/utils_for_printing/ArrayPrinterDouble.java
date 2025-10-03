package MiniLLM_V2.utilities.utils_for_printing;

import java.util.Locale;

import MiniLLM_V2.debug.Debug;

/*****************************************************************************************/

public class ArrayPrinterDouble
{

/*****************************************************************************************/

    public static void main(String[] args)
    {
		//
        // Example: Array of floats
        //
        double[] array = {0.1234, -0.4567, 0.0, -0.75,-0.9999, 0.5000, 0.001, 0.12345};

        int decimalDigits = 4; 		// Configurable: 3, 4, etc. (max 4 for 7-char width)

        printArrayDouble("Test Printing", array, decimalDigits);
    }

/*****************************************************************************************/

    public static void printArrayDouble(String title, double[] array, int decimalDigits)
    {
		if(Debug.ArrayPrinterDoubleFlag)
		{
			System.out.println("\n\n" + " "  + title  + "\n");

			System.out.println("Dimension: ("  + array.length  + ")"  + "\n");

        	for (int i = 0; i < array.length; i++)
        	{
				System.out.print(formatNumber(array[i], decimalDigits));
            }
            System.out.println("\n");
		}
    }

/*****************************************************************************************/

    private static String formatNumber(double num, int decimalDigits)
    {
		//
        // Determine Sign Character (space for positive, '-' for negative)
        //
        String sign = (num < 0) ? " -" : " ";
        //
        // Format Absolute Value to specified Decimal Digits
        //
        String absStr = String.format(Locale.US, "%." + decimalDigits + "f", Math.abs(num));
		//
        // Split into Integer and Fractional Parts
        //
        String[] parts = absStr.split("\\.");

        String intPart = parts[0];
        //
        // Use Full Fractional Part (padded with zeros during formatting)
        //
        String fracPart = (parts.length > 1) ? parts[1] : "";
        //
        // Combine Sign, Integer, and Fractional Parts
        //
        String numStr = sign + intPart + "." + fracPart;
        //
        // Pad to exactly 7 Characters (right-aligned with leading spaces)
        //
        return String.format("%7s", numStr);
    }

/*****************************************************************************************/

}