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

public class PrintCreation
{

/**************************************************************************************************************************************/

	public static void printCreation(String title)
	{
		//
		// Print Title
		//
		System.out.println("\n\n"  + "********************************************************************************************************************");
		System.out.println("                                          "  + title);
		System.out.println(          "********************************************************************************************************************");
		//
		// Hyperparameters
		//
		System.out.println("\n\n"  + "Hyperparameters"  +  "\n");

		System.out.println("\n"  + "note                       : " + Configuration.note                    + "		// Number of Training Epochs");
		System.out.println("\n"  + "Same Random Sequence Flag  : " + Configuration.sameRandomSequenceFlag  + "		// If true: Same Sequence");
		System.out.println("\n"  + "Corpus                     : "                                         + "		// Corpus Read from a File");
		System.out.println("\n"  + "Corpus Vector Size         : " + Configuration.corpusVectorSize        + "		// Corpus Size in Tokens");
		System.out.println("\n"  + "Context Window Size        : " + Configuration.contextWindowSize       + "		// Context Window Size");
		System.out.println("\n"  + "Embedding Vector Size      : " + Configuration.embeddingVectorSize     + "		// Size of each Token Embedding Vector");
		System.out.println("\n"  + "Hidden Layer Size          : " + Configuration.hiddenLayerSize         + "		// Number of Nodes in the Hidden Layer");
		System.out.println("\n"  + "Vocabulary Size            : " + Configuration.vocabSize               + "		// Number of Unique Tokens in the Corpus");
		System.out.println("\n"  + "Learning Rate              : " + Configuration.learningRate            + "		// Learning Rate");
		//
		// Vocabulary
		//
		// Note: The code to traverse the 'vocab' structure offers the possibility to use an
		//       'index', if needed.
		//
		System.out.println("\n\n"  + "vocab [vocabSize] ["  + Configuration.vocabSize  + "] :"  + "\n");

		int index = 0;

		Iterator<Map.Entry<String, Integer>> iterator = Tokenizer.vocab.entrySet().iterator();

		while (iterator.hasNext())
		{
		    Map.Entry<String, Integer> entry = iterator.next();
			    System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
			    // Use index here if needed
			    index++;
		}
		System.out.println("\n");
		//
		// Embeddings
		//
		MatrixPrinterFloat.printMatrixFloat("embeddings [vocabSize x embeddingVectorSize] (Each Line is an Embedding Vector)", Embedding.embeddings, 4);
		//
		// Weights and Biases
		//
		// W1
		//
//		MatrixPrinterFloat.printMatrixFloat("W1 [embeddingVectorSize x hiddenLayerSize] [5 x 16] (-0.5  :  +0.5) - Initialization", Model.W1, 4);
		System.out.println("\n\n"  + "W1 [embeddingVectorSize x hiddenLayerSize] ["  + Configuration.embeddingVectorSize  + " x "  + Configuration.hiddenLayerSize  + "]");
		//
		// b1
		//
//		ArrayPrinterFloat.printArrayFloat("b1 [hiddenSize] [16] (1) - Initialization", Model.b1);
		System.out.println("\n\n"  + "b1 [hiddenLayerSize] ["  + Configuration.hiddenLayerSize  + "]");
		//
		// W2
		//
//		MatrixPrinterFloat.printMatrixFloat("W2 [hiddenLayerSize x vocabSize] [16 x 10] (-0.5  :  +0.5) - Initialization", Model.W2, 4);
		System.out.println("\n\n"  + "W2 [hiddenLayerSize x vocabSize] ["  + Configuration.hiddenLayerSize  + " x "  + Configuration.vocabSize  + "]");

		//
		// b2
		//
//		ArrayPrinterFloat.printArrayFloat("b2 [vocabSize] [10] (1) - Initialization", Model.b2);
		System.out.println("\n\n"  + "b2 [vocabSize] ["  + Configuration.vocabSize  + "]");


		System.out.println("\n\n"  + "********************************************************************************************************************");
		System.out.println(          "********************************************************************************************************************");
	}

/**************************************************************************************************************************************/

}
