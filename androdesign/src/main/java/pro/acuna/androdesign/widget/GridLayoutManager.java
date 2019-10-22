  package pro.acuna.androdesign.widget;
  /*
   Created by Acuna on 27.01.2019
  */
  
  import android.content.Context;
  import android.support.v7.widget.RecyclerView;
  import android.util.AttributeSet;
  
  import pro.acuna.andromeda.Device;
  
  public class GridLayoutManager extends android.support.v7.widget.GridLayoutManager {
    
    private Context context;
    private static final int COLS_NUM = 1;
    
    public GridLayoutManager (Context context) {
      this (context, COLS_NUM);
    }
    
    public GridLayoutManager (Context context, int colsNum) {
      
      super (context, colsNum);
      this.context = context;
      
    }
    
    public GridLayoutManager (Context context, int colsNum, int orientation) {
      this (context, colsNum, orientation, false);
    }
    
    public GridLayoutManager (Context context, int orientation, boolean reverseLayout) {
      this (context, COLS_NUM, orientation, reverseLayout);
    }
    
    public GridLayoutManager (Context context, int colsNum, int orientation, boolean reverseLayout) {
      super (context, colsNum, orientation, reverseLayout);
    }
    
    public GridLayoutManager (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super (context, attrs, defStyleAttr, defStyleRes);
    }
    
    private int width = 150;
    
    public GridLayoutManager setWidth (int width) {
      
      this.width = width;
      return this;
      
    }
    
    @Override
    public void onLayoutChildren (RecyclerView.Recycler recycler, RecyclerView.State state) {
      
      setSpanCount (Device.widthColsNum (context, width));
      
      super.onLayoutChildren (recycler, state);
      
    }
    
  }