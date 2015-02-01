package ktar.five.TurfWars.Game.Cooling;

import java.text.DecimalFormat;

public class utilMath {

	 public static double trim(double untrimmeded, int decimal) {
	        String format = "#.#";
	 
	        for(int i = 1; i < decimal; i++) {
	            format = format + "#";
	        }
	        DecimalFormat twoDec = new DecimalFormat(format);
	        return Double.valueOf(twoDec.format(untrimmeded));
	    }
	
}
