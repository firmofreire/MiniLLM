package MiniLLM_V2.utilities.utils_for_printing;

import MiniLLM_V2.configuration.Configuration;

import MiniLLM_V2.debug.Debug;

import MiniLLM_V2.create.Tokenizer;

import MiniLLM_V2.train.Trainer;

import MiniLLM_V2.utilities.operations.Argmax;

//****************************************************************************************************************************************//

public class PredictionPrinter
{

//****************************************************************************************************************************************//

	public static void printPrediction(int t, float[][] probs, float totalCrossEntropyLoss)
	{
		if(Debug.PredictionPrinterFlag)
		{

System.out.println("\n\n"  + "####################################################################################");

			String inputToken = GetTokenFromId.getStringFromInteger(Trainer.input[t], Tokenizer.vocab);  // Get the Token corresponding to input[t]

			String targetToken = GetTokenFromId.getStringFromInteger(Trainer.target[t], Tokenizer.vocab);  // Get the Token corresponding to target[t]

			int maxProbId = Argmax.argmax(probs[t]);

			String maxProbToken = GetTokenFromId.getStringFromInteger(maxProbId, Tokenizer.vocab);  // Get the Token corresponding to target[t]

			System.out.println("\n\n" + "Given: '" + inputToken  +"'"  + " --> "  +  "Prediction Should be: '"  + targetToken  +"'"  + "  "
		                          + "Prediction was: '"  + maxProbToken  +"'"  + "("  + maxProbId  + ")");

//********************************************************************************************

			for(int cols=0; cols < Configuration.vocabSize; cols++)
			{
				System.out.print(" "  + probs[t][cols]);
			}
			System.out.println("\n");


			System.out.println("IN:Model:forward: For Token: "  + inputToken    + ":   probs["  + t  + "]["  + Trainer.target[t]  + "] = "  + probs[t][Trainer.target[t]]);

			System.out.println("IN:Model:forward: For Token: "  + inputToken    + ":   totalCrossEntropyLoss = "  + totalCrossEntropyLoss);
		}
	}

//****************************************************************************************************************************************//

}
