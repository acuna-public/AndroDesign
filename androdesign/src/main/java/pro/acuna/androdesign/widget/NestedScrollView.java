	package pro.acuna.androdesign.widget;
	/*
	 Created by Acuna on 21.01.2019
	*/
	
	import android.content.Context;
	import android.util.AttributeSet;
	import android.view.View;
	
	public class NestedScrollView extends android.support.v4.widget.NestedScrollView {
		
		public interface OnScrollListener {
			
			void onScrollBottomEnd (View view);
			
		}
		
		private OnScrollListener listener;
		
		public void setListener (OnScrollListener listener) {
			this.listener = listener;
		}
		
		public NestedScrollView (Context context) {
			
			super (context);
			init ();
			
		}
		
		public NestedScrollView (Context context, AttributeSet attrs) {
			
			super (context, attrs);
			init ();
			
		}
		
		public NestedScrollView (Context context, AttributeSet attrs, int defStyle) {
			
			super (context, attrs, defStyle);
			init ();
			
		}
		
		private boolean loading = false;
		
		private void init () {
			
			setOnScrollChangeListener (new android.support.v4.widget.NestedScrollView.OnScrollChangeListener () {
				
				@Override
				public void onScrollChange (android.support.v4.widget.NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
					
					if (scrollY > oldScrollY) {
						//Log.i (TAG, "Scroll DOWN");
					}
					if (scrollY < oldScrollY) {
						//Log.i (TAG, "Scroll UP");
					}
					
					if (scrollY == 0) {
						//Log.i (TAG, "TOP SCROLL");
					}
					
					if (loading && scrollY == (view.getChildAt (0).getMeasuredHeight () - view.getMeasuredHeight ())) {
						
						loading = false;
						if (listener != null) listener.onScrollBottomEnd (view);
						
					} else loading = true;
					
				}
				
			});
			
		}
		
	}