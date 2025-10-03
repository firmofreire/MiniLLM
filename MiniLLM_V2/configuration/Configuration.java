package MiniLLM_V2.configuration;

import MiniLLM_V2.create.Model;

import MiniLLM_V2.layers.Embedding;

/***********************************************************************************************************************/

public class Configuration
{
	//
	// Reference to the Model and Embedding Classe are initiated here to facilitate other Classes to access
	// these Classes
	//
	public static Model 	model 					= null;			// Initiated by BuildLLMMenu

	public static Embedding embedding 				= null;			// Initiated by Model
	//
	// Defines the Hyperparameters, Parameters and Variable for the Mini LLM Model
	//
	// Plot Control
	//
	public static boolean	plotLossFlag				= true;			// If true, Plot the Loss Function

	public static boolean	embeddingPlotterFlag		= true;			// If true, Plot Embedding Vectors
	//
	// Random Number Generation Control
	//
	public static boolean 	sameRandomSequenceFlag 		= true;
	//
	// Optimizer Usage Flag
	//
	public static boolean 	optimizerFlag				= true;			// If true, use Optimizer
	//
	// Gradient Clipping Flag
	//
	public static boolean 	gradientClippingFlag		= true;			// If true, use Clipping

	public static boolean 	gradienClippingBeepFlag 	= false;			// If true, Beep when Gradient is Clipped
	//
	// Overfitting Flag
	//
	public static boolean	overfittingFlag				= true;

	public static boolean 	overfittingBeepFlag 		= false;			// If true, Beep when Gradient is Clipped
	//
	// Diagnostics Flag
	//
	public static boolean	diagnosticsFlag				= true;
	//
	// Hyperparameters
	//
	// Training Loop Control
	//
	public static 	int 	note				 		= 1;			// Number of Training Epochs

	public static	int		corpusVectorSize			= 0;			// Initiated by the Model Class

	public static 	int		contextWindowSize			= 8;			// Number of Tokens in the Input/Target Sequence

	public static 	int 	embeddingVectorSize 		= 5;			// Size of each Token Embedding Vector

	public static 	int 	hiddenLayerSize 			= 16;			// Number of Nodes in the Hidden Layer

	public static 	float	learningRate 				= 0.01f;		// Used for Hello World
//	public static 	float	learningRate 				= 0.001f;		// Used for Alice
	//
	// Number of Unique Tokens in the Corpus
	//
	// Note: This parameter is updated here after the Vocabulary has been created
	//
	public static	int		vocabSize					= 0;			// Number of Unique Tokens in the Corpus
	//
	// Array of File Path to Corpus Files
	//
	public static String[]	corpusPathArray =
	{
		" ",
		"C:/Users/Firmo/JavaPrograms/AIs/LargeLanguageModels/MiniLLM_V2/corpora/hello_world_corpus.txt",
		"C:/Users/Firmo/JavaPrograms/AIs/LargeLanguageModels/MiniLLM_V2/corpora/step_1_minimal_base_corpus.txt",
		"C:/Users/Firmo/JavaPrograms/AIs/LargeLanguageModels/MiniLLM_V2/corpora/step_2_expand_with_more_determiners_and_variants_corpus.txt",
		"C:/Users/Firmo/JavaPrograms/AIs/LargeLanguageModels/MiniLLM_V2/corpora/step_3_add_more_verbs_and_actions_corpus.txt",
		"C:/Users/Firmo/JavaPrograms/AIs/LargeLanguageModels/MiniLLM_V2/corpora/step_4_add_adjectives_for_finer_cluster_corpus.txt",
		"C:/Users/Firmo/JavaPrograms/AIs/LargeLanguageModels/MiniLLM_V2/corpora/step_5_add_prepositional_variety_corpus.txt",
		"C:/Users/Firmo/JavaPrograms/AIs/LargeLanguageModels/MiniLLM_V2/corpora/alice_short_corpus.txt"
	};
	//
	// File Path to Model Persistance Files
	//
	// 		- Embedding Layer Weigths and Biases
	//
	public static 	String	persistancePath = "C:/Users/Firmo/JavaPrograms/AIs/LargeLanguageModels/MiniLLM_V2/utilities/model_persistance/ModelPillars.txt";

/***********************************************************************************************************************/

}

