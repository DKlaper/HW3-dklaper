package edu.cmu.lti.f14.hw3.hw3_dklaper.casconsumers.similarity;

import java.util.HashMap;
import java.util.HashSet;

public class JaccardCoefficientMeasure implements SimilarityMeasure {

	@Override
	public double computeSimilarity(HashMap<String, Integer> query,
			HashMap<String, Integer> doc) {
				
		// union of words
		HashSet<String> unionWords = new HashSet<String>(query.keySet());
		unionWords.addAll(doc.keySet());
		double denominator = unionWords.size();
		
		// intersection of words
		HashSet<String> bothWords = new HashSet<String>(query.keySet());
		bothWords.retainAll(doc.keySet());
		double numerator = bothWords.size(); // only count each word once
		
		return numerator/denominator; 
	}

}
