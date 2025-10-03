package MiniLLM_V2.layers;

import java.util.Random;

import MiniLLM_V2.configuration.Configuration;

import MiniLLM_V2.utilities.utils_for_printing.MatrixPrinterFloat;
import MiniLLM_V2.utilities.utils_for_printing.ArrayPrinterInt;

/**********************************************************************************************************************************/

public class Embedding
{
    public static float[][] embeddings;

/**********************************************************************************************************************************/

	public Embedding()
    {
  		//
  		// Set Seed for the generation of always the same Random Number Sequence
  		//
  		Long seed = 12346L;

  		Random random = new Random(seed);
  		//
  		// Initialize the embeddings Matrix with random numbers from: -0.5 to +0.5
  		//
  		// NOTE: This Matrix is used in Forward Propagation, but it is not Optimized in Backward Propagation.
  		// In this version of the Model (MiniLLM_V2) it is not changed after Initialization.
  		//
		embeddings = new float[Configuration.vocabSize][Configuration.embeddingVectorSize];

		for (int i = 0; i < Configuration.vocabSize; i++)
		{
			for (int j = 0; j < Configuration.embeddingVectorSize; j++)
			{
				if(!Configuration.sameRandomSequenceFlag)
    			{
     				embeddings[i][j] = (float) (Math.random() - 0.5); // Changes Random Number Sequence at every creation
    			}
    			else
    			{
                 	embeddings[i][j] = random.nextFloat() - 0.5f; // Ensures same Random Number Sequence as a function of a Seed
    			}
            }
        }
    }

/**********************************************************************************************************************************/

    public float[][] getInputEmbeddings(int[] imputTokenIndices)
    {
        float[][] inputEmbeddings = new float[imputTokenIndices.length][Configuration.embeddingVectorSize];

        for (int i = 0; i < imputTokenIndices.length; i++)
        {
            inputEmbeddings[i] = get(imputTokenIndices[i]);
        }

        return inputEmbeddings;
    }

/**********************************************************************************************************************************/

    public float[] get(int imputTokenIndices)
    {
        return embeddings[imputTokenIndices];
    }

/**********************************************************************************************************************************/

 public static void setE(float[][] newE)
 {
     embeddings = newE;
 }

/*********************************/

 public static float[][] getE() 				// E = embeddings
 {
     return embeddings;
 }

/**********************************************************************************************************************************/

}
