  package pro.acuna.androdesign.widget;
  /*
   Created by Acuna on 21.01.2019
  */
  
  import android.content.Context;
  import android.graphics.Bitmap;
  import android.graphics.Canvas;
  import android.graphics.Color;
  import android.graphics.Paint;
  import android.graphics.PorterDuff;
  import android.graphics.PorterDuffXfermode;
  import android.graphics.RectF;
  import android.os.Build;
  import android.support.v7.widget.AppCompatImageView;
  import android.util.AttributeSet;
  import android.view.ViewTreeObserver;
  
  import pro.acuna.andromeda.Graphic;
  
  public class ImageView extends AppCompatImageView {
    
    public interface OnImageListener {
      
      void onView ();
      
    }
    
    private OnImageListener listener;
    
    public void setListener (OnImageListener listener) {
      this.listener = listener;
    }
    
    public ImageView (Context context) {
      
      super (context);
      init ();
      
    }
    
    public ImageView (Context context, AttributeSet attrs) {
      
      super (context, attrs);
      init ();
      
    }
    
    public ImageView (Context context, AttributeSet attrs, int defStyle) {
      
      super (context, attrs, defStyle);
      init ();
      
    }
    
    private void init () {
      
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
    
    public enum Mode {
      
      ROUNDED,
      SQUARED,
      
    }
    
    private Mode mode;
    
    public ImageView setMode (Mode mode) {
      
      this.mode = mode;
      return this;
      
    }
    
    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
      
      super.onMeasure (widthMeasureSpec, heightMeasureSpec);
      
      if (mode == Mode.SQUARED) {
        
        if (widthMeasureSpec > heightMeasureSpec)
          setMeasuredDimension (heightMeasureSpec, heightMeasureSpec);
        else
          setMeasuredDimension (widthMeasureSpec, widthMeasureSpec);
        
      }
      
    }
    
    @Override
    protected void onDraw (Canvas canvas) {
      
      super.onDraw (canvas);
      
      if (mode == Mode.ROUNDED) {
        
        Bitmap rounder = Graphic.createBitmap (getWidth (), getHeight (), Bitmap.Config.ARGB_8888);
        Canvas canvasRound = new Canvas (rounder);
        
        Paint xferPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
        xferPaint.setColor (Color.BLACK);
        
        final int rx = this.getWidth ();
        final int ry = this.getHeight ();
        
        canvasRound.drawRoundRect (new RectF (0, 0, rx, ry), rx, ry, xferPaint);
        
        xferPaint.setXfermode (new PorterDuffXfermode (PorterDuff.Mode.DST_IN));
        
        canvas.drawBitmap (rounder, 0, 0, xferPaint);
        
      }
      
    }
    
  }