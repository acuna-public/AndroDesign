	package pro.acuna.androdesign.widget;
	/*
	 Created by Acuna on 21.01.2019
	*/
	
	import android.content.Context;
  import android.support.annotation.NonNull;
  import android.util.AttributeSet;
	import android.view.View;
	
	public class RecyclerView extends android.support.v7.widget.RecyclerView {
		
		public interface OnScrollListener {
			
			void onScrollBottomEnd (View recyclerView, int scrollState);
			
		}
		
		private OnScrollListener listener;
		
		public void setListener (OnScrollListener listener) {
			this.listener = listener;
		}
		
		public RecyclerView (Context context) {
			
			super (context);
			init ();
			
		}
		
		public RecyclerView (Context context, AttributeSet attrs) {
			
			super (context, attrs);
			init ();
			
		}
		
		public RecyclerView (Context context, AttributeSet attrs, int defStyle) {
			
			super (context, attrs, defStyle);
			init ();
			
		}
		
		private boolean loading = false;
		
		private void init () {
			
			addOnScrollListener (new android.support.v7.widget.RecyclerView.OnScrollListener () {
				
				@Override
				public void onScrolled (@NonNull android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
					if (dy > 0) loading = true;
				}
				
				@Override
				public void onScrollStateChanged (@NonNull android.support.v7.widget.RecyclerView recyclerView, int scrollState) {
					
					if (loading && !recyclerView.canScrollVertically (1)) {
						
						loading = false;
						if (listener != null) listener.onScrollBottomEnd (recyclerView, scrollState);
						
					}
					
				}
				
			});
			
		}
		
	}