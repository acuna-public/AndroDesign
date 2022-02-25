	package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 21.01.2019
  */
	
	import android.content.Context;
	import android.util.AttributeSet;
	import android.view.View;
	import android.view.ViewGroup;
	import android.widget.AbsListView;
	
	import ru.ointeractive.andromeda.OS;
	
	// https://stackoverflow.com/questions/8481844/gridview-height-gets-cut
	
	public class GridView extends android.widget.GridView {
		
		private boolean loading = false;
		
		public interface OnScrollListener {
			
			void onScrollBottomEnd (View view, int scrollState);
			
		}
		
		private OnScrollListener listener;
		
		public void setListener (OnScrollListener listener) {
			this.listener = listener;
		}
		
		public GridView (Context context) {
			this (context, null);
		}
		
		public GridView (Context context, AttributeSet attrs) {
			this (context, attrs, 0);
		}
		
		public GridView (Context context, AttributeSet attrs, int defStyle) {
			
			super (context, attrs, defStyle);
			
			setOnScrollListener (new AbsListView.OnScrollListener () {
				
				@Override
				public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					loading = true;
				}
				
				@Override
				public void onScrollStateChanged (AbsListView view, int scrollState) {
					
					if (loading && !canScrollVertically (view, 1)) { // SUPPORT 14
						
						loading = false;
						if (listener != null) listener.onScrollBottomEnd (view, scrollState);
						
					}
					
				}
				
			});
			
		}
		
		public static boolean canScrollVertically (AbsListView absListView, int position) {
			
			if (OS.SDK >= 14)
				return absListView.canScrollVertically (position);
			else
				return absListView.getChildCount () > 0 && (
					absListView.getFirstVisiblePosition () > 0 ||
						absListView.getChildAt (0).getTop () < absListView.getPaddingTop ()
				);
			
		}
		
		private boolean expanded = true;
		
		public boolean isExpanded () {
			return expanded;
		}
		
		@Override
		public void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
			
			// HACK! TAKE THAT ANDROID!
			
			if (isExpanded ()) {
				
				// Calculate entire height by providing a very large height hint.
				// View.MEASURED_SIZE_MASK represents the largest height possible.
				
				int expandSpec = MeasureSpec.makeMeasureSpec (/*MEASURED_SIZE_MASK*/ 0x00ffffff, MeasureSpec.AT_MOST); // SUPPORT 11
				
				super.onMeasure (widthMeasureSpec, expandSpec);
				
				ViewGroup.LayoutParams params = getLayoutParams ();
				params.height = getMeasuredHeight ();
				
			} else super.onMeasure (widthMeasureSpec, heightMeasureSpec);
			
		}
		
		public void setExpanded (boolean expanded) {
			this.expanded = expanded;
		}
		
	}