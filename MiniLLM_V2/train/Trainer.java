package MiniLLM_V2.train;

import javax.swing.*;

import java.util.*;

import MiniLLM_V2.configuration.Configuration;

import MiniLLM_V2.create.Model;

import MiniLLM_V2.create.Tokenizer;

import MiniLLM_V2.layers.Embedding;

import MiniLLM_V2.utilities.diagnostics.EmbeddingDiagnostics;

import MiniLLM_V2.utilities.plotters.LossPlotter;

import MiniLLM_V2.utilities.plotters.EmbeddingPlotter;

import MiniLLM_V2.utilities.operations.Argmax;

import MiniLLM_V2.utilities.utils_for_printing.ArrayPrinterToken;

import MiniLLM_V2.utilities.utils_for_printing.ArrayPrinterInt;

import MiniLLM_V2.utilities.utils_for_printing.ArrayPrinterFloat;

import MiniLLM_V2.utilities.utils_for_printing.ArrayPrinterTrainingSample;

import MiniLLM_V2.utilities.utils_for_printing.MatrixPrinterFloat;

/******************************************************************************************************************/

public class Trainer
{
	private	static	Model 				model;

	private	static	Embedding			embedding;

	public static	int[] 				input;

	public	static	int [] 				target;

	public static 	float[][] 			inputEmbeddings; 	// A Sequence of Tokens from the Corpus

	private			LossPlotter 		lossPlotter			= null;

	private			EmbeddingPlotter	embeddingPlotter 	= null;

	public			Map<String, String> categories;

	public			OverfittingMonitor 	overfittingMonitor;

/******************************************************************************************************************/

	public Trainer()
    {
		//
		// Instantiate the Loss Plotter
		//
		if(Configuration.plotLossFlag)
		{
        	SwingUtilities.invokeLater(() ->
			{
				try
	 			{
					lossPlotter = new LossPlotter();
				}
				catch(Exception e1)
				{
					e1.printStackTrace();

					System.out.println("Loss Plotter UI exception: e1 ="  + e1);
				}
			});
		}
		//
		// Instantiate Embedding Plotter
		//
		if(Configuration.embeddingPlotterFlag)
		{
			String[] labels = new String[Configuration.vocabSize];

        	for (int i = 0; i < Configuration.vocabSize; i++) labels[i] = "T" + i;

			SwingUtilities.invokeLater(() ->
			{
				try
	 			{
					embeddingPlotter = new EmbeddingPlotter(null, true);
				}
				catch(Exception e2)
				{
					e2.printStackTrace();

					System.out.println("EmbeddinPlotter UI exception: e2 ="  + e2);
				}
			});
		}
		//
        // Initialize Overfitting Monitor with Patience of 10 epochs and Minimum Improvement of 0.001
        //
        // int patience, double minDelta
        //
//			overfittingMonitor = new OverfittingMonitor(10, 0.001);
        	overfittingMonitor = new OverfittingMonitor(1, 0.0001);
	}

/******************************************************************************************************************/

