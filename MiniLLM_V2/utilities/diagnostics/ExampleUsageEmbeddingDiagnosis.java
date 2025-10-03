package MiniLLM_V2.utilities.diagnostics;

import java.util.*;

//***************************************************************************************************************************//

public class ExampleUsageEmbeddingDiagnosis
{

//***************************************************************************************************************************//

	public static void main(String[] args)
	{
		//
		//Example Usage
		//
		// Example vocabulary
		//
		String[] vocab = {"a","the","this","that","cat","dog","mat","yard","sat","ran","slept","chased","on","across","with"};
		//
		// Example dummy embeddings (replace with your model's embeddings!)
		//
		double[][] embeddings = new double[vocab.length][10]; 						// say 10-dim

		Random rand = new Random(0);

		for (int i = 0; i < vocab.length; i++)
		{
    		for (int j = 0; j < 10; j++)
    		{
        		embeddings[i][j] = rand.nextDouble();
    		}
		}
		//
		// Run diagnostics
		//
		EmbeddingDiagnostics ed = new EmbeddingDiagnostics();

		ed.runDiagnostics(embeddings, vocab);
	}

//***************************************************************************************************************************//

}
