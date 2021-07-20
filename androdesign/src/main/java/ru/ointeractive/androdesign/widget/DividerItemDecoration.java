  package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 17.08.2016
  */
  
  import android.content.Context;
  import android.content.res.TypedArray;
  import android.graphics.Canvas;
  import android.graphics.Rect;
  import android.graphics.drawable.Drawable;
  import android.support.v7.widget.LinearLayoutManager;
  import android.support.v7.widget.RecyclerView;
  import android.view.View;
  
  public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    
    private Drawable mDivider;
    private int mOrientation;
    
    public DividerItemDecoration (Context context, int orientation) {
      
      TypedArray attr = context.obtainStyledAttributes (new int[] { android.R.attr.listDivider });
      
      mDivider = attr.getDrawable (0);
      attr.recycle ();
      
      mOrientation = orientation;
      
    }
    
    @Override
    public void onDrawOver (Canvas c, RecyclerView parent, RecyclerView.State state) {
      
      if (mOrientation == LinearLayoutManager.VERTICAL)
        drawVertical (c, parent);
      else if (mOrientation == LinearLayoutManager.HORIZONTAL)
        drawHorizontal (c, parent);
      else {
        
        drawVertical (c, parent);
        drawHorizontal (c, parent);
        
      }
      
    }
    
    private void drawVertical (Canvas c, RecyclerView parent) {
      
      int left = parent.getPaddingLeft ();
      int right = parent.getWidth () - parent.getPaddingRight ();
      
      for (int i = 0; i < parent.getChildCount (); ++i) {
        
        View child = parent.getChildAt (i);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams ();
        
        int top = child.getBottom () + params.bottomMargin + mDivider.getIntrinsicHeight ();
        int bottom = top + mDivider.getIntrinsicHeight ();
        
        mDivider.setBounds ((left + paddingLeft), top, (right - paddingRight), bottom);
        mDivider.draw (c);
        
      }
      
    }
    
    private void drawHorizontal (Canvas c, RecyclerView parent) {
      
      int top = parent.getPaddingTop ();
      int bottom = parent.getHeight () - parent.getPaddingBottom ();
      
      for (int i = 0; i < parent.getChildCount (); ++i) {
        
        View child = parent.getChildAt (i);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams ();
        
        int left = child.getRight () + params.rightMargin + mDivider.getIntrinsicHeight ();
        int right = (left + mDivider.getIntrinsicWidth ());
        
        mDivider.setBounds (left, top, right, bottom);
        mDivider.draw (c);
        
      }
      
    }
    
    private int paddingLeft = 0, paddingTop = 0, paddingRight = 0, paddingBottom = 0;
    
    public void setPadding (int left, int top, int right, int bottom) {
      
      paddingLeft = left;
      paddingTop = top;
      paddingRight = right;
      paddingBottom = bottom;
      
    }
    
    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
      
      if (mOrientation == LinearLayoutManager.VERTICAL)
        outRect.set (paddingLeft, paddingTop, paddingRight, mDivider.getIntrinsicHeight ());
      else if (mOrientation == LinearLayoutManager.HORIZONTAL)
        outRect.set (paddingLeft, paddingTop, mDivider.getIntrinsicWidth (), paddingBottom);
      else
        outRect.set (paddingLeft, paddingTop, mDivider.getIntrinsicWidth (), mDivider.getIntrinsicHeight ());
      
    }
    
    public void setDrawable (Drawable drawable) {
      mDivider = drawable;
    }
    
  }