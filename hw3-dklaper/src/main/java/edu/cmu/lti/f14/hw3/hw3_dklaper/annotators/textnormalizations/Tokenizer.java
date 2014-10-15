package edu.cmu.lti.f14.hw3.hw3_dklaper.annotators.textnormalizations;

import java.util.LinkedList;

public abstract class Tokenizer {
	
	protected LinkedList<TextNormalizer> txtnorms = new LinkedList<TextNormalizer>();
	
	public void addNormalizer(TextNormalizer tn)
	{
		txtnorms.add(tn);
	}
	
	abstract String[] tokenize(String sentence);
	
	public String[] getNormalizedTokenization(String sentence)
	{
		LinkedList<String> res = new LinkedList<String>();
		for(String tok : tokenize(sentence))
		{
			String normTok = tok;
			for(TextNormalizer tn : txtnorms)
			{
				normTok = tn.normalizeToken(normTok);
			}
			res.add(normTok);
		}
		
		String[] resarr = new String[res.size()];
		res.toArray(resarr);
		return resarr;
	}
}
