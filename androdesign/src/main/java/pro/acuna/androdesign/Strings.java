	package pro.acuna.androdesign;
	/*
	 Created by Acuna on 18.06.2018
	*/
	
	import android.graphics.Typeface;
	import android.graphics.drawable.Drawable;
	import android.text.SpannableStringBuilder;
	import android.text.Spanned;
	import android.text.style.StyleSpan;
	import android.text.style.UnderlineSpan;
	
	import pro.acuna.jabadaba.Int;
	
	public class Strings {
		
		public static String toString (Drawable image) {
			return pro.acuna.andromeda.Strings.toString (Graphic.toBitmap (image));
		}
		
		public static String toString (Spanned text) {
			return toString (new SpannableStringBuilder (text));
		}
		
		public static String toString (SpannableStringBuilder ssBuilder) {
			
			int start, end;
			
			StyleSpan[] styleSpans = ssBuilder.getSpans (0, Int.size (ssBuilder), StyleSpan.class);
			
			for (int i = Int.size (styleSpans) - 1; i >= 0; i--) {
				
				StyleSpan span = styleSpans[i];
				
				start = ssBuilder.getSpanStart (span);
				end = ssBuilder.getSpanEnd (span);
				
				ssBuilder.removeSpan (span);
				
				if (span.getStyle () == Typeface.BOLD) {
					
					ssBuilder.insert (start, "<b>");
					ssBuilder.insert (end + 3, "</b>");
					
				} else if (span.getStyle () == Typeface.ITALIC) {
					
					ssBuilder.insert (start, "<i>");
					ssBuilder.insert (end + 3, "</i>");
					
				}
				
			}
			
			UnderlineSpan[] underSpans = ssBuilder.getSpans (0, Int.size (ssBuilder), UnderlineSpan.class);
			
			for (int i = Int.size (underSpans) - 1; i >= 0; i--) {
				
				UnderlineSpan span = underSpans[i];
				
				start = ssBuilder.getSpanStart (span);
				end = ssBuilder.getSpanEnd (span);
				
				ssBuilder.removeSpan (span);
				
				ssBuilder.insert (start, "<u>");
				ssBuilder.insert (end + 3, "</u>");
				
			}
			
			return ssBuilder.toString ();
			
		}
		
	}