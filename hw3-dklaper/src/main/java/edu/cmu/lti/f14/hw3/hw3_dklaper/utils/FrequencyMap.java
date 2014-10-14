package edu.cmu.lti.f14.hw3.hw3_dklaper.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Data structure that takes a sequence and counts the occurrence of each type
 * 
 * Makes sure that count map cannot be modified by using unmodifiable map
 * Note: Similar to a very basic version of nltk.freqdist in python
 */
public class FrequencyMap<Tkey> {
	
	/**
	 *  stores the frequencies
	 */
	private HashMap<Tkey, Integer> freq = new HashMap<Tkey, Integer>();

	/**
	 * Returns an unmodifiable map of type frequencies
	 * @return Map of types to frequencies based on occurrences added so far
	 */
	public Map<Tkey, Integer> getFrequencies()
	{
		return Collections.unmodifiableMap(freq);
	}
	
	/**
	 * Add this token as seen occurrence, i.e., increase its frequency by one
	 * @param token The token to be added as occurrence
	 */
	public void addOccurrence(Tkey token)
	{
		// check that it's there and add one
		if(!freq.containsKey(token)){
			freq.put(token, 0);
		}
		Integer val = freq.get(token);
		freq.put(token, val+1);
	}

	/**
	 * Add all tokens in the sequence to the frequency count
	 * @param sequence Sequence to be added
	 */
	public void addSequence(Iterable<Tkey> sequence)
	{
		for(Tkey k : sequence)
		{
			addOccurrence(k);
		}
	}
	
	
}
