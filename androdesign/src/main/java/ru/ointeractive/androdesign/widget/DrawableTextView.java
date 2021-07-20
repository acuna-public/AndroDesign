	package ru.ointeractive.androdesign.widget;
	
	import android.content.Context;
	import android.content.res.ColorStateList;
	import android.content.res.TypedArray;
	import android.graphics.Color;
	import android.graphics.drawable.Drawable;
	import android.util.AttributeSet;
	import android.view.ViewGroup;
	import android.widget.ImageView;
	import android.widget.LinearLayout;
	import android.widget.TextView;
	
	import ru.ointeractive.androdesign.R;
	
	public class DrawableTextView extends LinearLayout {
		
		private int mPadding = 10, mWidth = 30, mHeight = 30;
		private float mTextSize = 15;
		
		private final ImageView imageView;
		private final TextView textView;
		
		public DrawableTextView (Context context) {
			this (context, null);
		}
		
		public DrawableTextView (Context context, AttributeSet attrs) {
			this (context, attrs, 0);
		}
		
		public DrawableTextView (Context context, AttributeSet attrs, int defStyle) {
			
			super (context, attrs, defStyle);
			
			setLayoutParams (new LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			setOrientation (LinearLayout.HORIZONTAL);
			
			imageView = new ImageView (getContext ());
			
			imageView.setPadding (0, 0, mPadding, 0);
			
			imageView.setLayoutParams (new LayoutParams (mWidth, mHeight));
			
			addView (imageView);
			
			textView = new TextView (getContext ());
			
			addView (textView);
			
			TypedArray array = context.obtainStyledAttributes (attrs, R.styleable.DrawableTextView);
			
			for (int i = 0; i < array.getIndexCount (); ++i) {
				
				int attr = array.getIndex (i);
				
				if (attr == R.styleable.DrawableTextView_android_text)
					setText (array.getText (attr).toString ());
				else if (attr == R.styleable.DrawableTextView_android_textSize)
					setTextSize (array.getDimension (attr, mTextSize));
				else if (attr == R.styleable.DrawableTextView_src)
					setImage (array.getDrawable (attr));
				else if (attr == R.styleable.DrawableTextView_android_textAppearance)
					setTextAppearance (array.getResourceId (attr, 0));
				else if (attr == R.styleable.DrawableTextView_android_textColor)
					setTextColor (context.getResources ().getColor (array.getResourceId (attr, 0)));
				
			}
			
			array.recycle ();
			
		}
		
		public DrawableTextView setText (CharSequence text) {
			
			textView.setText (text);
			return this;
			
		}
		
		public DrawableTextView setTextSize (float size) {
			
			textView.setTextSize (size);
			return this;
			
		}
		
		public DrawableTextView setTextColor (int color) {
			
			textView.setTextColor (color);
			return this;
			
		}
		
		public DrawableTextView setImage (Drawable drawable) {
			
			imageView.setImageDrawable (drawable);
			return this;
			
		}
		
		public DrawableTextView setTextAppearance (int res) {
			
			textView.setTextAppearance (getContext (), res);
			return this;
			
		}
		
		public DrawableTextView setImageTint (int color) {
			
			int[][] states = new int[][] {
				new int[] { android.R.attr.state_enabled}, // enabled
				new int[] {-android.R.attr.state_enabled}, // disabled
				new int[] {-android.R.attr.state_checked}, // unchecked
				new int[] { android.R.attr.state_pressed}  // pressed
			};
			
			int[] colors = new int[] {
				Color.BLACK,
				Color.RED,
				Color.GREEN,
				Color.BLUE
			};
			
			imageView.setColorFilter (color, android.graphics.PorterDuff.Mode.SRC_IN);
			
			return this;
			
		}
		
		/*public DrawableTextView setImageTint (int color) {
			
			/*int[][] states = new int[][] {
				new int[] { android.R.attr.state_enabled}, // enabled
				new int[] {-android.R.attr.state_enabled}, // disabled
				new int[] {-android.R.attr.state_checked}, // unchecked
				new int[] { android.R.attr.state_pressed}  // pressed
			};
			
			int[] colors = new int[] {
				Color.BLACK,
				Color.RED,
				Color.GREEN,
				Color.BLUE
			};
			
			return setImageTint (ColorStateList.valueOf (color));
			
		}
		
		public DrawableTextView setImageTint (ColorStateList colors) {
			
			ImageViewCompat.setImageTintList (imageView, colors);
			
			return this;
			
		}*/
		
	}