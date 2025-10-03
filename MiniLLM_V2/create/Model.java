package MiniLLM_V2.create;

import java.nio.file.*;

import java.util.*;

import java.util.Arrays;

import MiniLLM_V2.configuration.Configuration;

import MiniLLM_V2.debug.Debug;

import MiniLLM_V2._exec_ui.BuildLLMMenu;

import MiniLLM_V2.layers.Embedding;

import MiniLLM_V2.train.Trainer;

import MiniLLM_V2.utilities.activation_functions.ActivationFunctions;

import MiniLLM_V2.utilities.utils_for_printing.ArrayPrinterInt;

import MiniLLM_V2.utilities.utils_for_printing.ArrayPrinterFloat;

import MiniLLM_V2.utilities.utils_for_printing.MatrixPrinterFloat;

import MiniLLM_V2.utilities.utils_for_printing.PrintCreation;

import MiniLLM_V2.utilities.utils_for_printing.PrintForward;

import MiniLLM_V2.utilities.utils_for_printing.GetTokenFromId;

import MiniLLM_V2.utilities.utils_for_printing.GetIdFromToken;

import MiniLLM_V2.utilities.utils_for_printing.PredictionPrinter;

import MiniLLM_V2.utilities.utils_for_printing.PrintSumLinesSoftmaxMatrix;

import MiniLLM_V2.utilities.operations.Argmax;

import MiniLLM_V2.optimizers.Adam;

/**********************************************************************************************************************/

public class Model
{
 	//
 	// This Class takes a sequence of Input Tokens and Predicts the Next Token
 	//
	private static	Random 		random;

 	public static	String		corpus;

	public static	String[] 	corpusVector;				// Vector where each element is a Token of the Corpus

	public static	Embedding 	embedding;

    public 	static	float[][] 	W1;							// Hidden Layer Weights

    public	static 	float[] 	b1;							// Hidden Layer Biases

    public static	float[][] 	hidden;

    public	static 	float[][] 	W2;							// Output Layer Weights

    public	static 	float[] 	b2;							// Output Layer Biases

	public static	float[][] 	logits;

	public static	float[][] 	probsRaw;

	public static	float[][] 	probsSoft;

	public static	float 		totalCrossEntropyLoss;

    public static	float 		averageCrossEntropyLoss;

    public static	Adam		adam;

/**********************************************************************************************************************/

