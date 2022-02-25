	package ru.ointeractive.androdesign.widget;
	
	import android.content.Context;
	import android.graphics.Rect;
	import android.support.v7.widget.AppCompatEditText;
	import android.text.Editable;
	import android.text.InputFilter;
	import android.text.InputType;
	import android.text.TextWatcher;
	import android.util.AttributeSet;
	import android.widget.EditText;
	
	import ru.ointeractive.androdesign.R;
	import upl.core.Int;
	
	public class CurrencyEditText extends AppCompatEditText {
		
		private static String cleanString = "";
		private static final int MAX_LENGTH = 20;
		
		public CurrencyEditText (Context context) {
			this (context, null);
		}
		
		public CurrencyEditText (Context context, AttributeSet attrs) {
			this (context, attrs, R.attr.editTextStyle);
		}
		
		public CurrencyEditText (Context context, AttributeSet attrs, int defStyleAttr) {
			
			super (context, attrs, defStyleAttr);
			
			setInputType (InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			setHint (Int.mPrefix);
			setFilters (new InputFilter[] {new InputFilter.LengthFilter (MAX_LENGTH)});
			
			addTextChangedListener (new CurrencyTextWatcher (this));
			
		}
		
		@Override
		protected void onFocusChanged (boolean focused, int direction, Rect previouslyFocusedRect) {
			
			super.onFocusChanged (focused, direction, previouslyFocusedRect);
			
			if (focused) {
				if (getText ().toString ().isEmpty ()) setText (Int.mPrefix);
			} else if (getText ().toString ().equals (Int.mPrefix))
				setText ("");
			
		}
		
		private static class CurrencyTextWatcher implements TextWatcher {
			
			private final EditText mEditText;
			private String previousCleanString;
			
			CurrencyTextWatcher (EditText editText) {
				mEditText = editText;
			}
			
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void afterTextChanged (Editable editable) {
				
				String str = editable.toString ();
				
				if (Int.size (str) < Int.size (Int.mPrefix)) {
					
					mEditText.setText (Int.mPrefix);
					mEditText.setSelection (Int.size (Int.mPrefix));
					
					return;
					
				}
				
				if (str.equals (Int.mPrefix)) {
					return;
				}
				// cleanString this the string which not contain prefix and ,
				cleanString = str.replace (Int.mPrefix, "").replaceAll ("[\\s]", "");
				// for prevent afterTextChanged recursive call
				if (cleanString.equals (previousCleanString) || cleanString.isEmpty ())
					return;
				
				previousCleanString = cleanString;
				
				mEditText.setText (Int.format (cleanString));
				mEditText.setSelection (Math.min (mEditText.getText ().length (), MAX_LENGTH));
				
			}
			
		}
		
		public String toString () {
			return Int.prepare (cleanString);
		}
		
	}