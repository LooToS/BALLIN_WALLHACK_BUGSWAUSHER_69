package ktar.five.TurfWars.Game.Cooling;


public enum utilTime {
	        BEST,
	        DAYS,
	        HOURS,
	        MINUTES,
	        SECONDS;
	        
	        
	        public static double convert(long time, utilTime unit, int decPoint) {
	            if(unit == utilTime.BEST) {
	                if(time < 60000L) unit = utilTime.SECONDS;
	                else if(time < 3600000L) unit = utilTime.MINUTES;
	                else if(time < 86400000L) unit = utilTime.HOURS;
	                else unit = utilTime.DAYS;
	            }
	            if(unit == utilTime.SECONDS) return utilMath.trim(time / 1000.0D, decPoint);
	            if(unit == utilTime.MINUTES) return utilMath.trim(time / 60000.0D, decPoint);
	            if(unit == utilTime.HOURS) return utilMath.trim(time / 3600000.0D, decPoint);
	            if(unit == utilTime.DAYS) return utilMath.trim(time / 86400000.0D, decPoint);
	            return utilMath.trim(time, decPoint);
	        }
	        
	        
	}


	

