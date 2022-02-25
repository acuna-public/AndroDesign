  package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 04.12.2021
  */
  
  import android.content.Context;
  import android.graphics.drawable.Drawable;
  import android.os.Build;
  import android.util.AttributeSet;
  import android.view.ViewTreeObserver;
  
  import java.io.IOException;
  import java.io.InputStream;
  
  import ru.ointeractive.andromeda.OS;
  
  public class RelativeLayout extends android.widget.RelativeLayout {
    
    public interface Listener {
      
      void onView (RelativeLayout layout);
      
    }
    
    private Listener listener;
    
    public void setListener (Listener listener) {
      this.listener = listener;
    }
    
    public RelativeLayout (Context context) {
      this (context, null);
    }
    
    public RelativeLayout (Context context, AttributeSet attrs) {
      this (context, attrs, 0);
    }
    
    public RelativeLayout (Context context, AttributeSet attrs, int defStyle) {
    	
      super (context, attrs, defStyle);
      
      getViewTreeObserver ().addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener () {
        
        @Override
        public void onGlobalLayout () {
          
          if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            getViewTreeObserver ().removeGlobalOnLayoutListener (this);
          else
            getViewTreeObserver ().removeOnGlobalLayoutListener (this);
          
          if (listener != null) listener.onView (RelativeLayout.this);
          
        }
        
      });
      
    }
    
    public void setBackground (InputStream stream) throws IOException {
      setBackground (Drawable.createFromStream (stream, null));
    }
    
    @Override
    public void setBackground (Drawable drawable) {
      
      if (OS.SDK < Build.VERSION_CODES.JELLY_BEAN)
        super.setBackgroundDrawable (drawable);
      else
        super.setBackground (drawable);
      
    }
    
  }