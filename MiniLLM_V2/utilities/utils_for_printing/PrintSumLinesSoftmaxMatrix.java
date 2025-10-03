package MiniLLM_V2.utilities.utils_for_printing;

import MiniLLM_V2.configuration.Configuration;

import MiniLLM_V2.create.Model;


public class PrintSumLinesSoftmaxMatrix
{
	public static void printSumLinesSoftmaxMatrix()
	{
		//
		// Print the Sum of the Softmax Probabilities Distribution in each Line of the "probs" Matrix
		//
		float sumProbsOnMatrixLines = 0f;

		System.out.println("\n\n");

		for(int i=0; i<Configuration.contextWindowSize; i++)
		{
			for(int j=0; j<Configuration.vocabSize; j++)
			{
				sumProbsOnMatrixLines += Model.probsSoft[i][j];
			}

			System.out.println("Sum of Elements of Each Line in the Softmax Probabilities Matrix ["  + i  + " ] = "  + sumProbsOnMatrixLines);

			sumProbsOnMatrixLines = 0f;
		}
	}
}