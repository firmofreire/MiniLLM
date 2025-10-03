package MiniLLM_V2.utilities.model_persistance;

import java.io.*;
import java.util.*;


import MiniLLM_V2.create.Model;

import MiniLLM_V2.layers.Embedding;

/*****************************************************************************************************/

public class ModelPersistance
{

/*****************************************************************************************************/

    public static void saveModel(String filename)
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename)))
        {
            writer.println("W1");
            writeMatrix(writer, toDouble(Model.getW1()));

            writer.println("b1");
            writeArray(writer, toDouble(Model.getB1()));

            writer.println("W2");
            writeMatrix(writer, toDouble(Model.getW2()));

            writer.println("b2");
            writeArray(writer, toDouble(Model.getB2()));

			writer.println("E");										// Save persistances Matrix
			writeMatrix(writer, toDouble(Embedding.getE()));


        }
        catch (IOException e)
        {
            System.out.println("Error Saving Model: " + e.getMessage());
        }
    }

/*****************************************************************************************************/

    public static void restoreModel(String filename)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            Map<String, double[][]> matrices = new HashMap<>();
            Map<String, double[]> arrays = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null)
            {
                switch (line)
                {
                    case "W1":
                        matrices.put("W1", readMatrix(reader));
                        break;
                    case "b1":
                        arrays.put("b1", readArray(reader));
                        break;
                    case "W2":
                        matrices.put("W2", readMatrix(reader));
                        break;
                    case "b2":
                        arrays.put("b2", readArray(reader));
                        break;
					case "E":
						matrices.put("E", readMatrix(reader));
                }
            }

            Model.setW1(toFloat(matrices.get("W1")));
            Model.setB1(toFloat(arrays.get("b1")));
            Model.setW2(toFloat(matrices.get("W2")));
            Model.setB2(toFloat(arrays.get("b2")));

			Embedding.setE(toFloat(matrices.get("E")));					// Restore persistances Matrix
        }
        catch (IOException e)
        {
            System.out.println("Error Restoring Model: " + e.getMessage());
        }
    }

/*****************************************************************************************************/

    public static void compareModelToFile(String filename)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            Map<String, double[][]> matrices = new HashMap<>();
            Map<String, double[]> arrays = new HashMap<>();

            String line;

            while ((line = reader.readLine()) != null)
            {
                switch (line)

                {
                    case "W1":
                        matrices.put("W1", readMatrix(reader));
                        break;
                    case "b1":
                        arrays.put("b1", readArray(reader));
                        break;
                    case "W2":
                        matrices.put("W2", readMatrix(reader));
                        break;
                    case "b2":
                        arrays.put("b2", readArray(reader));
                        break;
                    case "E":
                        matrices.put("E", readMatrix(reader));
                        break;
                }
            }

            boolean match = true;

            if (!compareMatrices(matrices.get("W1"), Model.getW1(), "W1")) match = false;
            if (!compareArrays  (arrays.get(  "b1"), Model.getB1(), "b1")) match = false;
            if (!compareMatrices(matrices.get("W2"), Model.getW2(), "W2")) match = false;
            if (!compareArrays  (arrays.get(  "b2"), Model.getB2(), "b2")) match = false;

            if (!compareMatrices(matrices.get("E") , Embedding.getE(), "E")) match = false;


            if (match)
            {
                System.out.println("Model MATCHES Saved Weights and Biases");
            }
            else
            {
                System.out.println("Model does NOT MATCH Saved Weights and Biases");
            }
        }
        catch (IOException e)
        {
            System.out.println("Exception Comparing Structures: " + e);
        }
    }

/*****************************************************************************************************/

	public static void printSavedModel(String filename)
	{
		//
		// Print Saved Model Weights and Biases
		//
		try (BufferedReader reader = new BufferedReader(new FileReader(filename)))
		{
			System.out.println("---- Saved Model ----");

			String line;

			while ((line = reader.readLine()) != null)
			{
				System.out.println(line);
			}

			System.out.println("---------------------");
		}
		catch (IOException e)
		{
			System.out.println("Error Printing Saved Model: " + e.getMessage());
		}
	}

