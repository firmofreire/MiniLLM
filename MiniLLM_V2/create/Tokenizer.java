package MiniLLM_V2.create;

import java.io.*;

import java.util.*;

import MiniLLM_V2.configuration.Configuration;

/***********************************************************************************************************************************/

public class Tokenizer
{
	public 	static  Map<String, Integer> vocab 		= new HashMap<>();	// Hash Map of the unique tokens in the Corpus

    private static 	Map<Integer, String> idToToken 	= new HashMap<>();	// Hash Map relating id to Tokens in the vocab


/***********************************************************************************************************************************/

    public static void buildVocabulary(String corpus)
    {
		//
        // Only Build the Vocabulary if it's not already Loaded and Corpus is not Empty
        //
        if (vocab.isEmpty() && corpus != null && !corpus.isEmpty())
        {
        	vocab.clear();

        	idToToken.clear();

        	String[] tokens = corpus.split("\\s+");

        	Set<String> uniqueTokens = new LinkedHashSet<>(Arrays.asList(tokens));

        	int id = 0;

        	for (String token : uniqueTokens)
        	{
        	    vocab.put(token, id);

        	    idToToken.put(id, token);

        	    id++;
        	}
		}
		else
		{
			System.out.println("\n\n"  + "IN: Tokenizer: buildVocabulary: Condition not consistent for building the Vocabulary \n");
		}
    }

/***********************************************************************************************************************************/

    public static int[] encode(String input)
    {
        String[] tokens = input.split("\\s+");

        int[] tokenIds = new int[tokens.length];

        for (int i = 0; i < tokens.length; i++)
        {
            String token = tokens[i];

            if (!vocab.containsKey(token))
            {
                System.out.println("Warning: Token not found in vocab: '" + token + "'");
            }
            tokenIds[i] = vocab.getOrDefault(token, 0); // Default to 0 if unknown
        }

        return tokenIds;
    }

/***********************************************************************************************************************************/

    public static String decode(int[] tokenIds)
    {
        StringBuilder sb = new StringBuilder();

        for (int id : tokenIds)
        {
            String token = idToToken.getOrDefault(id, "<unk>");

            sb.append(token).append(" ");
        }

        return sb.toString().trim();
    }

/***********************************************************************************************************************************/

	public static int getVocabSize()
	{
        return vocab.size();
    }

//*************************************************************************************************************************//

	public static String[] getStringVocab()
	{

		int index = 0;

		Iterator<Map.Entry<String, Integer>> iterator = Tokenizer.vocab.entrySet().iterator();

String[] strVocab = new String[10];		////////////////////////////////////////////////////////////////////////////////////////////////

		while (iterator.hasNext())
		{
		    Map.Entry<String, Integer> entry = iterator.next();

			strVocab[index] = entry.getKey();

			index++;
		}

		return strVocab;
	}

/***********************************************************************************************************************************/

}