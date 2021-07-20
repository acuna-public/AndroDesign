	package ru.ointeractive.androdesign;
	
	import android.content.Context;
	
	public class Locales {
		
		public static String[] monthNames (Context context) {
			return context.getResources ().getStringArray (R.array.month);
		}
		
	}