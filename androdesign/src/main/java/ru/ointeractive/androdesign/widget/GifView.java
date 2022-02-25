	package ru.ointeractive.androdesign.widget;
	
	import android.content.Context;
	import android.graphics.Canvas;
	import android.graphics.Color;
	import android.graphics.Movie;
	import android.os.SystemClock;
	import android.view.View;
	
	import java.io.InputStream;
	
	public class GifView extends View {
		
		private Movie mMovie;
		private long mMoviestart;
		private int mMovieWidth, mMovieHeight;
		private long mMovieDuration;
		
		public GifView (Context context, int resId) {
			this (context, context.getResources ().openRawResource (resId));
		}
		
		public GifView (Context context, InputStream stream) {
			
			super (context);
			
			mMovie = Movie.decodeStream (stream);
			
			mMovieWidth = mMovie.width ();
			mMovieHeight = mMovie.height ();
			mMovieDuration = mMovie.duration ();
			
		}
		
		@Override
		protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
			setMeasuredDimension (mMovieWidth, mMovieHeight);
		}
		
		public int getMovieWidth () {
			return mMovieWidth;
		}
		
		public int getMovieHeight () {
			return mMovieHeight;
		}
		
		public long getMovieDuration () {
			return mMovieDuration;
		}
		
		@Override
		protected void onDraw (Canvas canvas) {
			
			canvas.drawColor (Color.TRANSPARENT);
			
			super.onDraw (canvas);
			
			long now = SystemClock.uptimeMillis ();
			
			if (mMoviestart == 0)
				mMoviestart = now;
			
			int relTime = (int) ((now - mMoviestart) % mMovie.duration ());
			
			mMovie.setTime (relTime);
			mMovie.draw (canvas, 0, 0);
			
			this.invalidate ();
			
		}
		
	}