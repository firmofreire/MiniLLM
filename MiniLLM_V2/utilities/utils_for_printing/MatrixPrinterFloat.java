package MiniLLM_V2.utilities.utils_for_printing;

import java.util.Locale;

import MiniLLM_V2.debug.Debug;
/*****************************************************************************************/

public class MatrixPrinterFloat
{

/*****************************************************************************************/

    public static void main(String[] args)
    {
		//
        // Example 5x4 matrix of floats
        //
        float[][] matrix =
        {
            {0.1234f, -0.4567f, 0.0f, -0.75f},
            {-0.9999f, 0.5000f, 0.001f, 0.12345f},
            {0.8f, -0.25f, 0.9876f, -0.1f},
            {1.0f, -1.0f, 0.0001f, -0.5555f},
            {0.0f, 0.25f, -0.3333f, 0.5f}
        };

        int decimalDigits = 4; 		// Configurable: 3, 4, etc. (max 4 for 7-char width)

        printMatrixFloat("Test Printing", matrix, decimalDigits);
    }

/*****************************************************************************************/

    public static void printMatrixFloat(String title, float[][] matrix, int decimalDigits)
    {
		if(Debug.MatrixPrinterFloatFlag)
		{
			System.out.println("\n\n" + title  + "\n");

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

    private static String formatNumber(float num, int decimalDigits)
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