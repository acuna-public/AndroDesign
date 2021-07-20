  package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 21.01.2019
  */
  
  import android.content.Context;
  import android.support.annotation.NonNull;
  import android.util.AttributeSet;
  
  public class RecyclerView extends android.support.v7.widget.RecyclerView {
    
    public interface OnScrollListener {
      
      void onScrollBottomEnd (RecyclerView recyclerView, int scrollState);
      
    }
    
    private OnScrollListener listener;
    
    public void setListener (OnScrollListener listener) {
      this.listener = listener;
    }
    
    public RecyclerView (Context context) {
      this (context, null);
    }
    
    public RecyclerView (Context context, AttributeSet attrs) {
      this (context, attrs, 0);
    }
	
	  private boolean loading = false;
		
	  public RecyclerView (Context context, AttributeSet attrs, int defStyle) {
      
      super (context, attrs, defStyle);
      
	    setLayoutManager (new LinearLayoutManager (getContext ()));
    	
      addOnScrollListener (new android.support.v7.widget.RecyclerView.OnScrollListener () {
        
        @Override
        public void onScrolled (@NonNull android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
          if (dy > 0) loading = true;
        }
        
        @Override
        public void onScrollStateChanged (@NonNull android.support.v7.widget.RecyclerView recyclerView, int scrollState) {
          
          if (loading && !canScrollVertically (1)) {
            
            loading = false;
            if (listener != null) listener.onScrollBottomEnd (RecyclerView.this, scrollState);
            
          }
          
        }
        
      });
      
    }
    
    @Override
    public boolean canScrollVertically (int direction) {
    	
	    int offset = computeVerticalScrollOffset ();
	    int range = computeVerticalScrollRange () - computeVerticalScrollExtent ();
	    
	    if (range == 0) return false;
	    
	    if (direction < 0)
	      return offset > 0;
	    else
	      return offset < range - 1;
	    
		}
		
    /*private boolean expanded = true;
    
    public boolean isExpanded () {
      return expanded;
    }
    
    @Override
    public void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
      
      // HACK! TAKE THAT ANDROID!
      
      if (isExpanded ()) {
        
        // Calculate entire height by providing a very large height hint.
        // View.MEASURED_SIZE_MASK represents the largest height possible.
        
        int expandSpec = MeasureSpec.makeMeasureSpec (MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
        
        super.onMeasure (widthMeasureSpec, expandSpec);
        
        ViewGroup.LayoutParams params = getLayoutParams ();
        params.height = getMeasuredHeight ();
        
      } else super.onMeasure (widthMeasureSpec, heightMeasureSpec);
      
    }*/
    
  }