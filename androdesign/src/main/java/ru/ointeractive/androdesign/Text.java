  package ru.ointeractive.androdesign;
  /*
   Created by Acuna on 12.03.2019
  */
  
  import android.graphics.Canvas;
  import android.graphics.Paint;
  import android.os.Build;
  import android.text.Html;
  import android.text.Spannable;
  import android.text.SpannableString;
  import android.text.SpannableStringBuilder;
  import android.text.Spanned;
  import android.text.style.BackgroundColorSpan;
  import android.text.style.ForegroundColorSpan;
  import android.text.style.StyleSpan;
  import android.text.style.TypefaceSpan;
  
  import upl.json.JSONArray;
  import upl.json.JSONException;
  import upl.json.JSONObject;
  import org.jsoup.nodes.Element;

  import ru.ointeractive.andromeda.Arrays;
  import upl.core.Int;
  
  public class Text {
    
    public static SpannableString setSpan (SpannableString text, Object obj) {
      
      text.setSpan (obj, 0, Int.size (text), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      return text;
      
    }
    
    public static SpannableString setFontTypeface (String text, int style) {
      return setFontTypeface (new SpannableString (text), style);
    }
    
    public static SpannableString setFontTypeface (SpannableString text, int style) {
      return setSpan (text, new StyleSpan (style));
    }
    
    public static SpannableString setFontFamily (String text, String name) {
      return setFontFamily (new SpannableString (text), name);
    }
    
    public static SpannableString setFontFamily (SpannableString text, String name) {
      return (!name.equals ("") ? setSpan (text, new TypefaceSpan (name)) : text);
    }
    
    private static SpannableStringBuilder replace (char oldChar, String newStr, SpannableStringBuilder b) {
      
      for (int i = Int.size (b) - 1; i >= 0; i--) {
        
        if (b.charAt (i) == oldChar)
          b.replace (i, i + 1, newStr);
        
      }
      
      return b;
      
    }
    
    public static Spanned toSpanned (Element elem) {
      return toSpanned (elem.toString ());
    }
    
    @SuppressWarnings ("deprecation")
    public static Spanned toSpanned (String html) {
      
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        return Html.fromHtml (html, Html.FROM_HTML_MODE_LEGACY);
      else
        return Html.fromHtml (html);
      
    }
    
    public static Spanned toSpanned (JSONObject o) throws JSONException {
      
      SpannableString spannable = new SpannableString (o.getString ("text"));
      
      JSONArray spans = o.getJSONArray ("spans");
      
      for (int i = 0; i < Int.size (spans); ++i) {
        
        JSONObject obj = spans.getJSONObject (i);
        String type = obj.getString ("type");
        
        Object span = null;
        
        if (type.equals (Arrays.SPAN_TYPE_FOREGROUND))
          span = new ForegroundColorSpan (obj.getInt ("color"));
        else if (type.equals (Arrays.SPAN_TYPE_BACKGROUND))
          span = new BackgroundColorSpan (obj.getInt ("color"));
        else if (type.equals (Arrays.SPAN_TYPE_STYLE))
          span = new StyleSpan (obj.getInt ("style"));
        
        if (span != null)
          spannable.setSpan (span, obj.getInt ("start"), obj.getInt ("end"), obj.getInt ("flags"));
        
      }
      
      return spannable;
      
    }
    
		public static void justifyLine (Canvas canvas, Paint paint, float pageWidth, String line) {
      justifyLine (canvas, paint, pageWidth, line, 0);
    }
    
		public static void justifyLine (Canvas canvas, Paint paint, float pageWidth, String line, float leftPadding) {
      justifyLine (canvas, paint, pageWidth, line, leftPadding, 0);
    }
    
		public static void justifyLine (Canvas canvas, Paint paint, float pageWidth, String line, float leftPadding, float topPadding) {
      justifyLine (canvas, paint, pageWidth, line, leftPadding, topPadding, 0);
    }
    
		public static void justifyLine (Canvas canvas, Paint paint, float pageWidth, String line, float leftPadding, float topPadding, float wordWidth) {
			
			String[] words = line.split ("\\s+");
			
			StringBuilder lineBuilder = new StringBuilder ();
			
			for (String word : words)
				lineBuilder.append (word);
			
			wordWidth += paint.measureText (lineBuilder.toString ());
			
			float spacingWidth = (pageWidth - wordWidth) / (Int.size (words) - 1);
			
			for (String word : words) {
				
				canvas.drawText (word, leftPadding, topPadding, paint);
				leftPadding += paint.measureText (word) + spacingWidth;
				
			}
			
		}
		
  }