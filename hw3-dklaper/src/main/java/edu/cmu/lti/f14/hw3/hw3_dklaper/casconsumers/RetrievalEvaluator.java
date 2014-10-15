package edu.cmu.lti.f14.hw3.hw3_dklaper.casconsumers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.uima.UIMARuntimeException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;

import edu.cmu.lti.f14.hw3.hw3_dklaper.typesystems.Document;
import edu.cmu.lti.f14.hw3.hw3_dklaper.typesystems.Token;
import edu.cmu.lti.f14.hw3.hw3_dklaper.utils.RatedSentence;
import edu.cmu.lti.f14.hw3.hw3_dklaper.utils.Utils;

/**
 * Retrieves and evaluates based on cosine similarity
 */
public class RetrievalEvaluator extends CasConsumer_ImplBase {

	/** query id number **/
	public ArrayList<Integer> qIdList;

	/** query and text relevant values **/
	public ArrayList<Integer> relList;

	/** original text for report **/
	public ArrayList<String> origText;
	
	/** frequency values **/
	public ArrayList<HashMap<String, Integer>> tokenFreq;
		
	/** Map from query id to query index**/
	public HashMap<Integer, Integer> mapidx;
	
	/** Name of the outputFile (read from descriptor param) **/
	public String outputFile;
	
	@Override
	public void initialize() throws ResourceInitializationException {

		qIdList = new ArrayList<Integer>();
		outputFile = (String)getConfigParameterValue("OutputFile");
		relList = new ArrayList<Integer>();
		origText = new ArrayList<String>();
		tokenFreq = new ArrayList<HashMap<String,Integer>>();
		mapidx = new HashMap<Integer, Integer>();

	}

	/**
	 * brings Cas into right format to be processed
	 */
	@Override
	public void processCas(CAS aCas) throws ResourceProcessException {

		JCas jcas;
		try {
			jcas =aCas.getJCas();
		} catch (CASException e) {
			throw new ResourceProcessException(e);
		}

		FSIterator<Annotation> it = jcas.getAnnotationIndex(Document.type).iterator();
	
		if (it.hasNext()) {
			Document doc = (Document) it.next();

			//Make sure that your previous annotators have populated this in CAS
			FSList fsTokenList = doc.getTokenList();
			
			// now copy tokens back to hashmap for getcosine similarity.
			ArrayList<Token>tokenList = Utils.fromFSListToCollection(fsTokenList, Token.class);
			HashMap<String, Integer> toks = new HashMap<String, Integer>(tokenList.size());
			for(Token t : tokenList)
			{
				toks.put(t.getText(), t.getFrequency());
			}
			
			qIdList.add(doc.getQueryID());
			relList.add(doc.getRelevanceValue());
			//persist other important data
			origText.add(doc.getText());
			tokenFreq.add(toks);
			
			// if it's a query the last added element is index of query
			if(doc.getRelevanceValue() == 99)
			{
				mapidx.put(doc.getQueryID(), tokenFreq.size()-1);
			}

		}

	}

