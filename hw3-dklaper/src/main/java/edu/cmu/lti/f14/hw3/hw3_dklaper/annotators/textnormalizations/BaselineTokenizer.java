package edu.cmu.lti.f14.hw3.hw3_dklaper.annotators.textnormalizations;

/**
 * 
 * Tokenize on pattern \s+
 */
public class BaselineTokenizer extends Tokenizer {

	@Override
	String[] tokenize(String sentence) {
		return sentence.split("\\s+");
	}

}
