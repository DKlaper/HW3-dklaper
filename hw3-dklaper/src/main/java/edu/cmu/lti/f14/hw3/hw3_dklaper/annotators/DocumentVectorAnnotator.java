package edu.cmu.lti.f14.hw3.hw3_dklaper.annotators;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.uima.UIMARuntimeException;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.lti.f14.hw3.hw3_dklaper.utils.*;
import edu.cmu.lti.f14.hw3.hw3_dklaper.annotators.textnormalizations.*;
import edu.cmu.lti.f14.hw3.hw3_dklaper.typesystems.*;

/**
 * Extract tokens from text to create sparse vector
 * 
 */
public class DocumentVectorAnnotator extends JCasAnnotator_ImplBase {

	private Tokenizer tokenizer; 
	
	@Override
	public void initialize(UimaContext aUimaContext) {
		TokenizerFactory tokfac = new TokenizerFactory();
		try {
			tokenizer = tokfac.getTokenizer((String)aUimaContext.getConfigParameterValue("TokenizerClass"), (String[])aUimaContext.getConfigParameterValue("NormalizerClasses"));
		} catch (Exception e) {
			throw new UIMARuntimeException(e);
		}
	}
	
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		// made iteration safer!
		FSIterator<Annotation> iter = jcas.getAnnotationIndex(Document.type).iterator();
		if (iter.isValid()) {
			iter.moveToNext();
			Document doc = (Document) iter.get();
			createTermFreqVector(jcas, doc);
		}

	}
	
	/**
	 * Creates sparse term vector
	 * @param jcas Cas containing the tokens
	 * @param doc The document annotation that will be enriched with the sparse document vector
	 */
	private void createTermFreqVector(JCas jcas, Document doc) {

		String docText = doc.getText();
		// tokenizer performs tokenization and normalization!
		String[] tokens = tokenizer.getNormalizedTokenization(docText);
		// use frequency map to avoid lame counting in hashmap
		FrequencyMap<String> freqs = new FrequencyMap<String>();
		
		for(String tk : tokens)
		{
			freqs.addOccurrence(tk);
		}
		
		// then create token objects
		List<Token> tokenFreqs = new LinkedList<Token>();
		Map<String, Integer> counts = freqs.getFrequencies(); 
		for(String type : counts.keySet())
		{
			Token t = new Token(jcas);
			t.setText(type);
			t.setFrequency(counts.get(type));
			tokenFreqs.add(t);
		}
		
		// finally set the token list
		doc.setTokenList(Utils.fromCollectionToFSList(jcas, tokenFreqs));
		
	}

}
