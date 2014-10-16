package edu.cmu.lti.f14.hw3.hw3_dklaper.casconsumers.similarity;

import java.lang.reflect.InvocationTargetException;

public class SimilarityMeasureFactory {
	
	public SimilarityMeasure getSimilarityMeasure(String measureClassName) throws ClassNotFoundException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Class<?> measureClass = Class.forName(measureClassName);
		if(!SimilarityMeasure.class.isAssignableFrom(measureClass))
		{
			throw new RuntimeException(measureClassName+" MUST be subclass of SimilarityMeasure");
		}
		
		return (SimilarityMeasure)measureClass.getConstructor().newInstance();
	}

}
