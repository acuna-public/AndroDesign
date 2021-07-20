  package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 27.01.2019
  */
  
  import android.content.Context;
  import android.support.v7.widget.RecyclerView;
  import android.util.AttributeSet;
  
  import ru.ointeractive.andromeda.Device;
  
  public class GridLayoutManager extends android.support.v7.widget.GridLayoutManager {
    
    private Context context;
    private static int colsNum = 1;
    
    public GridLayoutManager (Context context) {
      this (context, colsNum);
    }
    
    public GridLayoutManager (Context context, AttributeSet attrs) {
      this (context, attrs, 0);
    }
    
    public GridLayoutManager (Context context, AttributeSet attrs, int defStyleAttr) {
      this (context, attrs, defStyleAttr, 0);
    }
    
    public GridLayoutManager (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super (context, attrs, defStyleAttr, defStyleRes);
    }
    
    public GridLayoutManager (Context context, int colsNum) {
      
      super (context, colsNum);
      this.context = context;
      
    }
    
    public GridLayoutManager (Context context, int colsNum, int orientation) {
      this (context, colsNum, orientation, false);
    }
    
    public GridLayoutManager (Context context, int orientation, boolean reverseLayout) {
      this (context, colsNum, orientation, reverseLayout);
    }
    
    public GridLayoutManager (Context context, int colsNum, int orientation, boolean reverseLayout) {
      
      super (context, colsNum, orientation, reverseLayout);
      GridLayoutManager.colsNum = colsNum;
      
    }
    
    private int width = 150;
    
    public GridLayoutManager setWidth (int width) {
      
      this.width = width;
      return this;
      
    }
    
    @Override
    public void onLayoutChildren (RecyclerView.Recycler recycler, RecyclerView.State state) {
      
      super.onLayoutChildren (recycler, state);
      setSpanCount (Device.widthColsNum (context, width));
      
    }
    
  }