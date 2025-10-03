package MiniLLM_V2.utilities.plotters;

public class PCA2D
{
	//
	// This gives you a PCA2D.reduce(float[][] data) method that works with your
	// Embedding Plotters.
	// It’s simple power-iteration-based PCA, so no external libraries are needed.
	//
    public static double[][] reduce(float[][] data)
    {
        int n = data.length; // number of samples (tokens)
        int d = data[0].length; // embedding dimension

        // 1. Compute mean for each dimension
        double[] mean = new double[d];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < d; j++)
            {
                mean[j] += data[i][j];
            }
        }
        for (int j = 0; j < d; j++)
        {
            mean[j] /= n;
        }

        // 2. Center data
        double[][] centered = new double[n][d];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < d; j++)
            {
                centered[i][j] = data[i][j] - mean[j];
            }
        }

        // 3. Compute covariance matrix (d x d)
        double[][] cov = new double[d][d];
        for (int i = 0; i < d; i++)
        {
            for (int j = i; j < d; j++)
            {
                double sum = 0;
                for (int k = 0; k < n; k++)
                {
                    sum += centered[k][i] * centered[k][j];
                }
                cov[i][j] = sum / (n - 1);
                cov[j][i] = cov[i][j];
            }
        }

        // 4. Power iteration to get top 2 eigenvectors
        double[][] components = new double[2][d];
        components[0] = powerIteration(cov, 100);
        components[1] = deflate(cov, components[0], 100);

        // 5. Project data to 2D
        double[][] reduced = new double[n][2];
        for (int i = 0; i < n; i++)
        {
            for (int c = 0; c < 2; c++)
            {
                double dot = 0;
                for (int j = 0; j < d; j++)
                {
                    dot += centered[i][j] * components[c][j];
                }
                reduced[i][c] = dot;
            }
        }
        return reduced;
    }

    // Power iteration to approximate dominant eigenvector
    private static double[] powerIteration(double[][] matrix, int iterations)
    {
        int d = matrix.length;
        double[] vec = new double[d];
        for (int i = 0; i < d; i++) vec[i] = Math.random() - 0.5;

        for (int it = 0; it < iterations; it++)
        {
            double[] newVec = new double[d];
            for (int i = 0; i < d; i++)
            {
                for (int j = 0; j < d; j++)
                {
                    newVec[i] += matrix[i][j] * vec[j];
                }
            }
            normalize(newVec);
            vec = newVec;
        }
        return vec;
    }

    // Deflation method to get next eigenvector
    private static double[] deflate(double[][] matrix, double[] eigenvector, int iterations)
    {
        int d = matrix.length;
        double[][] deflated = new double[d][d];
        double lambda = eigenvalue(matrix, eigenvector);

        for (int i = 0; i < d; i++)
        {
            for (int j = 0; j < d; j++)
            {
                deflated[i][j] = matrix[i][j] - lambda * eigenvector[i] * eigenvector[j];
            }
        }
        return powerIteration(deflated, iterations);
    }

    private static double eigenvalue(double[][] matrix, double[] vec)
    {
        double[] Av = new double[vec.length];
        for (int i = 0; i < vec.length; i++)
        {
            for (int j = 0; j < vec.length; j++)
            {
                Av[i] += matrix[i][j] * vec[j];
            }
        }
        double num = dot(Av, vec);
        double den = dot(vec, vec);
        return num / den;
    }

    private static void normalize(double[] vec)
    {
        double norm = Math.sqrt(dot(vec, vec));
        if (norm > 1e-10)
        {
            for (int i = 0; i < vec.length; i++)
            {
                vec[i] /= norm;
            }
        }
    }

    private static double dot(double[] a, double[] b)
    {
        double sum = 0;
        for (int i = 0; i < a.length; i++) sum += a[i] * b[i];
        return sum;
    }
}
