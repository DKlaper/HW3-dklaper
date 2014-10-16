package edu.cmu.lti.f14.hw3.hw3_dklaper.casconsumers.similarity;

import java.util.HashMap;
import java.util.HashSet;

public class CosineMeasure implements SimilarityMeasure {

	@Override
	public double computeSimilarity(HashMap<String, Integer> queryVector,
			HashMap<String, Integer> docVector) {
		double cosine_similarity = 0.0;
		double euc_norm = eucNorm(queryVector.values())
				* eucNorm(docVector.values());
		// make a copy to avoid changing the original keyset :P
		HashSet<String> wordsInBoth = new HashSet<String>(queryVector.keySet());
		// only need to match those that appear in both.
		wordsInBoth.retainAll(docVector.keySet());
		for (String matchKey : wordsInBoth) // scalar multiplication of vectors
		{
			cosine_similarity += queryVector.get(matchKey)
					* docVector.get(matchKey);
		}
		return cosine_similarity / euc_norm;
	}

	/**
	 * calculates the euclidean norm of a vector
	 * 
	 * @param vec
	 *            Vector for which norm is calculated
	 * @return Euclidean Norm of vec
	 */
	public static double eucNorm(Iterable<Integer> vec) {
		double res = 0.0;
		for (Integer el : vec) {
			res += Math.pow(el, 2);
		}
		return Math.sqrt(res);
	}

}
