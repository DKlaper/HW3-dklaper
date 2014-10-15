package edu.cmu.lti.f14.hw3.hw3_dklaper.annotators.textnormalizations;

/**
 * 
 * performs casefolding to lowercase
 */
public class CaseFoldNormalizer extends TextNormalizer {

	@Override
	public String normalizeToken(String tok) {
		return tok.toLowerCase();
	}

}
