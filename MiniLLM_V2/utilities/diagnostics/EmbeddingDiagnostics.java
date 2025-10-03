package MiniLLM_V2.utilities.diagnostics;

import java.util.*;

//*************************************************************************************************************************//

public class EmbeddingDiagnostics
{
	//
	// Categories for the Disgnostics Corpus
	//
	private static Map<String, String> categories = new HashMap<>();		// Category Map

//*************************************************************************************************************************//

    public static void runDiagnostics(double[][] embeddings, String[] vocab)
    {
		//
		// Example Category Map
		//
		categories.put("a"     ,"det");
		categories.put("the"   ,"det");
		categories.put("this"  ,"det");
		categories.put("that"  ,"det");
		categories.put("cat"   ,"noun");
		categories.put("dog"   ,"noun");
		categories.put("mat"   ,"noun");
		categories.put("yard"  ,"noun");
		categories.put("sat"   ,"verb");
		categories.put("ran"   ,"verb");
		categories.put("slept" ,"verb");
		categories.put("chased","verb");
		categories.put("on"    ,"prep");
		categories.put("across","prep");
		categories.put("with"  ,"prep");
		//
		// Demo Runner
		//
        printNearestNeighbors(embeddings, vocab, 3);
        printClusterCohesion(embeddings, vocab, categories);
        printClusterSeparation(embeddings, vocab, categories);

        System.out.println("\n\n");
    }

//*************************************************************************************************************************//

    public static void printNearestNeighbors(double[][] embeddings, String[] vocab, int topK)
    {
		//
		// Print Nearest Neighbors for Each Word
		//
        System.out.println("\n\n"  + "=== Nearest Neighbors ==="  + "\n");

        for (int i = 0; i < vocab.length; i++)
        {
            String word = vocab[i];
            double[] vec = embeddings[i];
            PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>(
                    Comparator.comparingDouble(Map.Entry::getValue)
            );
            for (int j = 0; j < vocab.length; j++)
            {
                if (i == j) continue;
                double sim = cosine(vec, embeddings[j]);
                pq.offer(new AbstractMap.SimpleEntry<>(vocab[j], sim));
                if (pq.size() > topK) pq.poll();
            }
            List<Map.Entry<String, Double>> neighbors = new ArrayList<>(pq);

            neighbors.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

            System.out.print(word + " → ");

            for (Map.Entry<String, Double> e : neighbors)
            {
                System.out.printf("%s (%.3f) ", e.getKey(), e.getValue());
            }
            System.out.println();
        }
    }

//*************************************************************************************************************************//

    public static void printClusterCohesion(double[][] embeddings, String[] vocab, Map<String, String> categories)
    {
		//
		// Compute Cluster Cohesion: Average Similarity Within Category
		//
        System.out.println("\n\n"  + "\n=== Cluster Cohesion ==="  + "\n");

        Map<String, List<Integer>> catIndices = new HashMap<>();

        for (int i = 0; i < vocab.length; i++)
        {
            catIndices.computeIfAbsent(categories.get(vocab[i]), k -> new ArrayList<>()).add(i);
        }
        for (String cat : catIndices.keySet())
        {
            List<Integer> idxs = catIndices.get(cat);
            if (idxs.size() < 2) continue;
            double total = 0.0;
            int count = 0;
            for (int i = 0; i < idxs.size(); i++)
            {
                for (int j = i + 1; j < idxs.size(); j++)
                {
                    total += cosine(embeddings[idxs.get(i)], embeddings[idxs.get(j)]);
                    count++;
                }
            }
            double avg = (count > 0) ? total / count : 0.0;
            System.out.printf("%s → %.3f\n", cat, avg);
        }
    }

//*************************************************************************************************************************//

    public static void printClusterSeparation(double[][] embeddings, String[] vocab, Map<String, String> categories)
    {
		//
		// Compute Cluster Separation: Distance Between Category Centroids
		//
        System.out.println("\n\n"  + "\n=== Cluster Separation ==="  + "\n");

        Map<String, double[]> centroids = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();

        for (int i = 0; i < vocab.length; i++)
        {
            String cat = categories.get(vocab[i]);
            centroids.putIfAbsent(cat, new double[embeddings[i].length]);
            counts.put(cat, counts.getOrDefault(cat, 0) + 1);
            double[] c = centroids.get(cat);
            for (int d = 0; d < embeddings[i].length; d++)
            {
                c[d] += embeddings[i][d];
            }
        }

        for (String cat : centroids.keySet())
        {
            double[] c = centroids.get(cat);
            int n = counts.get(cat);
            for (int d = 0; d < c.length; d++)
            {
                c[d] /= n;
            }
        }

        List<String> cats = new ArrayList<>(centroids.keySet());
        for (int i = 0; i < cats.size(); i++)
        {
            for (int j = i + 1; j < cats.size(); j++)
            {
                double sim = cosine(centroids.get(cats.get(i)), centroids.get(cats.get(j)));
                System.out.printf("%s vs %s → %.3f\n", cats.get(i), cats.get(j), sim);
            }
        }
    }

//*************************************************************************************************************************//

    private static double cosine(double[] v1, double[] v2)
    {
		//
		// Compute Cosine Similarity between two Vectors
		//
        double dot = 0.0, norm1 = 0.0, norm2 = 0.0;

        for (int i = 0; i < v1.length; i++)
        {
            dot   += v1[i] * v2[i];
            norm1 += v1[i] * v1[i];
            norm2 += v2[i] * v2[i];
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2) + 1e-8);
    }

//*************************************************************************************************************************//

}