/*****************************************************************************************************/
	//
	// Helper Methods
	//
    private static void writeMatrix(PrintWriter writer, double[][] matrix) {
        for (double[] row : matrix) {
            for (int j = 0; j < row.length; j++) {
                writer.print(row[j]);
                if (j < row.length - 1) writer.print(",");
            }
            writer.println();
        }
    }

/**********************************************/

    private static void writeArray(PrintWriter writer, double[] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            writer.print(array[i]);

            if (i < array.length - 1) writer.print(",");
        }
        writer.println();
    }

/**********************************************/

    private static double[][] readMatrix(BufferedReader reader) throws IOException
    {
        List<double[]> rows = new ArrayList<>();

        reader.mark(1000);

        String line;

        while ((line = reader.readLine()) != null)
        {
            if (line.equals("W1") || line.equals("b1") || line.equals("W2") || line.equals("b2"))
            {
                reader.reset(); 					// Go back to the start of the line

                break;
            }
            reader.mark(1000); 						// Mark before next line just in case

            String[] parts = line.trim().split(",");

            double[] row = new double[parts.length];

            for (int i = 0; i < parts.length; i++)
            {
                row[i] = Double.parseDouble(parts[i]);
            }
            rows.add(row);
        }
        return rows.toArray(new double[0][]);
    }

/**********************************************/

    private static double[] readArray(BufferedReader reader) throws IOException
    {
        String line = reader.readLine();

        if (line == null) return new double[0];

        String[] parts = line.trim().split(",");

        double[] array = new double[parts.length];

        for (int i = 0; i < parts.length; i++)
        {
            array[i] = Double.parseDouble(parts[i]);
        }
        return array;
    }

/**********************************************/

    private static boolean compareMatrices(double[][] saved, float[][] current, String label)
    {
        if (saved == null || current == null)
        {
            System.out.println("Missing matrix: " + label);

            return false;
        }

        if (saved.length != current.length || saved[0].length != current[0].length)
        {
            System.out.println("Mismatch in matrix shape for " + label);

            return false;
        }

        boolean match = true;

        for (int i = 0; i < saved.length; i++)
        {
            for (int j = 0; j < saved[i].length; j++)
            {
                if (Math.abs(saved[i][j] - current[i][j]) > 1e-5)
                {
                    System.out.printf("Mismatch in %s[%d][%d]: saved=%.6f, current=%.6f%n", label, i, j, saved[i][j], current[i][j]);

                    match = false;
                }
            }
        }
        return match;
    }

/**********************************************/

    private static boolean compareArrays(double[] saved, float[] current, String label)
    {
        if (saved == null || current == null)
        {
            System.out.println("Missing array: " + label);

            return false;
        }
        if (saved.length != current.length)
        {
            System.out.println("Mismatch in array length for " + label);

            return false;
        }

        boolean match = true;

        for (int i = 0; i < saved.length; i++)
        {
            if (Math.abs(saved[i] - current[i]) > 1e-5) {
                System.out.printf("Mismatch in %s[%d]: saved=%.6f, current=%.6f%n", label, i, saved[i], current[i]);
                match = false;
            }
        }

        return match;
    }

/**********************************************/

    private static double[][] toDouble(float[][] matrix)
    {
        double[][] result = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                result[i][j] = matrix[i][j];

        return result;
    }

/**********************************************/

    private static double[] toDouble(float[] array)
    {
        double[] result = new double[array.length];

        for (int i = 0; i < array.length; i++)
            result[i] = array[i];

        return result;
    }

/**********************************************/

    private static float[][] toFloat(double[][] matrix)
    {
        float[][] result = new float[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                result[i][j] = (float) matrix[i][j];

        return result;
    }

/**********************************************/

    private static float[] toFloat(double[] array)
    {
        float[] result = new float[array.length];

        for (int i = 0; i < array.length; i++)
            result[i] = (float) array[i];

        return result;
    }

/**********************************************/

/*****************************************************************************************************/

}