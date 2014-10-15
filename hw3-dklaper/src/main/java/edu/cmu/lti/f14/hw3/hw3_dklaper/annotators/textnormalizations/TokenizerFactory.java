package edu.cmu.lti.f14.hw3.hw3_dklaper.annotators.textnormalizations;

import java.lang.reflect.InvocationTargetException;

/**
 * Factory that takes classnames to create a tokenizer for tokenization + normalization of tokens
 * 
 */
public class TokenizerFactory {

	
	public Tokenizer getTokenizer(String tokenizerClassName, String[] normalizerClassNames) throws RuntimeException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{   
		if(normalizerClassNames == null)
		{
			normalizerClassNames = new String[0]; // empty array will lead to correct result without exception
		}
		// create tokenizer
		Class<?> tokenizerClass = Class.forName(tokenizerClassName);
		if(!Tokenizer.class.isAssignableFrom(tokenizerClass))
		{
			throw new RuntimeException(tokenizerClassName+" MUST be subclass of Tokenizer");
		}
		
		Tokenizer tok = (Tokenizer) tokenizerClass.getConstructor().newInstance();
		
		// add normalizers
		for(String nCn : normalizerClassNames)
		{
			Class<?> normalizerClass = Class.forName(nCn);
			if(!TextNormalizer.class.isAssignableFrom(normalizerClass))
			{
				throw new RuntimeException(nCn+" MUST be subclass of TextNormalizer");
			}
			Object norm = normalizerClass.getConstructor().newInstance();
			tok.addNormalizer((TextNormalizer)norm);
			
		}
		
		return tok;
	}
}
