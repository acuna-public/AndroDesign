	package pro.acuna.androdesign.widget;
	/*
	 Created by Acuna on 04.09.2018
	*/
	
	import android.content.Context;
	import android.content.res.TypedArray;
	import android.graphics.Typeface;
	import android.graphics.drawable.ColorDrawable;
	import android.graphics.drawable.Drawable;
	import android.os.Build;
	import android.text.Layout;
	import android.text.Spannable;
	import android.text.SpannableStringBuilder;
	import android.text.Spanned;
	import android.text.StaticLayout;
	import android.text.TextPaint;
	import android.text.method.MovementMethod;
	import android.text.style.ImageSpan;
	import android.util.AttributeSet;
	import android.util.DisplayMetrics;
	import android.view.Gravity;
	import android.view.ViewTreeObserver;
	
	import java.io.IOException;
	import java.io.InputStream;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	
	import pro.acuna.androdesign.R;
	import pro.acuna.andromeda.Device;
	import pro.acuna.andromeda.Log;
	import pro.acuna.jabadaba.Arrays;
	import pro.acuna.jabadaba.Int;
	
	public class ExTextView extends LinearLayout {
		
		public int page = 0;
		public TextView[] textViews = new TextView[0];
		
		private Layout spaceLayout;
		private int spaceWidth;
		
		private TypedArray attrs;
		
		public ExTextView (Context context) {
			
			super (context);
			init (null, 0);
			
		}
		
		public ExTextView (Context context, AttributeSet attrs) {
			
			super (context, attrs);
			init (attrs, 0);
			
		}
		
		public ExTextView (Context context, AttributeSet attrs, int defStyle) {
			
			super (context, attrs, defStyle);
			init (attrs, defStyle);
			
		}
		
		public interface OnTextListener {
			
			void onTextView (int num);
			
		}
		
		private OnTextListener listener;
		
		public void setListener (OnTextListener listener) {
			this.listener = listener;
		}
		
		private void init (AttributeSet attrSet, int defStyle) {
			
			attrs = getContext ().obtainStyledAttributes (attrSet, R.styleable.TextView, defStyle, 0);
			
			spaceLayout = new StaticLayout (" ", new TextPaint (), 10, Layout.Alignment.ALIGN_NORMAL, 0, 1, false);
			spaceWidth = (int) spaceLayout.getLineWidth (0);
			
		}
		
		private enum Options {
			
			BackgroundDrawable,
			TextSize,
			TextSizeUnit,
			TextColor,
			FontFaceAsset,
			FontFamily,
			FontStyle,
			BgColor,
			Paddings,
			LineSpacing,
			MovementMethod,
			TextAppearance,
			TouchListener,
			TextGravity,
			TextSelectable,
			
		}
		
		private Map<Options, Object> options = new HashMap<> ();
		
		public void setTextSize (float size) {
			options.put (Options.TextSize, size);
		}
		
		public void setTextColor (int color) {
			options.put (Options.TextColor, color);
		}
		
		public void setTextSize (int unit, float size) {
			
			options.put (Options.TextSizeUnit, unit);
			setTextSize (size);
			
		}
		
		public void setTypeface (Typeface typeface) {
			options.put (Options.FontFaceAsset, typeface);
		}
		
		public void setTypeface (Typeface family, int style) {
			
			options.put (Options.FontFamily, family);
			options.put (Options.FontStyle, style);
			
		}
		
		@Override
		public void setBackground (Drawable drawable) {
			options.put (Options.BackgroundDrawable, drawable);
		}
		
		public void setBackground (InputStream stream) throws IOException {
			setBackground (Drawable.createFromStream (stream, null));
		}
		
		@Override
		public void setBackgroundColor (int color) {
			options.put (Options.BgColor, color);
		}
		
		public void setPadding (Integer... paddings) {
			options.put (Options.Paddings, paddings);
		}
		
		public void setLineSpacing (int spacing) {
			options.put (Options.LineSpacing, spacing);
		}
		
		public void setMovementMethod (MovementMethod method) {
			options.put (Options.MovementMethod, method);
		}
		
		public void setTextAppearance (int style) {
			options.put (Options.TextAppearance, style);
		}
		
		public void setGravity (int gravity) {
			options.put (Options.TextGravity, gravity);
		}
		
		public void setTextIsSelectable (boolean selectable) {
			options.put (Options.TextSelectable, selectable);
		}
		
		@Override
		public void setOnTouchListener (OnTouchListener listener) {
			options.put (Options.TouchListener, listener);
		}
		
		public ExTextView setText (int resource) {
			return setText (getContext ().getString (resource));
		}
		
		public ExTextView setText (CharSequence... texts) {
			
			++page;
			textViews = new TextView[Int.size (texts)];
			
			DisplayMetrics dm = Device.getDisplayMetrics (getContext ());
			
			if (getChildCount () == 0) {
				
				for (int i = 0; i < Int.size (texts); ++i) {
					
					final int num = i;
					final TextView textView = new TextView (getContext ());
					
					String textApp = attrs.getString (R.styleable.TextView_textAppearance);
					
					int textAppearance = 0;
					
					if (textApp != null)
						textAppearance = Integer.valueOf (textApp.replace ("@", ""));
					
					if (textAppearance > 0)
						setTextAppearance (textAppearance);
					
					textView.setText (texts[i]);
					
					int textGravity = Gravity.LEFT;
					
					if (options.get (Options.TextGravity) != null)
						textGravity = (int) options.get (Options.TextGravity);
					else
						textGravity = attrs.getInt (R.styleable.TextView_gravity, textGravity);
					
					switch (textGravity) {
						
						default:
							textView.setGravity (textGravity);
							break;
							
						case Gravity.FILL: {
							
							textView.getViewTreeObserver ().addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener () {
								
								@Override
								public void onGlobalLayout () {
									
									if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
										getViewTreeObserver ().removeGlobalOnLayoutListener (this);
									else
										getViewTreeObserver ().removeOnGlobalLayoutListener (this);
									
									Layout layout = textView.getLayout ();
									
									textView.setText ("");
									
									for (int line = 0; line < layout.getLineCount (); ++line) {
										
										int lineOffsetEnd = layout.getLineEnd (line);
										CharSequence lineText = layout.getText ().subSequence (layout.getLineStart (line), lineOffsetEnd);
										
										List<CharSequence> words = Arrays.explode (" ", lineText);
										
										int spaceCount = (Int.size (words) - 2);
										if (spaceCount <= 0) spaceCount = 1;
										
										int removeSpaceWidth = ((int) layout.getLineWidth (line) - (spaceWidth * spaceCount));
										int eachSpaceWidth = ((textView.getWidth () - 40 - removeSpaceWidth) / spaceCount);
										
										//Log.w (lineText, spaceWidth, spaceCount, layout.getLineWidth (line), textView.getWidth () - 40, removeSpaceWidth, eachSpaceWidth);
										
										for (int word = 0; word < Int.size (words); ++word) {
											
											Spanned text = (Spanned) words.get (word);
											
											if (Int.size (text) > 0) {
												
												if (word > 0) {
													
													SpannableStringBuilder newLineText = new SpannableStringBuilder (spaceLayout.getText ());
													
													Drawable drawable = new ColorDrawable (0x00ffffff);
													drawable.setBounds (0, 0, eachSpaceWidth, 0);
													
													newLineText.setSpan (new ImageSpan (drawable), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
													
													textView.append (newLineText);
													
												}
												
												textView.append (text);
												
											} else textView.append ("\n");
											
										}
										
									}
									
								}
								
							});
							
							break;
							
						}
						
					}
					
					if (options.get (Options.TextColor) != null)
						textView.setTextColor ((int) options.get (Options.TextColor));
					
					if (options.get (Options.TextAppearance) != null)
						textView.setTextAppearance (getContext (), (int) options.get (Options.TextAppearance));
					
					if (options.get (Options.LineSpacing) != null)
						textView.setLineSpacing ((int) options.get (Options.LineSpacing), 1);
					
					if (options.get (Options.BackgroundDrawable) != null)
						textView.setBackground ((Drawable) options.get (Options.BackgroundDrawable));
					
					if (options.get (Options.FontFaceAsset) != null)
						textView.setTypeface ((Typeface) options.get (Options.FontFaceAsset));
					
					if (options.get (Options.BgColor) != null)
						textView.setBackgroundColor ((int) options.get (Options.BgColor));
					
					if (options.get (Options.Paddings) != null) {
						
						Integer[] paddings = (Integer[]) options.get (Options.Paddings);
						
						if (Int.size (paddings) == 1)
							textView.setPadding (paddings[0], paddings[0], paddings[0], paddings[0]);
						else
							textView.setPadding (paddings[0], paddings[1], paddings[2], paddings[3]);
						
					}
					
					if (options.get (Options.FontFamily) != null)
						textView.setTypeface ((Typeface) options.get (Options.FontFamily), (int) options.get (Options.FontStyle));
					
					if (options.get (Options.MovementMethod) != null)
						textView.setMovementMethod ((MovementMethod) options.get (Options.MovementMethod));
					
					if (options.get (Options.TextSize) != null) {
						
						if (options.get (Options.TextSizeUnit) != null)
							textView.setTextSize ((int) options.get (Options.TextSizeUnit), (float) options.get (Options.TextSize));
						else
							textView.setTextSize ((float) options.get (Options.TextSize));
						
					}
					
					if (options.get (Options.TouchListener) != null)
						textView.setOnTouchListener ((OnTouchListener) options.get (Options.TouchListener));
					
					textView.setLinksClickable (true);
					
					textView.setLayoutParams (new LinearLayout.LayoutParams (LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					
					if (Int.size (texts) > 1)
						textView.getLayoutParams ().width = (dm.widthPixels / Int.size (texts));
					
					//textView.getLayoutParams ().height = LayoutParams.FILL_PARENT;
					
					textView.setListener (new TextView.Listener () {
						
						@Override
						public void onView () {
							if (listener != null) listener.onTextView (num);
						}
						
					});
					
					if (options.get (Options.TextGravity) != null)
						textView.setTextIsSelectable ((boolean) options.get (Options.TextSelectable));
					else
						textView.setTextIsSelectable (attrs.getBoolean (R.styleable.TextView_textSelectable, false));
					
					textViews[i] = textView;
					
					addView (textView);
					
				}
				
			} else {
				
				for (int i = 0; i < getChildCount (); ++i) {
					
					TextView textView = (TextView) getChildAt (i);
					textView.setText (texts[i]);
					
					textViews[i] = textView;
					
				}
				
			}
			
			return this;
			
		}
		
	}