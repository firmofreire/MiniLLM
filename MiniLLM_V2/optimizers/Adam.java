package MiniLLM_V2.optimizers;

import java.util.HashMap;
import java.util.Map;

import MiniLLM_V2.configuration.Configuration;

/********************************************************************************************************/

public class Adam
{
	//
	// Adam Optimizer that can handle both 2D Weight Matrices and 1D Bias Vectors
	//
	// Instantiation: FlexibleAdam adam = new FlexibleAdam(learningRate, beta1, beta2, epsilon);
	//
	// Usage:
	//
	//	1. Register Parameters: adam.registerParameter("weights", weightsMatrix);		// 2D Arrays
	//
	//	2. Register Biases    : adam.registerParameter("biases", biasesVector); 		// 1D Arrays
	//
	//	3. Update             : param[i][j] += adam.update("paramName", gradient, i, j);
	//
	// Optimizer Parameters
	//
    private float 	lr 	= 0.001f; 				// Learning Rate

    private float 	b1 	= 0.9f; 				// Beta1 for First Moment

    private float 	b2 	= 0.999f; 				// Beta2 for Second Moment

    private float 	eps = 1e-8f; 				// Epsilon for Numerical Stability

    private int 	t 	= 0; 					// Time Step Counter
	//
    // Storage for Moment Estimates
    //
    private Map<String, float[][]> 	moments 	= new HashMap<>();
    private Map<String, float[][]> 	velocities 	= new HashMap<>();
    private Map<String, Integer> 	dimensions 	= new HashMap<>();

/********************************************************************************************************/

    public Adam(float learningRate, float beta1, float beta2, float epsilon)
    {
        this.lr = learningRate;
        this.b1 = beta1;
        this.b2 = beta2;
        this.eps = epsilon;
    }

/********************************************************************************************************/

    public void registerParameter(String name, float[][] matrix)
    {
		//
		// Register a 2D Parameter Matrix for Optimization
		//
        int rows = matrix.length;

        int cols = matrix[0].length;

        moments.put(name, new float[rows][cols]);

        velocities.put(name, new float[rows][cols]);

        dimensions.put(name, 2); 										// Mark as 2D
    }

/********************************************************************************************************/

    public void registerParameter(String name, float[] vector)
    {
	//
	// Register a 1D Parameter Vector (like Biases) for Optimization
	// Stored internally as a 1xN matrix for consistency
	//
        moments.put(name, new float[1][vector.length]);

        velocities.put(name, new float[1][vector.length]);

        dimensions.put(name, 1); 										// Mark as 1D
    }

/********************************************************************************************************/

    public double update(String paramName, float grad, int i, int j)
    {
	//
	// Update a Parameter using Adam Optimization
	//
	// @param paramName Name of the registered parameter
	// @param grad Gradient Value
	// @param i Row Index (for 1D Parameters, always use 0)
	// @param j Column Index
	// @return The Update Amount to Apply to the Parameter
	//
        if (!moments.containsKey(paramName))
        {
            throw new IllegalArgumentException("Parameter '" + paramName + "' not registered");
        }
        t++;

        float[][] m = moments.get(paramName);

        float[][] v = velocities.get(paramName);
		//
        // Bounds Checking
        //
        if (i >= m.length || j >= m[0].length)
        {
            throw new IndexOutOfBoundsException(
                String.format("Index [%d][%d] out of bounds for parameter '%s'", i, j, paramName)
            );
        }
		//
        // Adam Update Equations
        //
        m[i][j] = b1 * m[i][j] + (1 - b1) * grad;

        v[i][j] = b2 * v[i][j] + (1 - b2) * grad * grad;
		//
        // Bias Correction
        //
        float mHat = m[i][j] / (float)(1 - Math.pow(b1, t));
        float vHat = v[i][j] / (float)(1 - Math.pow(b2, t));
		//
        // Return the Update Amount
        //
        return  -lr * mHat / (Math.sqrt(vHat) + eps);
    }

/********************************************************************************************************/

//    public float update(String paramName, float grad, int j)
//    {
//	//
//	// Convenience Method for Updating 1D Parameters (Biases)
//	//
//        return (float)update(paramName, grad, 0, j);
//    }

/********************************************************************************************************/

    public void reset()
    {
	//
	// Reset the Optimizer State (useful for new training runs)
	//
        t = 0;

        for (String key : moments.keySet())
        {
            float[][] m = moments.get(key);
            float[][] v = velocities.get(key);

            for (int i = 0; i < m.length; i++)
            {
                for (int j = 0; j < m[i].length; j++)
                {
                    m[i][j] = 0.0f;
                    v[i][j] = 0.0f;
                }
            }
        }
    }

/********************************************************************************************************/

	//
    // Getters and Setters
    //
    public float getLearningRate() { return lr; }
    public void setLearningRate(float learningRate) { this.lr = learningRate; }

    public float getBeta1() { return b1; }
    public void setBeta1(float beta1) { this.b1 = beta1; }

    public float getBeta2() { return b2; }
    public void setBeta2(float beta2) { this.b2 = beta2; }

    public float getEpsilon() { return eps; }
    public void setEpsilon(float epsilon) { this.eps = epsilon; }

    public int getTimestep() { return t; }

/********************************************************************************************************/

    public boolean isRegistered(String paramName)
    {
	//
	// Check if a Parameter is Registered
	//
        return moments.containsKey(paramName);
    }

/********************************************************************************************************/

}
