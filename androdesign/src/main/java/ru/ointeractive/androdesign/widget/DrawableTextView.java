	package ru.ointeractive.androdesign.widget;
	
	import android.content.Context;
	import android.content.res.TypedArray;
	import android.graphics.Color;
	import android.graphics.drawable.Drawable;
	import android.util.AttributeSet;
	import android.view.Gravity;
	import android.view.ViewGroup;
	import android.widget.ImageView;
	import android.widget.LinearLayout;
	import android.widget.TextView;
	
	import ru.ointeractive.androdesign.R;
	
	public class DrawableTextView extends LinearLayout {
		
		private int mPadding = 15, mWidth = 25, mHeight = 25, mTextSize = 15;
		
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
			
			textView = new TextView (getContext ());
			
			imageView = new ImageView (getContext ());
			
			TypedArray array = context.obtainStyledAttributes (attrs, R.styleable.DrawableTextView);
			
			for (int i = 0; i < array.getIndexCount (); ++i) {
				
				int attr = array.getIndex (i);
				
				if (attr == R.styleable.DrawableTextView_android_text)
					setText (array.getText (attr).toString ());
				else if (attr == R.styleable.DrawableTextView_android_textSize)
					setTextSize (array.getDimensionPixelSize (attr, mTextSize));
				else if (attr == R.styleable.DrawableTextView_src)
					setImage (array.getDrawable (attr));
				else if (attr == R.styleable.DrawableTextView_android_textAppearance)
					setTextAppearance (array.getResourceId (attr, 0));
				else if (attr == R.styleable.DrawableTextView_android_textColor)
					setTextColor (context.getResources ().getColor (array.getResourceId (attr, 0)));
				else if (attr == R.styleable.DrawableTextView_imageWidth)
					setImageWidth (array.getDimensionPixelSize (attr, mWidth));
				else if (attr == R.styleable.DrawableTextView_imageHeight)
					setImageHeight (array.getDimensionPixelSize (attr, mHeight));
				
			}
			
			array.recycle ();
			
			setLayoutParams (new LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			setOrientation (LinearLayout.HORIZONTAL);
			
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams ((int) mWidth, (int) mHeight);
			
			layoutParams.gravity = Gravity.CENTER;
			
			imageView.setLayoutParams (layoutParams);
			
			addView (imageView);
			
			textView.setPadding ((int) mPadding, 0, 0, 0);
			
			addView (textView);
			
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
		
		public DrawableTextView setImageWidth (int width) {
			
			mWidth = width;
			
			return this;
			
		}
		
		public DrawableTextView setImageHeight (int height) {
			
			mHeight = height;
			
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