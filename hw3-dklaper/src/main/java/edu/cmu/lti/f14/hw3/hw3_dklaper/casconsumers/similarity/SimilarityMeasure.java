package edu.cmu.lti.f14.hw3.hw3_dklaper.casconsumers.similarity;

import java.util.HashMap;

public interface SimilarityMeasure {
	
	public double computeSimilarity(HashMap<String, Integer> query, HashMap<String, Integer> doc);

}
