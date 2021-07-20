  package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 09.11.2018
  */
  
  import android.content.Context;
  import android.support.v7.widget.RecyclerView;
  import android.util.AttributeSet;
  
  import ru.ointeractive.andromeda.System;
  
  public class LinearLayoutManager extends android.support.v7.widget.LinearLayoutManager {
    
    private Context context;
    
    public LinearLayoutManager (Context context) {
      
      super (context);
      this.context = context;
      
    }
    
    public LinearLayoutManager (Context context, int orientation, boolean reverseLayout) {
      
      super (context, orientation, reverseLayout);
      this.context = context;
      
    }
    
    public LinearLayoutManager (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      
      super (context, attrs, defStyleAttr, defStyleRes);
      this.context = context;
      
    }
    
    @Override
    public void onLayoutChildren (RecyclerView.Recycler recycler, RecyclerView.State state) {
      
      try {
        super.onLayoutChildren (recycler, state);
      } catch (IndexOutOfBoundsException e) {
        System.error (context, e, "", "layout");
      }
      
    }
    
  }