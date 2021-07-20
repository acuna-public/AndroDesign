  package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 25.02.2019
  */
  
  import android.content.Context;
  import android.graphics.drawable.Drawable;
  import android.os.Build;
  import android.util.AttributeSet;
  import android.view.ViewTreeObserver;
  
  import java.io.IOException;
  import java.io.InputStream;
  
  public class LinearLayout extends android.widget.LinearLayout {
    
    public interface Listener {
      
      void onView ();
      
    }
    
    private Listener listener;
    
    public void setListener (Listener listener) {
      this.listener = listener;
    }
    
    public LinearLayout (Context context) {
      this (context, null);
    }
    
    public LinearLayout (Context context, AttributeSet attrs) {
      this (context, attrs, 0);
    }
    
    public LinearLayout (Context context, AttributeSet attrs, int defStyle) {
    	
      super (context, attrs, defStyle);
      
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
    
    public void setBackground (InputStream stream) throws IOException {
      setBackground (Drawable.createFromStream (stream, null));
    }
    
    @Override
    public void setBackground (Drawable drawable) {
      
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
        super.setBackgroundDrawable (drawable);
      else
        super.setBackground (drawable);
      
    }
    
  }