package MiniLLM_V2.predict;

import java.util.Scanner;

import java.util.*;

import MiniLLM_V2.configuration.Configuration;

import MiniLLM_V2.create.Model;

import MiniLLM_V2.create.Tokenizer;

import MiniLLM_V2.layers.Embedding;

import MiniLLM_V2.utilities.operations.Argmax;

import MiniLLM_V2.utilities.utils_for_printing.MatrixPrinterFloat;

 /********************************************************************************************************************/

 public class Query
 {
	 private String 	query;

	 private Model 		model;

	 private Embedding	embedding;

 /********************************************************************************************************************/

	public void readQuery()
	{

 System.out.println("\n\n"  + "**************************************************************************************");
 System.out.println(          "**************************************PLAY********************************************");
 System.out.println(          "**************************************************************************************");

	//
	// Initiate References to the Model and Embedding Classes from the Configuration Class
	//
	model 		= Configuration.model;

	embedding 	= Configuration.embedding;
	//
	// Create a Scanner to Read the Query (input) from the Console
	//
		Scanner scanner = new Scanner(System.in);

        System.out.println("\n\n"  + "Enter Query (type 'exit' to quit):");
	//
	// Read Line from the Console (line is terminated by entering ENTER)
	//
        while (true)
        {
            System.out.print("\n"  + "Query> ");				// Prompt for the the Queryt

            query = scanner.nextLine();							// Read the entire next line of input as a String

            if (query.equalsIgnoreCase("exit"))					// If input equals "exit", break
            {
				break;
			}

            predictNextToken();									// If not "exit", predict next Token
		}

        scanner.close();
	}

 /********************************************************************************************************************/

	public void predictNextToken()
    {
        int[] queryTokens = Tokenizer.encode(query);

        if (queryTokens.length == 0)
        {
            System.out.println("No valid tokens found.");

            return;
        }

        float[][] 	queryEmbeddings 	= embedding.getInputEmbeddings(queryTokens);

		Configuration.contextWindowSize = queryEmbeddings.length;

		model.forward(queryEmbeddings);

		float[][] logits = model.getLogits();

        float[] 	lastLogits 			= logits[logits.length - 1]; 				// Last Position

        int 		predictedTokenId 	= Argmax.argmax(lastLogits); 				// Or use sampling if you prefer

        String 		predictedToken 		= Tokenizer.decode(new int[]{predictedTokenId});

        System.out.println("\n\n"  + "IN: Query: predictNextToken: Query               : \"" + query + "\"");

        System.out.println(          "IN: Query: predictNextToken: Predicted Next Token: \"" + predictedToken +
                                                        "          Predicted Token Id: "  + predictedTokenId  + "\"");

System.out.println("\n\n"  + "***************************************************************************************");
System.out.println(          "***********************************END Play********************************************");
System.out.println(          "***************************************************************************************");

    }

/**************************************************************************************************/

}
