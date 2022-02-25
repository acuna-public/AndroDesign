  package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 14.02.2019
  */
  
  import android.annotation.TargetApi;
  import android.content.Context;
  import android.content.res.TypedArray;
  import android.graphics.drawable.Drawable;
  import android.os.Build;
  import android.support.annotation.NonNull;
  import android.support.v7.widget.AppCompatTextView;
  import android.util.AttributeSet;
  import android.view.MotionEvent;
  import android.view.ViewTreeObserver;
  
  import java.io.InputStream;

  import ru.ointeractive.andromeda.OS;
  import upl.core.Int;
  
  public class TextView extends AppCompatTextView {
    
    public TypedArray attrs;
    
    public TextView (Context context) {
      this (context, null);
    }
    
    public TextView (Context context, AttributeSet attrs) {
      this (context, attrs, 0);
    }
    
    public TextView (Context context, AttributeSet attrSet, int defStyle) {
      
      super (context, attrSet, defStyle);
      
      //attrs = getContext ().obtainStyledAttributes (attrSet, R.styleable.TextView, defStyle, 0);
      
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
    
		private float mBaseRatio = 0, mBaseDist = 0, mRatio = 0;
		
		@Override
		public boolean onTouchEvent (@NonNull MotionEvent event) {
			
			if (OS.SDK >= 5 && event.getPointerCount () == 2) {
				
				int action = event.getAction ();
				int pureaction = action & MotionEvent.ACTION_MASK;
				
				if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
					
					mBaseDist = getDistance (event);
					mBaseRatio = mRatio;
					
				} else {
					
					float delta = (getDistance (event) - mBaseDist) / 200;
					float multi = (float) Math.pow (2, delta);
					mRatio = Math.min (1024.0f, Math.max (0.1f, mBaseRatio * multi));
					setTextSize (mRatio + 13);
					
				}
				
				return true;
				
			} else return super.onTouchEvent (event);
			
		}
		
		@TargetApi (5)
		private int getDistance (MotionEvent event) {
			
			int dx = (int) (event.getX (0) - event.getX (1));
			int dy = (int) (event.getY (0) - event.getY (1));
			
			return (int) (Math.sqrt (dx * dx + dy * dy));
			
		}
		
    public interface Listener {
      
      void onView ();
      
    }
    
    private Listener listener;
    
    public void setListener (Listener listener) {
      this.listener = listener;
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
    
  }