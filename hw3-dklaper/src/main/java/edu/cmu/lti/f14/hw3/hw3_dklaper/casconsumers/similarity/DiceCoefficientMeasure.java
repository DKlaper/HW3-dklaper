package edu.cmu.lti.f14.hw3.hw3_dklaper.casconsumers.similarity;

import java.util.HashMap;
import java.util.HashSet;

public class DiceCoefficientMeasure implements SimilarityMeasure {

	@Override
	public double computeSimilarity(HashMap<String, Integer> query,
			HashMap<String, Integer> doc) {
		
		double denominator =  Math.pow(CosineMeasure.eucNorm(query.values()),2)+Math.pow(CosineMeasure.eucNorm(doc.values()),2);
		double numerator = 0.0;
		
		HashSet<String> bothWords = new HashSet<String>(query.keySet());
		bothWords.retainAll(doc.keySet());
		numerator = bothWords.size(); // only count each word once
		
		return 2*numerator/denominator;
	}

}