	public void train()
	{
		//
		// Initiate References to the Model and Embedding Classes from the Configuration Class
		//
		model		= Configuration.model;

		embedding 	= Configuration.embedding;
		//
		// Begin Training Loop: A Forward Propagation and a Backward Propagation
		//
		// Train for the specified Number Of Epochs
		//
		for (int epoch = 0; epoch < Configuration.note; epoch++)
		{
			//
			// One Epoch is Defined as a Complete Traversal of the Corpus Vector,
			// given the Context Window Size
			//
			// Note: The Traversal will only cover the whole Corpus Vector if the
			//       Corpus Size is a Multiple of The Contex Window Plus One extra
			//       Element for the Target Token.
			//
			int maxTrainingSamples = Configuration.corpusVectorSize - Configuration.contextWindowSize + 1;

			for (int trainingSample = 0; trainingSample < maxTrainingSamples - 1; trainingSample++)
			{

System.out.println("\n\n"  + "/********************************************************************************************/");

System.out.println("\n\n"  + "IN:Trainer:train: epoch = "  + epoch);

//System.out.println("\n\n"  + "IN:Trainer:train: Input Sample:");

				//
				// Move Context Window to Select the Tokens for the current "input" and Embed the
				// Embedding Vector of each of the Tokens in the Window
				//
				String strInput = extractCorpusContextSample(model.corpusVector, trainingSample, (trainingSample + Configuration.contextWindowSize - 1));

				input = Tokenizer.encode(strInput);

				inputEmbeddings = embedding.getInputEmbeddings(input);

//System.out.println("\n\n"  + "IN:Trainer:train: Target Sample:");

				//
				// Move Context Window to Select the Tokens for the current "target"
				//
				String strTarget = extractCorpusContextSample(model.corpusVector, trainingSample + 1, trainingSample + Configuration.contextWindowSize);

				target = Tokenizer.encode(strTarget);


ArrayPrinterTrainingSample.printArrayTrainingSample(trainingSample, input, target);

				//
				// Forward Propagation
				//
				model.forward(inputEmbeddings);				// Do Forward Propagation
				//
				// Backward Propagation
				//
				model.backward(inputEmbeddings);			// Do Backward Propagation
			}

// END OF TRAINING SAMPLES LOOP  ***************************************************************************************************
			//
			// Print Loss
			//
			float loss = model.getAverageCrossEntropyLoss();
double currentLoss = (double) loss;

try{Thread.sleep(30);}									// Delay a litle to allow printing of epoch/loss to be seen
catch(Exception e99){}

			System.out.println("\n\n");
			String epochLoss = String.format("\rIN: Trainer: train: Epoch: %3d | Loss: %6.2f  ", epoch, loss);
			System.out.print(epochLoss);
			//
			// Plot Loss as a Function of Epoch
			//
			if(Configuration.plotLossFlag)
			{
				lossPlotter.waitUntilReady(); 						// Wait until GUI is shown

				lossPlotter.display((double)epoch, (double)loss);

				try
				{
//					Thread.sleep(10);
				}
				catch(Exception e3)
				{
				}
			}
			//
			// Plot Embeddings
			//
			if(Configuration.embeddingPlotterFlag)
			{
				//
				// Get the Embedding Vector Labels
				//
				String[] labels = new String[Configuration.vocabSize];

				int index = 0;

				Iterator<Map.Entry<String, Integer>> iterator = Tokenizer.vocab.entrySet().iterator();

				while (iterator.hasNext())
				{
					Map.Entry<String, Integer> entry = iterator.next();

					labels[index] = entry.getKey();

			    	index++;
				}
				//
				// Plot the Current Epoch Embedding Vectors
				//
				embeddingPlotter.waitUntilReady(); 						// Wait until GUI is shown

				embeddingPlotter.display(Embedding.embeddings, labels);

				try
				{
					Thread.sleep(10);
				}
				catch(Exception e4)
				{
				}
			}
			//
			// Monitor Overfitting
			//
			if(Configuration.overfittingFlag)
			{
				overfittingMonitor.monitorTraining(currentLoss);
			}
		}

// END OF EPOCHS LOOP  ***************************************************************************************************************

		//
		// Print the Predicted Output after the Training
		//
		float[][] logits = model.getLogits();

        int[] predictedOutput = new int[logits.length];

        for (int i = 0; i < logits.length; i++)
		{
            predictedOutput[i] = Argmax.argmax(logits[i]);
        }
		System.out.println("\n\n"  + "IN: Trainer: Predicted Output: "  + "\""  + Tokenizer.decode(predictedOutput)  + "\""  + "\n\n");
		//
		// Run Diagnostics for Diagnostics Corpora
		//
		if(Configuration.diagnosticsFlag)
		{
			EmbeddingDiagnostics ed = new EmbeddingDiagnostics();

			float[][] FE = embedding.embeddings;

			double[][] DE = new double[FE.length][FE[0].length];

			for(int i=0; i<FE.length; i++)
			{
				for(int j=0; j<FE[0].length; j++)
				{
					DE[i][j] = (double)FE[i][j];
				}
			}
			String[] strVocab = Tokenizer.getStringVocab();

			System.out.println("\n\n"  + "strVocab Length = "  + strVocab.length);

			for(int i=0; i<strVocab.length; i++)
			{
				System.out.println("\n\n"  + "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"  + strVocab[i]);
			}

				ed.runDiagnostics(DE, strVocab);
		}
	}

/*************************************************************************************************************************/

	public static String extractCorpusContextSample(String[] string, int fromIndex, int toIndex)
	{
		//
    	// Validate Indices
    	//

System.out.println("\n"  +  "IN:Training   Corpus Vector = "  + string.length  + "     fromIndex = "  + fromIndex  + "     toIndex = "  + toIndex);

String extraction = String.join(" ", Arrays.copyOfRange(string, fromIndex, toIndex+1));

    	if(fromIndex < 0 || fromIndex > toIndex || toIndex > string.length)
    	{
    	    throw new IndexOutOfBoundsException("Invalid Indices for String Extraction");
    	}
		//
    	// Join Selected Words With Spaces
    	//
		String extractionResult = String.join(" ", Arrays.copyOfRange(string, fromIndex, toIndex+1));

		return extractionResult;
	}

/*************************************************************************************************************************/

}