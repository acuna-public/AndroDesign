  package pro.acuna.androdesign.widget;
  /*
   Created by Acuna on 21.01.2019
  */
  
  import android.content.Context;
  import android.util.AttributeSet;
  import android.view.View;
  import android.view.ViewTreeObserver;
  
  public class ScrollView extends android.widget.ScrollView {
    
    private boolean loading = false;
    
    public interface OnScrollListener {
      
      void onScrollBottomEnd (View view);
      
    }
    
    private OnScrollListener listener;
    
    public void setListener (OnScrollListener listener) {
      this.listener = listener;
    }
    
    public ScrollView (Context context) {
      
      super (context);
      init ();
      
    }
    
    public ScrollView (Context context, AttributeSet attrs) {
      
      super (context, attrs);
      init ();
      
    }
    
    public ScrollView (Context context, AttributeSet attrs, int defStyle) {
      
      super (context, attrs, defStyle);
      init ();
      
    }
    
    private void init () {
      
      getViewTreeObserver ().addOnScrollChangedListener (new ViewTreeObserver.OnScrollChangedListener () {
        
        @Override
        public void onScrollChanged () {
          
          View view = getChildAt (getChildCount () - 1);
          
          int diff = (view.getBottom () - (getHeight () + getScrollY ()));
          
          if (loading && diff == 0) {
            
            loading = false;
            if (listener != null) listener.onScrollBottomEnd (view);
            
          } else loading = true;
          
        }
        
      });
      
    }
    
  }