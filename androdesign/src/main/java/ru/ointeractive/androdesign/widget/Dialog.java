	package ru.ointeractive.androdesign.widget;
	
	import android.app.Activity;
	import android.graphics.Color;
	import android.graphics.drawable.ColorDrawable;
	import android.os.Bundle;
	import android.view.Window;
	
	public class Dialog extends android.app.Dialog {
		
		public Activity mActivity;
		public final int mLayout;
		public Listener mListener;
		public final boolean mCanceable;
		
		public interface Listener {
			
			void onShow (Dialog dialog);
			
		}
		
		public Dialog (Activity activity, int layout) {
			this (activity, layout, null);
		}
		
		public Dialog (Activity activity, int layout, Listener listener) {
			this (activity, layout, listener, true);
		}
		
		public Dialog (Activity activity, int layout, Listener listener, boolean canceable) {
			
			super (activity);
			
			mActivity = activity;
			mLayout = layout;
			mListener = listener;
			mCanceable = canceable;
			
		}
		
		public void setListener (Listener listener) {
			mListener = listener;
		}
		
		@Override
		protected void onCreate (Bundle savedInstanceState) {
			
			super.onCreate (savedInstanceState);
			
			requestWindowFeature (Window.FEATURE_NO_TITLE);
			
			getWindow ().setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));
			
			setContentView (mLayout);
			setCancelable (mCanceable);
			
			if (mListener != null) mListener.onShow (this);
			
		}
		
	}