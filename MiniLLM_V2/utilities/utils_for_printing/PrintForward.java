package MiniLLM_V2.utilities.utils_for_printing;

import java.util.*;

import MiniLLM_V2.configuration.Configuration;

import MiniLLM_V2._exec_ui.BuildLLMMenu;

import MiniLLM_V2.create.Model;

import MiniLLM_V2.create.Tokenizer;

import MiniLLM_V2.layers.Embedding;

import MiniLLM_V2.train.Trainer;

import MiniLLM_V2.utilities.utils_for_printing.ArrayPrinterFloat;

//**************************************************************************************************************************************/

public class PrintForward
{

/**************************************************************************************************************************************/

	public static void printForward(String title)
	{
		Model model = Configuration.model;
		//
		// Print Title
		//
		System.out.println("\n\n"  + "********************************************************************************************************************");
		System.out.println("                                      "  + title);
		System.out.println(          "********************************************************************************************************************");
		//
		// Network Architecture
		//
		System.out.println("\n\n"  + "********************************************************************************************************************"  + "\n");
		System.out.println("hidden[contextWindowSize][hiddenLayerSize]["  + Configuration.contextWindowSize    + " x "  + Configuration.hiddenLayerSize      + "]"  + " -->  "
							+ "imputEmbedding["                           + Configuration.contextWindowSize    + " x "  + Configuration.embeddingVectorSize  + "]"  + " dot "
							+ "W1["                                       + Configuration.embeddingVectorSize  + " x "  + Configuration.hiddenLayerSize      + "]"  + " + "
							+ "b1["                                       + Configuration.hiddenLayerSize                                                    + "]"  + " Relu");

		System.out.println("\n"  + "********************************************************************************************************************" + "\n");
		System.out.println("logits[contextWindowSize][vocabSize]["  + Configuration.contextWindowSize + " x "  + Configuration.vocabSize        + "]"  + " -->  "
							+ "hidden["                             + Configuration.contextWindowSize + " x "  + Configuration.hiddenLayerSize  + "]"  + " dot "
							+ "W2["                                 + Configuration.hiddenLayerSize   + " x "  + Configuration.vocabSize        + "]"  + " + "
							+ "b2["                                 + Configuration.vocabSize                                                   + "]");
		System.out.println("\n"  + "********************************************************************************************************************");
		//
		// Hidden Matrix Computation
		//
		System.out.println("\n\n"  + "HIDDEN LAYER COMPUTATION*******************************************************************************************");
		//
		// Input to the Network
		//
		// Input Embeddings Computation
		//
		System.out.println("\n\n"  + "IMPUT EMBEDDING COMPUTATION*****************************************************************************************");


		ArrayPrinterToken.printArrayToken("Tokens in the Input Context Window", Trainer.input);


//		System.out.println("\n\n"  + "Tokens in the Input Context Window [contextWindowSize] ["  + Configuration.contextWindowSize  + "]");

//		ArrayPrinterInt.printArrayInt("input" , Trainer.input);

//		System.out.println("\n\n"  + "Tokens in the Target Context Window [contextWindowSize] ["  + Configuration.contextWindowSize  + "]");

//		ArrayPrinterInt.printArrayInt("target" , Trainer.target);

		MatrixPrinterFloat.printMatrixFloat("inputEmbeddings (Each Line is an Embedding Vector)", Trainer.inputEmbeddings, 4);
		//
		// Hidden Layer Weights and Biases
		//
		System.out.println("\n\n"  + "W1 [embeddingVectorSize x hiddenLayerSize] ["  + Configuration.embeddingVectorSize  + " x "  + Configuration.hiddenLayerSize  + "]");

		System.out.println("\n\n"  + "b1 [hiddenLayerSize] ["  + Configuration.hiddenLayerSize  + "]");

		MatrixPrinterFloat.printMatrixFloat("hidden [contextWindowSize][Configuration.hiddenLayerSize]", model.hidden, 4);

		System.out.println("\n\n"  + "********************************************************************************************************************"  + "\n");
		//
		// Logits Matrix Computation
		//
		System.out.println("\n\n"  + "LOGITS MATRIX COMPUTATION*******************************************************************************************");

		System.out.println("\n\n"  + "W2 [hiddenLayerSize x vocabSize] ["  + Configuration.hiddenLayerSize  + " x "  + Configuration.vocabSize  + "]");

		System.out.println("\n\n"  + "b2 [vocabSize] ["  + Configuration.vocabSize  + "]");

		MatrixPrinterFloat.printMatrixFloat("logits [contextWindowSize][vocabSize]", model.logits, 4);

		System.out.println("\n\n"  + "********************************************************************************************************************"  + "\n");
		//
		// Probs Matrix Computation
		//
		System.out.println("\n\n"  + "PROBS MATRIX COMPUTATION********************************************************************************************");

		MatrixPrinterFloat.printMatrixFloat("probsRaw [Configuration.contextWindowSize][Configuration.vocabSize]", model.probsRaw, 4);
		//
		// Print the Sum of the Softmax Probabilities Distribution in each Line of the "probs" Matrix
		//
		float sumProbsOnMatrixLines = 0f;

		System.out.println("\n\n");

		for(int i=0; i<Configuration.contextWindowSize; i++)
		{
			for(int j=0; j<Configuration.vocabSize; j++)
			{
				sumProbsOnMatrixLines += model.probsRaw[i][j];
			}

			System.out.println("Sum of Elements of Each Line in the Probabilities Matrix ["  + i  + " ] = "  + sumProbsOnMatrixLines);

			sumProbsOnMatrixLines = 0f;
		}
		//
		//	Print the Total and Average Cross Entropy Loss
		//
		System.out.println("\n\n"  + "Total Cross Entropy Loss in the Probabilities Matrix   = "  + model.totalCrossEntropyLoss);

		System.out.println("\n\n"  + "Average Cross Entropy Loss in the Probabilities Matrix = "  + model.averageCrossEntropyLoss);
		//
		//	Get Target Token and Token ID
		//
		System.out.println("\n\n"  + "GET TARGET TOKEN AND TOKEN ID***************************************************************************************");

		ArrayPrinterToken.printArrayToken("Tokens in the Target Context Window", Trainer.target);
		//
		// Compute Loss
		//
		System.out.println("\n\n"  + "COMPUTE LOSS********************************************************************************************************");

		System.out.println("\n\n"  + "Loss        : "  + model.averageCrossEntropyLoss);
		//
		// End of Forward Pass
		//
		System.out.println("\n\n"  + "********************************************************************************************************************");
		System.out.println("\n"  + "END OF FORWARD PATH*************************************************************************************************");

		System.out.println("\n"    + "********************************************************************************************************************");
	}

/**************************************************************************************************************************************/

}
