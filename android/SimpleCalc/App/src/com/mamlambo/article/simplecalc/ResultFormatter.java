package com.mamlambo.article.simplecalc;

public class ResultFormatter {

    public String getFormattedValue(Double dblAnswer)
    {
        Integer intAnswer = dblAnswer.intValue();
        
        if (dblAnswer == (double)intAnswer) {
        	return intAnswer.toString();
        } else {
        	return dblAnswer.toString();
        }
    }	
}
