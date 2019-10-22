  package pro.acuna.androdesign.widget;
  /*
   Created by Acuna on 14.02.2019
  */
  
  import android.content.Context;
  import android.content.res.TypedArray;
  import android.graphics.Canvas;
  import android.graphics.Paint;
  import android.graphics.drawable.ColorDrawable;
  import android.graphics.drawable.Drawable;
  import android.os.Build;
  import android.support.v7.widget.AppCompatTextView;
  import android.text.Layout;
  import android.text.Spannable;
  import android.text.SpannableString;
  import android.text.SpannableStringBuilder;
  import android.text.Spanned;
  import android.text.StaticLayout;
  import android.text.TextPaint;
  import android.text.style.AbsoluteSizeSpan;
  import android.text.style.DynamicDrawableSpan;
  import android.text.style.ImageSpan;
  import android.text.style.LeadingMarginSpan;
  import android.util.AttributeSet;
  import android.view.Gravity;
  import android.view.ViewTreeObserver;
  
  import java.io.InputStream;
  import java.util.ArrayList;
  import java.util.List;
  
  import pro.acuna.androdesign.R;
  import pro.acuna.andromeda.Log;
  import pro.acuna.jabadaba.Arrays;
  import pro.acuna.jabadaba.Int;
  
  public class TextView extends AppCompatTextView {
    
    private TypedArray attrs;
    
    private Layout spaceLayout, topLayout;
    private int spaceWidth;
    private int paragraphNum = 0, paragraphLine = 0;
    
    private List<CharSequence> paragraphs = new ArrayList<> ();
    private Spannable redLineSpannable, paragraphTopSpannable;
    private SpannableStringBuilder pageText = new SpannableStringBuilder ();
    private int redLineLength = 0, paragraphTopPadding = 0;
    
    public interface Listener {
      
      void onView ();
      
    }
    
    private Listener listener;
    
    public void setListener (Listener listener) {
      this.listener = listener;
    }
    
    public TextView (Context context) {
      
      super (context);
      init (null, 0);
      
    }
    
    public TextView (Context context, AttributeSet attrs) {
      
      super (context, attrs);
      init (attrs, 0);
      
    }
    
    public TextView (Context context, AttributeSet attrs, int defStyle) {
      
      super (context, attrs, defStyle);
      init (attrs, defStyle);
      
    }
    
    private void init (AttributeSet attrSet, int defStyle) {
      
      attrs = getContext ().obtainStyledAttributes (attrSet, R.styleable.TextView, defStyle, 0);
      
      getViewTreeObserver ().addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener () {
        
        @Override
        public void onGlobalLayout () {
          
          if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            getViewTreeObserver ().removeGlobalOnLayoutListener (this);
          else
            getViewTreeObserver ().removeOnGlobalLayoutListener (this);
          
          if (listener != null) listener.onView ();
          
        }
        
      });
      
    }
    
    public void setPadding (int... padding) {
      
      if (Int.size (padding) == 1)
        super.setPadding (padding[0], padding[0], padding[0], padding[0]);
      else
        super.setPadding (padding[0], padding[1], padding[2], padding[3]);
      
    }
    
    public void setBackground (InputStream stream) {
      setBackground (Drawable.createFromStream (stream, null));
    }
    
    @Override
    public void setBackground (Drawable drawable) {
      
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
        super.setBackgroundDrawable (drawable);
      else
        super.setBackground (drawable);
      
    }
    
    public TextView setRedLineLength (int length) {
      
      redLineLength = length;
      return this;
      
    }
    
    public int getRedLineLength () {
      return redLineLength;
    }
    
    public TextView setParagraphTopPadding (int length) {
      
      paragraphTopPadding = length;
      return this;
      
    }
    
    public int getParagraphTopPadding () {
      return paragraphTopPadding;
    }
    
    private SpannableStringBuilder paragraphText = new SpannableStringBuilder ();
    
    public void setParagraph (CharSequence text) {
      
      if (redLineSpannable == null) {
        
        TextPaint paint = new TextPaint ();
        
        paint.setTextSize (getTextSize ());
        paint.setTypeface (getTypeface ());
        
        spaceLayout = new StaticLayout (" ", paint, 10, Layout.Alignment.ALIGN_NORMAL, 0, 1, false);
        spaceWidth = (int) spaceLayout.getLineWidth (0);
        
        //pageText.append (redLineSpannable);
        
        topLayout = new StaticLayout (" ", paint, 0, Layout.Alignment.ALIGN_NORMAL, 150, 1, false);
        
      }
      
      paragraphs.add (text);
      
      if (redLineLength > 0) {
        
        redLineSpannable = new SpannableStringBuilder (" ");
        redLineSpannable.setSpan (new LeadingMarginSpan.Standard (redLineLength, 0), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        pageText.append (redLineSpannable);
        
      }
      
      pageText.append (text);
      
      if (paragraphTopPadding > 0)
        pageText.append (topLayout.getText ());
      
    }
    
    private void setPage (int startOffset, int endOffset) {
      
      SpannableStringBuilder pageText = new SpannableStringBuilder ();
      
      /*if (endOffset > Int.size (paragraphText)) {
        
        if (paragraphNum < Int.size (paragraphs) - 1)
          ++paragraphNum;
        
        //paragraphText = paragraphs.get (paragraphNum);
        paragraphLine = 0;
        
      } else ++paragraphLine;*/
      
      CharSequence sequence = getLayout ().getText ().subSequence (startOffset, endOffset);
      //Log.w (sequence);
      
      if (getGravity () == Gravity.FILL && getLayout ().getLineCount () > 1 && paragraphLine < getLayout ().getLineCount () - 2) { // Выравнивание по ширине
        
        int pageWidth = (getWidth () - (getPaddingLeft () + getPaddingRight ()));
        
        List<CharSequence> words = Arrays.explode (" ", sequence);
        
        int spaceCount = (Int.size (words) - 1);
        if (spaceCount <= 0) spaceCount = 1;
        
        int removeSpaceWidth = ((int) getLayout ().getLineWidth (paragraphLine) - (spaceWidth * spaceCount));
        int eachSpaceWidth = ((pageWidth - removeSpaceWidth) / spaceCount);
        
        for (int word = 0; word < Int.size (words); ++word) {
          
          Spanned lineText = (Spanned) words.get (word);
          
          if (Int.size (lineText) > 0) {
            
            if (word > 0) {
              
              SpannableStringBuilder newLineText = new SpannableStringBuilder (spaceLayout.getText ());
              
              Drawable drawable = new ColorDrawable (0x00ffffff);
              drawable.setBounds (0, 0, eachSpaceWidth, 0);
              
              newLineText.setSpan (new ImageSpan (drawable), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
              
              pageText.append (newLineText);
              
            }
            
            pageText.append (lineText);
            
          } else pageText.append ("\n");
          
        }
        
      } else pageText.append (sequence);
      
    }
    
    @Override
    public void onDraw (Canvas canvas) {
      //Log.w (Int.size (paragraphs));
      super.onDraw (canvas);
    }
    
    public List<CharSequence> splitPages () { // Пагинация
      
      setText (pageText);
      
      int pageHeight = (getHeight () - (getPaddingLeft () + getPaddingRight ()));
      
      List<CharSequence> pages = new ArrayList<> ();
      
      int pageStartOffset = 0, pageEndOffset;
      int height = pageHeight;
      
      for (int line = 0; line < getLayout ().getLineCount (); ++line) {
        
        //int startOffset = getLayout ().getLineStart (line);
        //int endOffset = getLayout ().getLineEnd (line);
        
        if (getLayout ().getLineBottom (line) > height) {
          
          pageEndOffset = getLayout ().getLineStart (line);
          
          pages.add (getLayout ().getText ().subSequence (pageStartOffset, pageEndOffset));
          
          pageStartOffset = getLayout ().getLineStart (line);
          height = pageHeight + getLayout ().getLineTop (line);
          
          //Log.w (paragraphNum, pageHeight, text2);
          
        }// else setPage (startOffset, endOffset);
        
        if (line == (getLayout ().getLineCount () - 1)) {
          
          pageEndOffset = getLayout ().getLineEnd (line);
          pages.add (getLayout ().getText ().subSequence (pageStartOffset, pageEndOffset));
          
        }
        
      }
      
      return pages;
      
    }
    
    public TextView setText (CharSequence text, boolean imposed) {
      
      if (imposed)
        setParagraph (text);
      else
        super.setText (text);
      
      return this;
      
    }
    
  }