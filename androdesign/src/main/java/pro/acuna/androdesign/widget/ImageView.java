	package pro.acuna.androdesign.widget;
	/*
	 Created by Acuna on 21.01.2019
	*/
	
	import android.content.Context;
	import android.os.Build;
	import android.support.v7.widget.AppCompatImageView;
	import android.util.AttributeSet;
	import android.view.ViewTreeObserver;
	
	public class ImageView extends AppCompatImageView {
		
		public interface OnImageListener {
			
			void onView ();
			
		}
		
		private OnImageListener listener;
		
		public void setListener (OnImageListener listener) {
			this.listener = listener;
		}
		
		public ImageView (Context context) {
			
			super (context);
			init ();
			
		}
		
		public ImageView (Context context, AttributeSet attrs) {
			
			super (context, attrs);
			init ();
			
		}
		
		public ImageView (Context context, AttributeSet attrs, int defStyle) {
			
			super (context, attrs, defStyle);
			init ();
			
		}
		
		private void init () {
			
			getViewTreeObserver ().addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener () {
				
				@Override
				public void onGlobalLayout () {
					
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
						getViewTreeObserver ().removeGlobalOnLayoutListener (this);
					else
						getViewTreeObserver ().removeOnGlobalLayoutListener (this);
					
					if (listener != null) listener.onView ();
					
				}
				
			});
			
		}
		
	}