    public void createLLM()
    {
		//
		// Set Seed for the generation of the same Random Number Sequence, always
		//
		Long seed = 123588L;

		random = new Random(seed);
		//
		// To allow the Model to be Created without the user previously having chosen a Corpus,
		// a test is made to see if the Corpus has been read (selectedCorpusIndex > 0). If not,
		// a call is made for the Default Corpus (hello_worldCorpus.txt) to be read.
		//
		int selectedCorpusIndex = BuildLLMMenu.getSelectedCorpusIndex();

		if(selectedCorpusIndex == 0)
		{
			readCorpus(1);				// Read the Default Corpus (selectedCorpusIndex = 1)
		}
		else
		{
			readCorpus(selectedCorpusIndex);
		}
		//
		// Determine the number of Tokens in the Corpus
		//
		int corpusTokensSize = corpus.split("\\s").length;

		Configuration.corpusVectorSize = corpusTokensSize;		// Initialize the Corpus Token Size in the Configuration Class
//
//	Create a Vector where each Element is a Token from the Corpus
//
//  Note: This Vector of Tokens is used by the extractCorpusContextSample Method in the Trainer
//
corpusVector = corpus.split(" ");

System.out.println("\n\n"  + "Corpus Vector Length= "  + corpusVector.length);
System.out.println("\n\n"  + "corpusVector:\n");
for(int i=0; i<corpusVector.length; i++)
{
	System.out.println("i = "  + i  +  " "  + corpusVector[i]);
}
System.out.println("\n\n");

		//
		// Build and Tokenize the Vocabulary from the Corpus
		//
		// Note: Vocabulary is the the collection of unique Tokens in the Corpus
		//
		Tokenizer.buildVocabulary(corpus);
		//
		// Set variables vocabSize and embedding in the Configuration Class
		//
		Configuration.vocabSize = Tokenizer.getVocabSize();		// Number of Unique Tokens in the Corpus

		embedding 	= new Embedding();							// Creates the embedding Matrix

		Configuration.embedding = embedding;					// Initialize the Embedding reference in the Configuration Class
		//
		//	Create and Initialize the Weights Matrix W1 and W2, and the Bias Array b1 and b2
		//
		// Create: W1, b1, W2, and b2
		//
        this.W1 = new float[Configuration.embeddingVectorSize][Configuration.hiddenLayerSize];

        this.b1 = new float[Configuration.hiddenLayerSize];

        this.W2 = new float[Configuration.hiddenLayerSize][Configuration.vocabSize];

        this.b2 = new float[Configuration.vocabSize];
		//
		// Initialize W1 and W2 with Random Numbers between -0.5 (inclusive) and 0.5 (exclusive)
		//
		// Initialize W1
		//
        for (int i = 0; i < Configuration.embeddingVectorSize; i++)
		{
            for (int j = 0; j < Configuration.hiddenLayerSize; j++)
            {
				if(!Configuration.sameRandomSequenceFlag)
				{
                	W1[i][j] = (float) (Math.random() - 0.5);			// Changes Random Number Sequence at every creation
				}
				else
				{
                	W1[i][j] = random.nextFloat() - 0.5f;				// Ensures same Random Number Sequence as a function of a Seed
				}
			}
		}
		//
		// b1 Initialization (with 0s)
		//
		// Initialize W2
		//
        for (int i = 0; i < Configuration.hiddenLayerSize; i++)
        {
            for (int j = 0; j < Configuration.vocabSize; j++)
            {
				if(!Configuration.sameRandomSequenceFlag)
				{
					W2[i][j] = (float) (Math.random() - 0.5);			// Changes Random Number Sequence at every creation
				}
				else
				{
                	W2[i][j] = random.nextFloat() - 0.5f;				// Ensures same Random Number Sequence as a function of a Seed
				}
			}
		}
		//
		// b2 Initialization (with 0s)
		//

if(Debug.ModelOneCycleTraceFlag)
{
MatrixPrinterFloat.printMatrixFloat("W1 [embeddingVectorSize][hiddenLayerSize]", W1, 4);
ArrayPrinterFloat.printArrayFloat("b1 [embeddingVectorSize][hiddenLayerSize]", b1);
MatrixPrinterFloat.printMatrixFloat("W2 [embeddingVectorSize][hiddenLayerSize]", W2, 4);
ArrayPrinterFloat.printArrayFloat("b2 [embeddingVectorSize][hiddenLayerSize]", b2);
}

	//
	// Create Optimizer
	//
    float learningRate 	= 0.001f; 				// Learning Rate

    float beta1 		= 0.9f; 				// Beta1 for First Moment

    float beta2 		= 0.999f; 				// Beta2 for Second Moment

    float epsilon 		= 1e-8f; 				// Epsilon for Numerical Stability

	adam 				= new Adam(learningRate, beta1, beta2, epsilon);
	//
	// Register Optimezer Weights Matrices and Biases Vectors
	//
	adam.registerParameter("W1", W1);

	adam.registerParameter("b1", b1);

	adam.registerParameter("W2", W2);

	adam.registerParameter("b2", b2);

	adam.registerParameter("E", embedding.getE());


//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//if(Configuration.ModelCreationSectionFlag == true) PrintCreation.printCreation("PARAMETERS AFTER CREATE");
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

	}

/**********************************************************************************************************************/

	public static void readCorpus(int selectedCorpusIndex)
	{
		String corpusPath = Configuration.corpusPathArray[selectedCorpusIndex];

		corpus = " ";

		try
		{
			corpus = Files.readString(Path.of(corpusPath)).toLowerCase();
		}
		catch(Exception e0)
		{
			System.out.println("\n\n"  + "IN: ModelCreation: createLLM(): e0: "  + e0);
		}
	}

/**********************************************************************************************************************/

