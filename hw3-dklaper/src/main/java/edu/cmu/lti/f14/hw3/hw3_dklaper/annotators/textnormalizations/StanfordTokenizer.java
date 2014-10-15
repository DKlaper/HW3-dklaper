package edu.cmu.lti.f14.hw3.hw3_dklaper.annotators.textnormalizations;

import java.io.StringReader;
import java.util.LinkedList;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;



public class StanfordTokenizer extends Tokenizer {

	@Override
	String[] tokenize(String sentence) {
		PTBTokenizer<Word> tok = new PTBTokenizer<Word>(new StringReader(sentence), new WordTokenFactory(), "");
		LinkedList<String> res = new LinkedList<String>();
		
		while(tok.hasNext())
		{
			res.add(tok.next().word());
		}
		
		String[] resarr = new String[res.size()];
		res.toArray(resarr);
		return resarr;
	}

}
