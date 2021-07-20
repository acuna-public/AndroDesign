  package ru.ointeractive.androdesign.widget;
  
  import android.content.Context;
  import android.util.AttributeSet;
  import android.view.MotionEvent;
  
  public class ViewPager2_ extends android.support.v4.view.ViewPager {
    
    // https://stackoverflow.com/questions/13667459/how-to-know-viewpager-is-scroll-left-or-right
    
    private SwipeListener mSwiperListener;
    private boolean isTouchCaptured;
    private float upX1;
    private float upY1;
    
    static final int min_distance = 20;
    boolean eventSent = false;
    
    public ViewPager2_ (Context context) {
      super (context);
    }
    
    public ViewPager2_ (Context context, AttributeSet attrs) {
      super (context, attrs);
    }
    
    @Override
    public boolean onTouchEvent (MotionEvent event) {
      
      float upX2;
      float upY2;
      
      switch (event.getAction ()) {
        
        case MotionEvent.ACTION_MOVE: {
          
          if (!isTouchCaptured) {
            
            upX1 = event.getX ();
            upY1 = event.getY ();
            
            isTouchCaptured = true;
            
          } else {
            
            upX2 = event.getX ();
            upY2 = event.getY ();
            
            float deltaX = upX1 - upX2;
            float deltaY = upY1 - upY2;
            
            //HORIZONTAL SCROLL
            
            if (Math.abs (deltaX) > Math.abs (deltaY)) {
              
              if (Math.abs (deltaX) > min_distance) {
                
                // left or right
                if (deltaX < 0) {
                  
                  if (!eventSent && mSwiperListener != null) {
                    
                    mSwiperListener.onRightSwipe ();
                    eventSent = true;
                    
                  }
                }
                
                if (deltaX > 0) {
                  
                  if (!eventSent && mSwiperListener != null) {
                    if (mSwiperListener.onLeftSwipe ()) {
                      eventSent = true;
                      return false;
                    }
                    
                  }
                  
                }
                
              } else {
                //not long enough swipe...
              }
              
            } else { // VERTICAL SCROLL
              if (Math.abs (deltaY) > min_distance) {
                // top or down
                if (deltaY < 0) {
                
                }
                if (deltaY > 0) {
                
                }
              } else {
                //not long enough swipe...
              }
            }
          }
        }
        
        break;
        
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL: {
          
          isTouchCaptured = false;
          eventSent = false;
          
        }
        
      }
      
      return super.onTouchEvent (event);
      
    }
    
    public void setOnSwipeListener (SwipeListener mSwiperListener) {
      this.mSwiperListener = mSwiperListener;
    }
    
    public interface SwipeListener {
      
      boolean onLeftSwipe ();
      boolean onRightSwipe ();
      
    }
    
  }