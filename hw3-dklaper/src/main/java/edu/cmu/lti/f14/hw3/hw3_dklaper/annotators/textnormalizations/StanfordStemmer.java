package edu.cmu.lti.f14.hw3.hw3_dklaper.annotators.textnormalizations;

import edu.stanford.nlp.process.Morphology;

public class StanfordStemmer extends TextNormalizer {

	private Morphology lemmatizer;
	
	public StanfordStemmer()
	{
		lemmatizer = new Morphology();
	}
	@Override
	public String normalizeToken(String tok) {
		String lemma = lemmatizer.stem(tok);
		return lemma;
	}

}