	public void forward(float[][] inputEmbeddings)
    {
		//
		// This Method Traverses the Network Computing Predicted Token and the Loss
		// between the Predicted and the Expected Token (target)
		//
		// Create the "hidden" Matrix
		//
		hidden = new float[Configuration.contextWindowSize][Configuration.hiddenLayerSize];

        for (int t = 0; t < Configuration.contextWindowSize; t++)
        {
			float sum = 0f;

            for (int j = 0; j < Configuration.hiddenLayerSize; j++)
            {
                for (int i = 0; i < Configuration.embeddingVectorSize; i++)
                {
                    sum += inputEmbeddings[t][i] * W1[i][j];
                }

				sum += b1[j];

                hidden[t][j] = ActivationFunctions.relu(sum);
            }
        }

if(Debug.ModelOneCycleTraceFlag)
{
MatrixPrinterFloat.printMatrixFloat("hidden [contextWindowSize][hiddenLayerSize]", hidden, 4);
}

        //
		// Compute the "logits" Matrix
		//
		// Note: The "logits" Matrix is the output Prediction (in Unnormalized Scores) for
		//       each possible Token in the Vocabulary. It must then be Normalized, i. e.,
		//       Converted to a Softmax Distribution.
		//
        logits = new float[Configuration.contextWindowSize][Configuration.vocabSize];

        for (int t = 0; t < Configuration.contextWindowSize; t++)
        {
			float sum = 0f;

            for (int j = 0; j < Configuration.vocabSize; j++)
            {
                for (int i = 0; i < Configuration.hiddenLayerSize; i++)
                {
                    sum += hidden[t][i] * W2[i][j];
                }

				sum += b2[j];

                logits[t][j] = sum;
            }
        }

if(Debug.ModelOneCycleTraceFlag)
{
MatrixPrinterFloat.printMatrixFloat("logits [contextWindowSize][hiddenLayerSize]", logits, 4);
}

        //
		// Compute the "probsSoft" Matrix
		//
		// Notes: (1) The "probsSoft" Matrix is the output Prediction (in Softmax Distribution)
		//            for each possible Token in the Vocabulary.
		//
		//        (2) The Cross Entropy Loss and Softmax Distribution are derived using
		//            the Log_Sum_Exp Trick.
		//
		probsRaw  = new float[Configuration.contextWindowSize][Configuration.vocabSize];			// Create the Raw Probabilities Matrix (probsRaw)

		probsSoft = new float[Configuration.contextWindowSize][Configuration.vocabSize];			// Create the Softmax Probabilities Matrix (probsSoft)

        totalCrossEntropyLoss = 0f;

if(Debug.ModelOneCycleTraceFlag)
{
System.out.println("\n\n");
}

        for (int t = 0; t < Configuration.contextWindowSize; t++)
        {
			//
			// Compute the Maximum Value in the Current Line (t) of the "logits" Matrix
			//
			float maxLogit = Float.NEGATIVE_INFINITY;

			for(int j=0; j < Configuration.vocabSize; j++)
			{
				if(logits[t][j] > maxLogit) maxLogit = logits[t][j];
			}
			//
			// For each Line (t) in the "logits" Matrix derive a corresponding line in
			// the "probsRaw" Matrix.
			//
			// Note: After this computation, each line of the 'probsRaw' Matrix corresponds
			//       to a Token in the Vocabulary ('vocab').
			//
            float sumProbsRaw = 0f;

            for (int i = 0; i < Configuration.vocabSize; i++)
            {
                probsRaw[t][i] = (float) Math.exp(logits[t][i] - maxLogit);

                sumProbsRaw += probsRaw[t][i];
            }

if(Debug.ModelOneCycleTraceFlag)
{
System.out.println("\n"  +"maxLogit for logits line["  + t  + "] = "  + maxLogit  + "      sumProbsRaw for logits line["  + t  + "] = "  + sumProbsRaw);
}

			//
			// Average the "probsRaw" Matrix
			//
			// Note: (A) After the Averaging, each Line of the "probsRaw" Matrix the "probsSoft" Matrix
			//           is the Softmax distribution of a Token in the Vocabulary ('vocab').
			//
			//       (B) Each entry in this new Matrix means that probsSoft[t][i] is the probability
			//           of the Token relative to vocab[t] is the Token in input[i].
			//
            for (int i = 0; i < Configuration.vocabSize; i++)
            {
				probsSoft[t][i] = probsRaw[t][i] / sumProbsRaw;
            }
			//
			// Compute the Prediction and the Total Cross Entropy Loss for all Tokens in the Training Sample
			//
			totalCrossEntropyLoss -= Math.log(probsSoft[t][Trainer.target[t]] + 1e-8f);

if(Debug.ModelOneCycleTraceFlag)
{
System.out.println("\n"  + "totalCrossEntropyLoss -= Math.log(probsSoft[t][Trainer.target[t]] + 1e-8f) for probsSoft line["  + t  + "] = "  + totalCrossEntropyLoss);
}

        }

if(Debug.ModelOneCycleTraceFlag)
{
MatrixPrinterFloat.printMatrixFloat("probsRaw: probsRaw[t][i] = (exp(logits[t][i] - maxLogit)) [contextWindowSize][vocabSize]", probsRaw, 4);

MatrixPrinterFloat.printMatrixFloat("probsSoft: probsSoft[t][i] = probsRaw[t][i] / sumProbsRaw [contextWindowSize][vocabSize]", probsSoft, 4);

PrintSumLinesSoftmaxMatrix.printSumLinesSoftmaxMatrix();
}

		//
		//	Compute the Average Loss
		//
        averageCrossEntropyLoss = totalCrossEntropyLoss / Configuration.contextWindowSize;

if(Debug.ModelOneCycleTraceFlag)
{
System.out.println("\n"  + "contextWindowSize = "  + Configuration.contextWindowSize);

System.out.println("\n"  + "averageCrossEntropyLoss = totalCrossEntropyLoss / Configuration.contextWindowSize = "  +  averageCrossEntropyLoss);
}

		//
		// Print the Predicted Token
		//
		int[] predictedOutput = new int[logits.length];

		for (int i = 0; i < logits.length; i++)
		{
			predictedOutput[i] = Argmax.argmax(probsSoft[i]);
		}

if(Debug.ModelOneCycleTraceFlag)
{
ArrayPrinterInt.printArrayInt("predictedOutput [][]", predictedOutput);

System.out.println("\n\n"  + "Predicted Output: "  + "\""  + Tokenizer.decode(predictedOutput)  + "\""  + "\n\n");
}

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
if(Debug.ModelForwardSectionFlag == true)PrintForward.printForward("STRUCTURES AFTER FORWARD PROPAGATION");
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    }

/**********************************************************************************************************************/