	/**
	 * Aggregates sentences per queryid, computes cosine similarity, ranks and measures MRR
	 */
	@Override
	public void collectionProcessComplete(ProcessTrace arg0)
			throws ResourceProcessException, IOException {

		super.collectionProcessComplete(arg0);
		
		ArrayList<String> reportLines = new ArrayList<String>();
		HashMap<Integer, ArrayList<RatedSentence>> queryAnswers = new HashMap<Integer, ArrayList<RatedSentence>>();
		// compute the cosine similarity measure for each sentence
		for(int idx = 0; idx < origText.size(); ++idx)
		{
			// make sure query id is in answer hashmap that aggregates per query
			if(!queryAnswers.containsKey(qIdList.get(idx)))
			{
				queryAnswers.put(qIdList.get(idx), new ArrayList<RatedSentence>());
			}
			// skip queries
			if(relList.get(idx) == 99)
			{
				continue;
			}
			// calculate cosine similarity and add to answers
			int queryidx = mapidx.get(qIdList.get(idx));
			double cos = computeCosineSimilarity(tokenFreq.get(queryidx), tokenFreq.get(idx));
			RatedSentence sen = new RatedSentence(idx, cos, relList.get(idx));
			queryAnswers.get(qIdList.get(idx)).add(sen);
		}
		
		
		// compute the rank and mrr of retrieved sentences for each query
		double metric_mrr = 0.0;
		// make sure its sorted by queryid
		Integer[] qids = new Integer[queryAnswers.size()];
		queryAnswers.keySet().toArray(qids);
		Arrays.sort(qids);
		// then compute rank
		for(Integer queryidx : qids)
		{
			ArrayList<RatedSentence> candidates = queryAnswers.get(queryidx);
			Collections.sort(candidates);
			int rank = computeRank(candidates); 
			reportLines.add(getReportString(candidates.get(rank-1), rank));
			// avg contribution across each query
			metric_mrr += (1/(double)rank)/queryAnswers.size();
		}
		
		writeReport(reportLines, metric_mrr);
		System.out.println(" (MRR) Mean Reciprocal Rank ::" + metric_mrr);
	}
	
	private void writeReport(ArrayList<String> lines, double mrr)
	{
		try {
			FileWriter fwr = new FileWriter(outputFile, false);
			for(String ln : lines)
			{
				fwr.write(ln+"\n");
			}
			fwr.write(String.format("MRR=%.4f\n", mrr));
			fwr.close();
		} catch (IOException e) {
			throw new UIMARuntimeException(e);
		}
	}

	/**
	 * Gets the string required for the report.txt file
	 * @param ratedSentence The sentence with highest rank that is relevant
	 * @param rank The rank of the sentence within the query
	 * @return String formatted according to reportspec
	 */
	private String getReportString(RatedSentence ratedS, int rank) {
		assert relList.get(ratedS.getIdx()) == 1;
		return String.format("cosine=%.4f\trank=%d\tqid=%d\trel=1\t%s", ratedS.getRating(), rank, qIdList.get(ratedS.getIdx()), origText.get(ratedS.getIdx()));
	}

	/**
	 * Computes cosine similarity between query and document
	 * @param queryVector sparse word vector of query
	 * @param docVector sparse word vector of document
	 * @return Cosine similarity
	 */
	private double computeCosineSimilarity(Map<String, Integer> queryVector,
			Map<String, Integer> docVector) {
		double cosine_similarity=0.0;
		
		double euc_norm = eucNorm(queryVector.values())*eucNorm(docVector.values());
		
		// make a copy to avoid changing the original keyset :P
		HashSet<String> wordsInBoth = new HashSet<String>(queryVector.keySet());
		// only need to match those that appear in both.
		wordsInBoth.retainAll(docVector.keySet());
		
		for(String matchKey : wordsInBoth) // scalar multiplication of vectors
		{
			cosine_similarity += queryVector.get(matchKey)*docVector.get(matchKey);
		}

		return cosine_similarity/euc_norm;
	}
	
	/**
	 * calculates the euclidean norm of a vector
	 * @param vec Vector for which norm is calculated
	 * @return Euclidean Norm of vec
	 */
	private double eucNorm(Iterable<Integer> vec)
	{
		double res = 0.0;
		for(Integer el : vec)
		{
			res += Math.pow(el, 2);
		}
		
		return Math.sqrt(res);
	}

	/**
	 * Computes the rank for the ranked sentences
	 * The supplied list MUST be sorted already by the desired criterion
	 * @param rankedSents sorted list of the sentences from highest to lowest ranked
	 * @return Rank of first relevant answer
	 */
	private int computeRank(ArrayList<RatedSentence> rankedSents)
	{
		int rank = 1;
		for(int r = 1; r <= rankedSents.size(); ++r)
		{
			rank = r;
			// if the current sentence (idx = r-1) is relevant we stop
			if(relList.get(rankedSents.get(r-1).getIdx()) == 1)
			{
				break;
			}
		}
		
		return rank;
	}

}
