package MiniLLM_V2.utilities.utils_for_printing;

import java.util.Locale;

import MiniLLM_V2.debug.Debug;

/*****************************************************************************************/

public class MatrixPrinterDouble
{

/*****************************************************************************************/

    public static void main(String[] args)
    {
		//
        // Example 5x4 matrix of floats
        //
        double[][] matrix =
        {
            {0.1234d, -0.4567d, 0.0d, -0.75d},
            {-0.9999d, 0.5000d, 0.001d, 0.12345d},
            {0.8d, -0.25d, 0.9876d, -0.1d},
            {1.0d, -1.0d, 0.0001d, -0.5555d},
            {0.0d, 0.25d, -0.3333d, 0.5d}
        };

        int decimalDigits = 4; 		// Configurable: 3, 4, etc. (max 4 for 7-char width)

        printMatrixDouble("Test Printing", matrix, decimalDigits);
    }

/*****************************************************************************************/

    public static void printMatrixDouble(String title, double[][] matrix, int decimalDigits)
    {
		if(Debug.MatrixPrinterDoubleFlag)
		{
			System.out.println("\n\n" + " "  + title  + "\n");

			System.out.println("Dimension: ("  + matrix.length  + " x "  + matrix[0].length  + ")"  + "\n");

        	for (int i = 0; i < matrix.length; i++)
        	{
        	    for (int j = 0; j < matrix[i].length; j++)
        	    {
        	        System.out.print(formatNumber(matrix[i][j], decimalDigits));

        	        if (j < matrix[i].length - 1)
                	{
                	    System.out.print(" "); 				// Single space between columns
                	}
            	}

            	System.out.println(); 						// Newline after each row
        	}
		}
    }

/*****************************************************************************************/

    private static String formatNumber(double num, int decimalDigits)
    {
		//
        // Determine Sign Character (space for positive, '-' for negative)
        //
        String sign = (num < 0) ? "-" : " ";
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