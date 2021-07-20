	package ru.ointeractive.androdesign.widget;
	
	import android.content.Context;
	import android.content.res.TypedArray;
	import android.graphics.Color;
	import android.graphics.Paint;
	import android.graphics.drawable.ColorDrawable;
	import android.text.InputFilter;
	import android.util.AttributeSet;
	import android.view.View;
	import android.widget.EditText;
	
	import java.lang.reflect.Field;
	
	import ru.ointeractive.androdesign.R;
	
	public class NumberPicker extends android.widget.NumberPicker {
		
		private int minValue = 1;
		private int maxValue = 10;
		private int defaultValue = 1;
		private float textSize = 20.f;
		private int textColor = Color.BLACK, inactiveTextColor = Color.GRAY;
		private int backgroundColor = Color.WHITE;
		private int separatorColor = Color.TRANSPARENT;
		private Boolean enableFocusability;
		private boolean wrapSelectorWheel = false, enabled = true;
		
		public NumberPicker (Context context) {
			this (context, null);
		}
		
		public NumberPicker (Context context, AttributeSet attributeSet) {
			
			super (context, attributeSet);
			
			TypedArray a = context.obtainStyledAttributes (attributeSet, R.styleable.NumberPicker);
			
			for (int i = 0; i < a.getIndexCount (); ++i) {
				
				int attr = a.getIndex (i);
				
				if (attr == R.styleable.NumberPicker_minValue)
					setMinValue (a.getInt (attr, minValue));
				else if (attr == R.styleable.NumberPicker_maxValue)
					setMaxValue (a.getInt (attr, maxValue));
				else if (attr == R.styleable.NumberPicker_defaultValue)
					setValue (a.getInt (attr, defaultValue));
				else if (attr == R.styleable.NumberPicker_textSize)
					setTextSize (a.getDimension (attr, textSize));
				else if (attr == R.styleable.NumberPicker_textColor)
					setTextColor (a.getColor (attr, textColor));
				else if (attr == R.styleable.NumberPicker_inactiveTextColor)
					setInactiveTextColor (a.getColor (attr, inactiveTextColor));
				else if (attr == R.styleable.NumberPicker_separatorColor)
					setSeparatorColor (a.getColor (attr, separatorColor));
				else if (attr == R.styleable.NumberPicker_backgroundColor)
					setBackgroundColor (a.getColor (attr, backgroundColor));
				else if (attr == R.styleable.NumberPicker_focusable)
					setFocusability (a.getBoolean (attr, enableFocusability));
				else if (attr == R.styleable.NumberPicker_wrapSelector)
					setWrapSelectorWheel (a.getBoolean (attr, wrapSelectorWheel));
				else if (attr == R.styleable.NumberPicker_enabled)
					setEnabled (a.getBoolean (attr, enabled));
				else if (attr == R.styleable.NumberPicker_android_text)
					setText (a.getText (attr).toString (), a.getInt (R.styleable.NumberPicker_num, 3));
				
			}
			
			if (enableFocusability == null)
				setFocusability (false);
			
			a.recycle ();
			
			try {
				
				Field f = android.widget.NumberPicker.class.getDeclaredField ("mInputText");
				f.setAccessible (true);
				
				EditText inputText = (EditText) f.get (this);
				inputText.setFilters (new InputFilter[0]);
				
			} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
				e.printStackTrace ();
			}
			
			setOnValueChangedListener (new OnValueChangeListener () {
				
				@Override
				public void onValueChange (android.widget.NumberPicker picker, int oldVal, int newVal) {
					//restore ();
				}
				
			});
			
		}
		
		protected void restore () {
			
			for (int i = 0; i < getChildCount (); i++) {
				
				View child = getChildAt (i);
				
				if (child instanceof EditText) {
					
					try {
						
						EditText editText = ((EditText) child);
						
						editText.setTextColor (inactiveTextColor);
						
						editText.setTextSize (pixelsToSp (getContext (), textSize));
						
						Field selectorWheelPaintField = android.widget.NumberPicker.class.getDeclaredField ("mSelectorWheelPaint");
						selectorWheelPaintField.setAccessible (true);
						
						Paint wheelPaint = ((Paint) selectorWheelPaintField.get (this));
						
						wheelPaint.setColor (inactiveTextColor);
						wheelPaint.setTextSize (textSize);
						
						invalidate ();
						
						break;
						
					} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
						e.printStackTrace ();
					}
					
				}
				
			}
			
		}
		
		public int getTextColor () {
			return textColor;
		}
		
		public int getSeparatorColor () {
			return separatorColor;
		}
		
		public boolean isFocusabilityEnabled () {
			return enableFocusability;
		}
		
		/**
		 * Uses reflection to access divider private attribute and override its color
		 * Use Color.Transparent if you wish to hide them
		 */
		public void setSeparatorColor (int separatorColor) {
			
			this.separatorColor = separatorColor;
			
			Field[] pickerFields = android.widget.NumberPicker.class.getDeclaredFields ();
			
			for (Field pf : pickerFields) {
				
				if (pf.getName ().equals ("mSelectionDivider")) {
					
					pf.setAccessible (true);
					
					try {
						pf.set (this, new ColorDrawable (separatorColor));
					} catch (IllegalAccessException | IllegalArgumentException e) {
						e.printStackTrace ();
					}
					
					break;
				}
				
			}
			
		}
		
		/**
		 * Uses reflection to access text color private attribute for both wheel and edit text inside the number picker.
		 */
		public void setTextColor (int textColor) {
			
			this.textColor = textColor;
			updateTextAttributes ();
			
		}
		
		/**
		 * Uses reflection to access text color private attribute for both wheel and edit text inside the number picker.
		 */
		public void setInactiveTextColor (int textColor) {
			
			this.inactiveTextColor = textColor;
			updateTextAttributes ();
			
		}
		
		/**
		 * Uses reflection to access text size private attribute for both wheel and edit text inside the number picker.
		 */
		public void setTextSize (float textSize) {
			
			this.textSize = textSize;
			updateTextAttributes ();
			
		}
		
		private void updateTextAttributes () {
			
			for (int i = 0; i < getChildCount (); i++) {
				
				View child = getChildAt (i);
				
				if (child instanceof EditText) {
					
					try {
						
						EditText editText = ((EditText) child);
						
						/*editText.setTextColor (new ColorStateList (
							
							new int[][] {
								new int[] {  android.R.attr.state_selected },
								new int[] { -android.R.attr.state_selected }
							},
							
							new int[] { textColor, textColor }
							
						));*/
						
						editText.setTextColor (textColor);
						
						editText.setTextSize (pixelsToSp (getContext (), textSize));
						
						Field selectorWheelPaintField = android.widget.NumberPicker.class.getDeclaredField ("mSelectorWheelPaint");
						selectorWheelPaintField.setAccessible (true);
						
						Paint wheelPaint = ((Paint) selectorWheelPaintField.get (this));
						
						wheelPaint.setColor (inactiveTextColor);
						wheelPaint.setTextSize (textSize);
						
						invalidate ();
						
						break;
						
					} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
						e.printStackTrace ();
					}
					
				}
				
			}
			
		}
		
		private NumberPicker setFocusability (boolean isFocusable) {
			
			enableFocusability = isFocusable;
			setDescendantFocusability (isFocusable ? FOCUS_AFTER_DESCENDANTS : FOCUS_BLOCK_DESCENDANTS);
			
			return this;
			
		}
		
		private float pixelsToSp (Context context, float px) {
			return px / context.getResources ().getDisplayMetrics ().scaledDensity;
		}
		
		private float spToPixels (Context context, float sp) {
			return sp * context.getResources ().getDisplayMetrics ().scaledDensity;
		}
		
		public NumberPicker setText (String text) {
			return setText (text, 3);
		}
		
		public NumberPicker setText (String text, int num) {
			
			setEnabled (false);
			
			if (num == 0) num = 1;
			
			if (num > 1) {
				
				setMinValue (0);
				setMaxValue (2);
				setValue (1);
				
			}
			
			String[] values = new String[num];
			
			for (int i = 0; i < num; i++)
				values[i] = text;
			
			setDisplayedValues (values);
			
			return this;
			
		}
		
	}