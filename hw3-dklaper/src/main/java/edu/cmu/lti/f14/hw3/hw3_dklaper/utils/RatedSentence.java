package edu.cmu.lti.f14.hw3.hw3_dklaper.utils;

/**
 * Represents a reference to a sentence that has been rated
 * 
 */
public class RatedSentence implements Comparable<RatedSentence> {
	
	private Double senrating;
	private int sentidx;
	
	/**
	 * Constructor
	 * @param senidx Index of sentence in original lists 
	 * @param rating Rating (= Cosine Sim) of sentence
	 */
	public RatedSentence(int senidx, double rating)
	{
		sentidx = senidx;
		senrating = rating;
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
	 * Comparator for sorting -> note it's largest to smallest!!
	 */
	@Override
	public int compareTo(RatedSentence other) {
		return -this.senrating.compareTo(other.senrating);
	}

}
