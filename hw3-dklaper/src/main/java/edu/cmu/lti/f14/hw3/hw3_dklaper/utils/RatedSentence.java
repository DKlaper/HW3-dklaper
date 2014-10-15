package edu.cmu.lti.f14.hw3.hw3_dklaper.utils;

/**
 * Represents a reference to a sentence that has been rated
 * 
 */
public class RatedSentence implements Comparable<RatedSentence> {
	
	private Double senrating;
	private int sentidx;
	private Integer relevant;
	
	/**
	 * Constructor
	 * @param senidx Index of sentence in original lists 
	 * @param rating Rating (= Cosine Sim) of sentence
	 * @param relevancy Indicates whether it is relevant or not (necessary for sorting)
	 */
	public RatedSentence(int senidx, double rating, int relevancy)
	{
		sentidx = senidx;
		senrating = rating;
		relevant = relevancy;
	}
	
	/**
	 * get Rating
	 * @return Rating of the sentence
	 */
	public double getRating()
	{
		return senrating;
	}
	
	/**
	 * Get Index
	 * @return Index of the sentence data in the lists
	 */
	public int getIdx()
	{
		return sentidx;
	}

	/**
	 * Comparator for sorting -> note it's largest to smallest value!!
	 * Also relevant documents are preferred in case of tie
	 */
	@Override
	public int compareTo(RatedSentence other) {
		int res = -this.senrating.compareTo(other.senrating);
		// implement tie breaking procedures:
		if(res == 0)
		{ // prefer reevant = 1 to relevant = 0
			res = -this.relevant.compareTo(other.relevant);
		}
		
		return res;
	}

}
