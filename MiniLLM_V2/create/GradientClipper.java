package MiniLLM_V2.create;

import MiniLLM_V2.configuration.Configuration;

import MiniLLM_V2.debug.Debug;

import	MiniLLM_V2.utilities.beep_generator.Beep;

//***************************************************************************************//

public class GradientClipper
{
	private static double 	totalNorm;

	private static float 	scaleFactor;

//***************************************************************************************//

    public static void clipGradientsByNorm(float[][] dW1, float[] db1,
                                           float[][] dW2, float[] db2,
                                           float maxNorm)
	{
		//
        // 1. Calculate the Squared Sum of all Gradients
        //
        double totalSquaredSum = 0.0;
		//
        // Sum Squares for dW1
        //
        for (int i = 0; i < dW1.length; i++)
        {
            for (int j = 0; j < dW1[i].length; j++)
            {
                totalSquaredSum += dW1[i][j] * dW1[i][j];
            }
        }
		//
        // Sum Squares for db1
        //
        for (int i = 0; i < db1.length; i++)
        {
            totalSquaredSum += db1[i] * db1[i];
        }
		//
        // Sum Squares for dW2
        //
        for (int i = 0; i < dW2.length; i++)
        {
            for (int j = 0; j < dW2[i].length; j++)
            {
                totalSquaredSum += dW2[i][j] * dW2[i][j];
            }
        }
		//
        // Sum Squares for db2
        //
        for (int i = 0; i < db2.length; i++)
        {
            totalSquaredSum += db2[i] * db2[i];
        }
		//
        // 2. Calculate the Total Norm (L2 norm)
        //
        totalNorm = Math.sqrt(totalSquaredSum);
        //
        // 2.5 Calculate Scale Factor
        //
        scaleFactor = (float) (maxNorm / totalNorm);
		//
        // 3. Check if Clipping is Not Warranted
        //
        if (totalNorm > maxNorm)
        {
			//
			// 3.5 Beep if Clipping Occured
			//
			if (Configuration.gradienClippingBeepFlag)
			{
				Beep.playBeep(48000, 500, 0.2f);
			}
			//
            // 4. Apply Scaling to all Gradients
            //
            // Scale dW1
            //
            for (int i = 0; i < dW1.length; i++)
            {
                for (int j = 0; j < dW1[i].length; j++)
                {
                    dW1[i][j] *= scaleFactor;
                }
            }
			//
            // Scale db1
            //
            for (int i = 0; i < db1.length; i++)
            {
                db1[i] *= scaleFactor;
            }
			//
            // Scale dW2
            //
            for (int i = 0; i < dW2.length; i++)
            {
                for (int j = 0; j < dW2[i].length; j++)
                {
                    dW2[i][j] *= scaleFactor;
                }
            }
			//
            // Scale db2
            //
            for (int i = 0; i < db2.length; i++)
            {
                db2[i] *= scaleFactor;
            }
        }
        //
        // Print Debug Information
        //
        if(Debug.gradientDebugClippingFlag)
        {
			System.out.println("\n"  + "Max Norm: "  + maxNorm
									     	+ "     Total Norm: " + String.format("%.4f", totalNorm)
                            		     	+ "     Scale Factor: " + String.format("%.4f", scaleFactor));
		}
    }

//***************************************************************************************//

	//
    // Example usage
    //
    public static void main(String[] args)
    {
		//
        // Example Gradients (normally these come from Backpropagation)
        //
        float[][] dW1 = {{1.2f, -3.4f}, {5.6f, -7.8f}};
        float[] db1 = {9.0f, -10.1f};
        float[][] dW2 = {{11.2f, -12.3f}, {13.4f, -14.5f}};
        float[] db2 = {15.6f, -16.7f};

        float maxNorm = 10.0f; 						// Your Chosen Clipping Threshold
		//
        // Clip Gradients Before Weight Update
        //
        clipGradientsByNorm(dW1, db1, dW2, db2, maxNorm);


		System.out.println("\n\n"  + "Gradients clipped. Total Norm: " +
                                      String.format("%.4f", totalNorm) +
                                     ", Scaled by: " + String.format("%.4f", scaleFactor));

		System.out.println("\n\n");
		//
        // Now use the Clipped Gradients for Weight Updates:
        //
//			updateWeights(dW1, db1, dW2, db2, learningRate);

    }

//***************************************************************************************//

}