	public void backward(float[][] inputEmbeddings)
    {
		//
		//
		//
        float[][] dOutput = new float[Configuration.contextWindowSize][Configuration.vocabSize];

        for (int t = 0; t < Configuration.contextWindowSize; t++)
        {
            for (int i = 0; i < Configuration.vocabSize; i++)
            {
				dOutput[t][i] = probsSoft[t][i];
            }
            dOutput[t][Trainer.target[t]] -= 1.0f;

if(Debug.ModelOneCycleTraceFlag)
{
System.out.println("\n\n"  +  "Trainer.target[t] = "  + Trainer.target[t]);
System.out.println("\n\n"  +  "dOutput["  + t  + "]["  + Trainer.target[t]  + "] = "  + dOutput[t][Trainer.target[t]]);
}

        }

if(Debug.ModelOneCycleTraceFlag)
{
MatrixPrinterFloat.printMatrixFloat("dOutput", dOutput, 4);
}

		//
		// Compute Derivatives for the Weights and Biases: W2 and b2
		//
        float[][] dHidden = new float[Configuration.contextWindowSize][Configuration.hiddenLayerSize];

        float[][] dW2 = new float[Configuration.hiddenLayerSize][Configuration.vocabSize];

        float[] db2 = new float[Configuration.vocabSize];

        for (int t = 0; t < Configuration.contextWindowSize; t++)
        {
            for (int i = 0; i < Configuration.hiddenLayerSize; i++)
            {
                for (int j = 0; j < Configuration.vocabSize; j++)
                {
                    dW2[i][j] += hidden[t][i] * dOutput[t][j];
                }
            }

            for (int j = 0; j < Configuration.vocabSize; j++)
            {
                db2[j] += dOutput[t][j];
            }
        }

        for (int t = 0; t < Configuration.contextWindowSize; t++)
        {
            for (int i = 0; i < Configuration.hiddenLayerSize; i++)
            {
                float grad = 0f;

                for (int j = 0; j < Configuration.vocabSize; j++)
                {
                    grad += dOutput[t][j] * W2[i][j];
                }

                dHidden[t][i] = grad * ActivationFunctions.reluDerivative(hidden[t][i]);
            }
        }
		//
		// Compute Derivatives for the Weights and Biases: W1 and b1
		//
        float[][] dW1 = new float[Configuration.embeddingVectorSize][Configuration.hiddenLayerSize];

        float[] db1 = new float[Configuration.hiddenLayerSize];

        for (int t = 0; t < Configuration.contextWindowSize; t++)
        {
            for (int i = 0; i < Configuration.embeddingVectorSize; i++)
            {
                for (int j = 0; j < Configuration.hiddenLayerSize; j++)
                {
                    dW1[i][j] += inputEmbeddings[t][i] * dHidden[t][j];
                }
            }
            for (int j = 0; j < Configuration.hiddenLayerSize; j++)
            {
                db1[j] += dHidden[t][j];
            }
        }

//
// Compute Gradient Clipping
//
if(Configuration.gradientClippingFlag) {GradientClipper.clipGradientsByNorm(dW1, db1,dW2, db2, 5.0f);}


		//
		// Optimize W1 and b1
		//
        for (int i = 0; i < Configuration.embeddingVectorSize; i++)
        {
            for (int j = 0; j < Configuration.hiddenLayerSize; j++)
            {
				if(Configuration.optimizerFlag)
				{
					W1[i][j] += adam.update("W1", dW1[i][j], i, j);
				}
				else
				{
                	W1[i][j] -= Configuration.learningRate * dW1[i][j];
				}
            }
        }

        for (int j = 0; j < Configuration.hiddenLayerSize; j++)
        {
			if(Configuration.optimizerFlag)
			{
				b1[j] += adam.update("b1", db1[j], 0, j);
			}
			else
			{
				b1[j] -= Configuration.learningRate * db1[j];
			}
        }
		//
		// Optimize W2 and b2
		//
        for (int i = 0; i < Configuration.hiddenLayerSize; i++)
        {
            for (int j = 0; j < Configuration.vocabSize; j++)
            {
				if(Configuration.optimizerFlag)
				{
					W2[i][j] += adam.update("W2", dW2[i][j], i, j);
				}
				else
				{
                	W2[i][j] -= Configuration.learningRate * dW2[i][j];
            	}
			}
        }

        for (int j = 0; j < Configuration.vocabSize; j++)
        {
			if(Configuration.optimizerFlag)
			{
				b2[j] += adam.update("b2", db2[j], 0, j);
			}
			else
			{
            	b2[j] -= Configuration.learningRate * db2[j];
			}
        }

//
// Optimize Embedding Matrix
//
 float[][] dE = new float[Configuration.vocabSize][Configuration.embeddingVectorSize];

        for (int t = 0; t < Configuration.contextWindowSize; t++)
        {
            int tokenId = Trainer.input[t]; 				// Assumes Trainer.input[] holds token ids

            for (int i = 0; i < Configuration.embeddingVectorSize; i++)
            {
                float gradE = 0f;

                for (int j = 0; j < Configuration.hiddenLayerSize; j++)
                {
                    gradE += dHidden[t][j] * W1[i][j];
                }

                dE[tokenId][i] += gradE;
            }
        }

//        float[][] Emb = Configuration.embedding.getEmbeddingMatrix();
        float[][] Emb = embedding.getE();

        for (int token = 0; token < Configuration.vocabSize; token++)
        {
            for (int i = 0; i < Configuration.embeddingVectorSize; i++)
            {
                if (dE[token][i] == 0f) continue; 			// skip if no gradient

                if (Configuration.optimizerFlag)
                {
                    Emb[token][i] += adam.update("E", dE[token][i], token, i);
                }
                else
                {
                    Emb[token][i] -= Configuration.learningRate * dE[token][i];
                }
            }
        }

    }

/**********************************************************************************************************************************/

	public float getAverageCrossEntropyLoss()
	{
		return averageCrossEntropyLoss;
	}

/**********************************************************************************************************************/

	public float[][] getLogits()
	{
		return logits;
	}

/**********************************************************************************************************************/

	//
	// Acessors to the Embedding Weights Matrix and Bias Array for the ModelPersistance Class
	//
	public static float[][] getW1()
	{
    return W1;
	}

/******************************************/

	public static void setW1(float[][] newW1)
	{
	    W1 = newW1;
	}

/******************************************/

	public static float[] getB1()
	{
	    return b1;
	}

/******************************************/

	public static void setB1(float[] newb1)
	{
	    b1 = newb1;
	}

/******************************************/

	//
	// Hidden Layer Weights and Biases
	//
	public static float[][] getW2()
	{
	    return W2;
	}

/******************************************/

	public static void setW2(float[][] newW2)
	{
	    W2 = newW2;
	}

/******************************************/

	public static float[] getB2()
	{
    	return b2;
	}

/******************************************/

	public static void setB2(float[] newb2)
	{
		b2 = newb2;
	}

/**********************************************************************************************************************************/

}