	package pro.acuna.androdesign;
	/*
	 Created by Acuna on 10.07.2018
	*/
	
	import android.app.Activity;
	import android.app.AlertDialog;
	import android.app.ProgressDialog;
	import android.content.Context;
	import android.content.DialogInterface;
	import android.content.Intent;
	import android.os.AsyncTask;
	import android.os.Build;
	import android.os.Handler;
	import android.support.annotation.NonNull;
	import android.text.Html;
	import android.text.Spannable;
	import android.text.Spanned;
	import android.text.TextPaint;
	import android.text.TextUtils;
	import android.text.style.URLSpan;
	import android.text.style.UnderlineSpan;
	import android.util.DisplayMetrics;
	import android.view.LayoutInflater;
	import android.view.View;
	import android.view.ViewGroup;
	import android.widget.ArrayAdapter;
	import android.widget.CheckBox;
	import android.widget.ImageView;
	import android.widget.LinearLayout;
	import android.widget.Spinner;
	import android.widget.TextView;
	
	import com.larswerkman.holocolorpicker.ColorPicker;
	import com.larswerkman.holocolorpicker.SaturationBar;
	import com.larswerkman.holocolorpicker.ValueBar;
	
	import java.io.File;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Map;
	
	import pro.acuna.androdesign.widget.ListItem;
	import pro.acuna.andromeda.AppsManager;
	import pro.acuna.andromeda.Device;
	import pro.acuna.andromeda.OS;
	import pro.acuna.andromeda.System;
	import pro.acuna.jabadaba.Arrays;
	import pro.acuna.jabadaba.Console;
	import pro.acuna.jabadaba.Files;
	import pro.acuna.jabadaba.Int;
	import pro.acuna.jabadaba.Net;
	import pro.acuna.jabadaba.exceptions.HttpRequestException;
	
	public class UI {
		
		/*public static void debug (Activity activity, String msg) {
			dialog (activity, 0, msg);
		}
		
		public static void debug (Activity activity, Exception e) {
			dialog (activity, 0, etoString ());
		}
		
		public static void debug (Activity activity, Integer[] msg) {
			dialog (activity, 0, Arrays.implode (", ", msg));
		}
		
		public static void debug (Activity activity, String[] msg) {
			debug (activity, msg, "\n");
		}
		
		public static void debug (Activity activity, String[] msg, String sep) {
			dialog (activity, 0, Arrays.implode (sep, msg));
		}
		
		public static void debug (Activity activity, List<?> msg) {
			debug (activity, msg, "\n");
		}
		
		public static void debug (Activity activity, List<?> msg, String sep) {
			dialog (activity, 0, Arrays.implode (sep, msg));
		}
		
		public static void debug (Activity activity, int msg) {
			debug (activity, String.valueOf (msg));
		}
		
		public static void debug (Activity activity, boolean msg) {
			debug (activity, String.valueOf (msg));
		}*/
		
		public static DisplayMetrics getScreenData (Context context) {
			return context.getResources ().getDisplayMetrics ();
		}
		
		public static String getFieldText (View title) {
			return getFieldText ((TextView) title);
		}
		
		public static String getFieldText (TextView title) {
			return title.getText ().toString ();
		}
		
		public static int getFieldId (Spinner spinner) {
			return spinner.getSelectedItemPosition ();
		}
		
		public static String getFieldText (Spinner spinner) {
			return spinner.getSelectedItem ().toString ();
		}
		
		public static boolean checkFieldText (TextView title) {
			return !TextUtils.isEmpty (getFieldText (title));
		}
		
		public static boolean checkFieldUrl (TextView title) {
			return Net.isUrl (getFieldText (title));
		}
		
		public static boolean checkField (Context context, View title, View titleCheck, int errorMess) {
			return checkField (context, (TextView) title, titleCheck, errorMess);
		}
		
		public static boolean checkField (Context context, TextView title, View titleCheck, int errorMess) {
			return checkField (context, title, (TextView) titleCheck, errorMess);
		}
		
		public static boolean checkField (Context context, View title, TextView titleCheck, int errorMess) {
			return checkField (context, (TextView) title, titleCheck, errorMess);
		}
		
		public static boolean checkField (Context context, TextView title, TextView titleCheck, int errorMess) {
			return checkField (title, titleCheck, context.getString (errorMess));
		}
		
		public static boolean checkField (View title, View titleCheck, String errorMess) {
			return checkField ((TextView) title, (TextView) titleCheck, errorMess);
		}
		
		public static boolean checkField (TextView title, TextView titleCheck, String errorMess) {
			return checkField (title, titleCheck, errorMess, true);
		}
		
		public static boolean checkField (Context context, TextView title, View titleCheck, int errorMess, boolean forced) {
			return checkField (context, title, (TextView) titleCheck, errorMess, forced);
		}
		
		public static boolean checkField (Context context, TextView title, TextView titleCheck, int errorMess, boolean forced) {
			return checkField (title, titleCheck, context.getString (errorMess), forced);
		}
		
		public static boolean checkField (TextView title, TextView titleCheck, String errorMess, boolean forced) {
			
			boolean error = (
				
				forced && (
					(title.getInputType () == 17 && !checkFieldUrl (title))
					||
					!checkFieldText (title)
				)
				
			);
			
			return checkField (error, titleCheck, errorMess);
			
		}
		
		public static boolean checkField (Context context, boolean error, View titleCheck, int errorMess) {
			return checkField (context, error, (TextView) titleCheck, errorMess);
		}
		
		public static boolean checkField (Context context, boolean error, TextView titleCheck, int errorMess) {
			return checkField (error, titleCheck, context.getString (errorMess));
		}
		
		public static boolean checkField (boolean error, TextView titleCheck, String errorMess) {
			
			if (error) {
				
				titleCheck.setVisibility (View.VISIBLE);
				titleCheck.setText (errorMess);
				
			} else {
				
				titleCheck.setVisibility (View.INVISIBLE);
				titleCheck.setText ("");
				
			}
			
			return !error;
			
		}
		
		public static int getResourceId (Context context, String pResourceName, String pVariableName) {
			return getResourceId (context, pVariableName, pResourceName, context.getPackageName ());
		}
		
		public static int getResourceId (Context context, String pResourceName, String pVariableName, String pPackageName) {
			return context.getResources ().getIdentifier (pVariableName, pResourceName, pPackageName);
		}
		
		public static Spannable stripUnderlines (Spanned string) {
			
			Spannable str = (Spannable) string;
			
			for (URLSpan span : str.getSpans (0, Int.size (str), URLSpan.class)) {
				
				str.setSpan (new UnderlineSpan () {
					
					@Override
					public void updateDrawState (TextPaint tp) {
						tp.setUnderlineText (false);
					}
					
				}, str.getSpanStart (span), str.getSpanEnd (span), 0);
				
			}
			
			return str;
			
		}
		
		@SuppressWarnings ("deprecation")
		public static Spanned toSpanned (String source) {
			
			if (Build.VERSION.SDK_INT >= 24)
				return Html.fromHtml (source, Html.FROM_HTML_MODE_LEGACY);
			else
				return Html.fromHtml (source);
			
		}
		
		public static Runnable textLoading (Handler handler, TextView textView) {
			return textLoading (handler, textView, 20);
		}
		
		public static Runnable textLoading (Handler handler, TextView textView, int speed) {
			return textLoading (handler, textView, speed, new String[] { "|", "/", "-", "\\" });
		}
		
		public static Runnable textLoading (Handler handler, TextView textView, String item, int speed) {
			return textLoading (handler, textView, speed, new String[] { item });
		}
		
		public static Runnable textLoading (final Handler handler, final TextView textView, final int speed, final String[] items) {
			
			final String text = textView.getText ().toString ();
			
			Runnable runnable = new Runnable () {
				
				private int i = 0;
				private String content = "";
				
				@Override
				public void run () {
					
					if (Int.size (items) > 1) {
						
						content += (!text.equals ("") ? " " : "") + items[i];
						if (i >= (Int.size (items) - 1)) i = 0; else ++i;
						
					} else content += items[i];
					
					textView.setText (text + content);
					handler.postDelayed (this, speed);
					
				}
				
			};
			
			handler.post (runnable);
			
			return runnable;
			
		}
		
		private static int mId = 0;
		
		public interface DialogPosInterface {
			
			void onPositiveClick (AlertDialog dialog);
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int msg, DialogPosInterface listener, int posButtonTitle) {
			return dialog (activity, style, title, UI.toString (activity, msg), listener, posButtonTitle);
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, List<?> items, DialogPosInterface listener, int posButtonTitle) {
			return dialog (activity, style, title, Arrays.implode (items), listener, posButtonTitle);
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, Object text, DialogPosInterface listener, int posButtonTitle) {
			return dialog (activity, style, UI.toString (activity, title), text, listener, posButtonTitle);
		}
		
		public static AlertDialog dialog (Activity activity, int style, String title, Object text, DialogPosInterface listener, int posButtonTitle) {
			
			AlertDialog dialog = dialog (activity, style, title, text, posButtonTitle).create ();
			dialog.show ();
			
			return dialog (dialog, listener, posButtonTitle);
			
		}
		
		private static void setMessage (AlertDialog.Builder builder, Object text) {
			
			if (text instanceof CharSequence)
				builder.setMessage ((CharSequence) text);
			else if (text instanceof List)
				builder.setMessage (pro.acuna.jabadaba.System.debug ((List<?>) text));
			else
				builder.setMessage (String.valueOf (text));
			
		}
		
		public static AlertDialog.Builder dialog (Activity activity, int style, String title, Object text, int posButtonTitle) {
			
			AlertDialog.Builder builder = dialogBuilder (activity, style);
			
			if (!title.equals ("")) builder.setTitle (title);
			setMessage (builder, text);
			
			if (posButtonTitle > 0)
				builder.setPositiveButton (posButtonTitle, new DialogInterface.OnClickListener () {
					
					@Override
					public void onClick (DialogInterface dialog, int dialogId) {}
					
				});
			
			return builder;
			
		}
		
		public static AlertDialog dialog (final AlertDialog dialog, final DialogPosInterface listener, int posButtonTitle) {
			
			if (posButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_POSITIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onPositiveClick (dialog);
					}
					
				});
			
			return dialog;
			
		}
		
		public interface DialogNegInterface {
			
			void onPositiveClick (AlertDialog dialog);
			void onNegativeClick (AlertDialog dialog);
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int msg, DialogNegInterface listener, int posButtonTitle, int negButtonTitle) {
			return dialog (activity, style, title, UI.toString (activity, msg), listener, posButtonTitle, negButtonTitle);
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, List<?> items, DialogNegInterface listener, int posButtonTitle, int negButtonTitle) {
			return dialog (activity, style, title, Arrays.implode (items), listener, posButtonTitle, negButtonTitle);
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, Object text, DialogNegInterface listener, int posButtonTitle, int negButtonTitle) {
			
			AlertDialog dialog = dialog (activity, style, title, text, posButtonTitle, negButtonTitle).create ();
			dialog.show ();
			
			return dialog (dialog, listener, posButtonTitle, negButtonTitle);
			
		}
		
		public static AlertDialog.Builder dialog (Activity activity, int style, int title, Object text, int posButtonTitle, int negButtonTitle) {
			
			AlertDialog.Builder builder = dialogBuilder (activity, style);
			
			if (title > 0) builder.setTitle (title);
			if (!text.equals ("")) setMessage (builder, text);
			
			if (posButtonTitle > 0)
				builder.setPositiveButton (posButtonTitle, new DialogInterface.OnClickListener () {
					
					@Override
					public void onClick (DialogInterface dialog, int dialogId) {}
					
				});
			
			if (negButtonTitle > 0)
				builder.setNegativeButton (negButtonTitle, new DialogInterface.OnClickListener () {
					
					@Override
					public void onClick (DialogInterface dialog, int dialogId) {}
					
				});
			
			return builder;
			
		}
		
		public static AlertDialog.Builder dialogBuilder (Activity activity, int style) {
			return dialogBuilder (activity, style, false);
		}
		
		public static AlertDialog.Builder dialogBuilder (Activity activity, int style, boolean canceable) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder (activity, style);
			builder.setCancelable (canceable);
			
			return builder;
			
		}
		
		public interface DialogNeuInterface {
			
			void onPositiveClick (AlertDialog dialog);
			void onNeutralClick (AlertDialog dialog);
			void onNegativeClick (AlertDialog dialog);
			
		}
		
		public static AlertDialog dialog (final AlertDialog dialog, final DialogNegInterface listener, int posButtonTitle, int negButtonTitle) {
			
			if (posButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_POSITIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onPositiveClick (dialog);
					}
					
				});
			
			if (negButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_NEGATIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View view) {
						listener.onNegativeClick (dialog);
					}
					
				});
			
			return dialog;
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, Object text, DialogNeuInterface listener, int posButtonTitle, int negButtonTitle, int neuButtonTitle) {
			return dialog (activity, style, UI.toString (activity, title), text, listener, posButtonTitle, negButtonTitle, neuButtonTitle);
		}
		
		public static AlertDialog dialog (Activity activity, int style, String title, Object text, DialogNeuInterface listener, int posButtonTitle, int negButtonTitle, int neuButtonTitle) {
			
			AlertDialog dialog = dialog (activity, style, title, text, posButtonTitle, negButtonTitle, neuButtonTitle).create ();
			dialog.show ();
			
			return dialog (dialog, listener, posButtonTitle, negButtonTitle, neuButtonTitle);
			
		}
		
		public static AlertDialog dialog (final AlertDialog dialog, final DialogNeuInterface listener, int posButtonTitle, int negButtonTitle, int neuButtonTitle) {
			
			if (posButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_POSITIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onPositiveClick (dialog);
					}
					
				});
			
			if (negButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_NEGATIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View view) {
						listener.onNegativeClick (dialog);
					}
					
				});
			
			if (neuButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_NEUTRAL).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View view) {
						listener.onNeutralClick (dialog);
					}
					
				});
			
			return dialog;
			
		}
		
		public static AlertDialog.Builder dialog (Activity activity, int style, String title, Object text, int posButtonTitle, int negButtonTitle, int neuButtonTitle) {
			return dialog (activity, style, title, text, posButtonTitle, negButtonTitle, neuButtonTitle, false);
		}
		
		public static AlertDialog.Builder dialog (Activity activity, int style, String title, Object text, int posButtonTitle, int negButtonTitle, int neuButtonTitle, boolean canceable) {
			
			AlertDialog.Builder builder = dialogBuilder (activity, style, canceable);
			
			if (!title.equals ("")) builder.setTitle (title);
			if (!text.equals ("")) setMessage (builder, text);
			
			if (posButtonTitle > 0)
				builder.setPositiveButton (posButtonTitle, new DialogInterface.OnClickListener () {
					
					@Override
					public void onClick (DialogInterface dialog, int dialogId) {}
					
				});
			
			if (neuButtonTitle > 0)
				builder.setNeutralButton (neuButtonTitle, new DialogInterface.OnClickListener () {
					
					@Override
					public void onClick (DialogInterface dialog, int dialogId) {}
					
				});
			
			if (negButtonTitle > 0)
				builder.setNegativeButton (negButtonTitle, new DialogInterface.OnClickListener () {
					
					@Override
					public void onClick (DialogInterface dialog, int dialogId) {}
					
				});
			
			return builder;
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int msg) {
			return dialog (activity, style, title, UI.toString (activity, msg));
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, List<?> items) {
			return dialog (activity, style, title, Arrays.implode (items));
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, Object text) {
			return dialog (activity, style, UI.toString (activity, title), text);
		}
		
		private static String toString (Activity activity, int title) {
			return (title > 0 ? activity.getString (title) : "");
		}
		
		public static AlertDialog dialog (Activity activity, int style, String title, Object text) {
			
			return dialog (activity, style, title, text, new DialogPosInterface () {
				
				@Override
				public void onPositiveClick (AlertDialog dialog) {
					dialog.dismiss ();
				}
				
			}, android.R.string.ok);
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int inflater, DialogViewInterface listener) {
			return dialog (activity, style, UI.toString (activity, title), inflater, listener);
		}
		
		public static AlertDialog dialog (Activity activity, int style, String title, int inflater, DialogViewInterface listener) {
			
			AlertDialog.Builder builder = dialog (activity, style, title, "", 0, 0, 0, true);
			
			View view = LayoutInflater.from (activity).inflate (inflater, null);
			view = listener.onView (builder, view);
			
			builder.setView (view);
			
			AlertDialog dialog = builder.create ();
			dialog.show ();
			
			return dialog;
			
		}
		
		public interface DialogViewNegInterface {
			
			View onView (AlertDialog.Builder builder, View view);
			
			void onPositiveClick (AlertDialog dialog, View view);
			void onNegativeClick (AlertDialog dialog, View view);
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int inflater, DialogViewNegInterface listener, int posButtonTitle, int negButtonTitle) {
			return dialog (activity, style, UI.toString (activity, title), inflater, listener, posButtonTitle, negButtonTitle);
		}
		
		public static AlertDialog dialog (Activity activity, int style, String title, int inflater, DialogViewNegInterface listener, int posButtonTitle, int negButtonTitle) {
			
			AlertDialog.Builder builder = dialog (activity, style, title, "", posButtonTitle, negButtonTitle, 0);
			
			View view = LayoutInflater.from (activity).inflate (inflater, null);
			view = listener.onView (builder, view);
			
			builder.setView (view);
			
			AlertDialog dialog = builder.create ();
			dialog.show ();
			
			return dialog (dialog, listener, posButtonTitle, negButtonTitle, view);
			
		}
		
		public static AlertDialog dialog (final AlertDialog dialog, final DialogViewNegInterface listener, int posButtonTitle, int negButtonTitle, final View view) {
			
			if (posButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_POSITIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onPositiveClick (dialog, view);
					}
					
				});
			
			if (negButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_NEGATIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onNegativeClick (dialog, view);
					}
					
				});
			
			return dialog;
			
		}
		
		public interface DialogChoiseNegInterface {
			
			void onClick (DialogInterface dialog, int id);
			
			void onPositiveClick (AlertDialog dialog, int id);
			void onNegativeClick (AlertDialog dialog, int id);
			
		}
		
		public interface DialogChoiseInterface {
			
			void onClick (DialogInterface dialog, int id);
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int layout, List<?> items, DialogChoiseInterface listener) {
			return dialog (activity, style, UI.toString (activity, title), layout, Arrays.toStringArray (items), listener);
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int layout, String[] items, DialogChoiseInterface listener) {
			return dialog (activity, style, UI.toString (activity, title), layout, items, listener);
		}
		
		public static AlertDialog dialog (Activity activity, int style, String title, int layout, String[] items, final DialogChoiseInterface listener) {
			
			AlertDialog.Builder builder = dialog (activity, style, title, "", 0, 0, 0, true);
			
			if (Int.size (items) > 0) {
				
				ArrayAdapter adapter = new ArrayAdapter<> (activity, layout, items);
				
				builder.setAdapter (adapter, new DialogInterface.OnClickListener () {
					
					@Override
					public void onClick (DialogInterface dialog, int id) {
						
						dialog.dismiss ();
						listener.onClick (dialog, id);
						
					}
					
				});
				
			}
			
			AlertDialog dialog = builder.create ();
			dialog.show ();
			
			return dialog;
			
		}
		
		public static void rebootDialog (Activity activity, int style, int title, int message) {
			rebootDialog (activity, style, title, message, false);
		}
		
		public static void rebootDialog (final Activity activity, final int style, final int title, int message, final boolean close) {
			
			dialog (activity, style, title, message, new DialogNegInterface () {
				
				@Override
				public void onPositiveClick (AlertDialog dialog) {
					
					try {
						Device.reboot ();
					} catch (Console.ConsoleException e) {
						dialog (activity, style, title, System.error (activity, e));
					}
					
				}
				
				@Override
				public void onNegativeClick (AlertDialog dialog) {
					
					dialog.dismiss ();
					if (close) AppsManager.close (activity);
					
				}
				
			}, android.R.string.ok, R.string.cancel);
			
		}
		
		public static void restoreDialog (Activity activity, int style, int title, int message, String item) {
			restoreDialog (activity, style, title, message, item, false);
		}
		
		public static void restoreDialog (Activity activity, int style, int title, int message, String item, boolean close) {
			
			List<String> items = new ArrayList<> ();
			items.add (item);
			
			restoreDialog (activity, style, title, message, items, close);
			
		}
		
		public static void restoreDialog (Activity activity, int style, int title, int message, List<String> items) {
			restoreDialog (activity, style, title, message, items, false);
		}
		
		public static void restoreDialog (final Activity activity, final int style, final int title, int message, final List<String> items, final boolean close) {
			
			String strMessage = UI.toString (activity, message).replace ("%apps%", Arrays.implode (items));
			
			dialog (activity, style, title, strMessage, new DialogNegInterface () {
				
				@Override
				public void onPositiveClick (AlertDialog dialog) {
					for (String id : items) AppsManager.restore (activity, id);
				}
				
				@Override
				public void onNegativeClick (AlertDialog dialog) {
					
					dialog.dismiss ();
					if (close) AppsManager.close (activity);
					
				}
				
			}, android.R.string.ok, R.string.cancel);
			
		}
		
		public static void eulaDialog (Activity activity, int style, int title, int layout, int textId, int text, int checkboxId, int checkbox, int checkboxText, DialogNeuInterface listener) {
			eulaDialog (activity, style, title, layout, textId, UI.toString (activity, text), checkboxId, checkbox, checkboxText, listener);
		}
		
		public static void eulaDialog (Activity activity, int style, int title, int layout, final int textId, final String text, final int checkboxId, final int checkbox, final int checkboxText, final DialogNeuInterface listener) {
			
			dialog (activity, style, title, layout, new DialogViewNegInterface () {
				
				@Override
				public View onView (AlertDialog.Builder builder, View view) {
					
					TextView textView = view.findViewById (textId);
					textView.setText (text);
					
					textView = view.findViewById (checkbox);
					textView.setText (checkboxText);
					
					final CheckBox checkBox = view.findViewById (checkboxId);
					
					textView.setOnClickListener (new View.OnClickListener () {
						
						@Override
						public void onClick (View view) {
							checkBox.setChecked (!checkBox.isChecked ());
						}
						
					});
					
					return view;
					
				}
				
				@Override
				public void onPositiveClick (AlertDialog dialog, View view) {
					
					CheckBox checkBox = view.findViewById (checkboxId);
					
					if (checkBox.isChecked ()) {
						
						dialog.dismiss ();
						listener.onPositiveClick (dialog);
						
					} else listener.onNeutralClick (dialog);
					
				}
				
				@Override
				public void onNegativeClick (AlertDialog dialog, View view) {
					
					dialog.dismiss ();
					listener.onNegativeClick (dialog);
					
				}
				
			}, android.R.string.ok, R.string.cancel);
			
		}
		
		public interface DialogViewInterface {
			
			View onView (AlertDialog.Builder builder, View view);
			
		}
		
		public interface DialogViewNeuInterface {
			
			View onView (AlertDialog.Builder builder, View view);
			
			void onPositiveClick (AlertDialog dialog, View view);
			void onNegativeClick (AlertDialog dialog, View view);
			void onNeutralClick (AlertDialog dialog, View view);
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int inflater, DialogViewNeuInterface listener, int posButtonTitle, int negButtonTitle, int neuButtonTitle) {
			return dialog (activity, style, UI.toString (activity, title), inflater, listener, posButtonTitle, negButtonTitle, neuButtonTitle);
		}
		
		public static AlertDialog dialog (Activity activity, int style, String title, int inflater, DialogViewNeuInterface listener, int posButtonTitle, int negButtonTitle, int neuButtonTitle) {
			
			AlertDialog.Builder builder = dialog (activity, style, title, "", posButtonTitle, negButtonTitle, neuButtonTitle);
			
			View view = LayoutInflater.from (activity).inflate (inflater, null);
			view = listener.onView (builder, view);
			
			builder.setView (view);
			
			AlertDialog dialog = builder.create ();
			dialog.show ();
			
			return dialog (dialog, listener, posButtonTitle, negButtonTitle, neuButtonTitle, view);
			
		}
		
		public static AlertDialog dialog (final AlertDialog dialog, final DialogViewNeuInterface listener, int posButtonTitle, int negButtonTitle, int neuButtonTitle, final View view) {
			
			if (posButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_POSITIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onPositiveClick (dialog, view);
					}
					
				});
			
			if (negButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_NEGATIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onNegativeClick (dialog, view);
					}
					
				});
			
			if (neuButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_NEUTRAL).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onNeutralClick (dialog, view);
					}
					
				});
			
			return dialog;
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int layout, String[] items, int selected, DialogChoiseNegInterface listener, int posButtonTitle, int negButtonTitle) {
			return dialog (activity, style, UI.toString (activity, title), layout, items, selected, listener, posButtonTitle, negButtonTitle);
		}
		
		public static AlertDialog dialog (Activity activity, int style, String title, int layout, String[] items, int selected, final DialogChoiseNegInterface listener, int posButtonTitle, int negButtonTitle) {
			
			final AlertDialog.Builder builder = dialog (activity, style, title, "", posButtonTitle, negButtonTitle, 0, true);
			
			if (Int.size (items) > 0) {
				
				ArrayAdapter adapter = new ArrayAdapter<> (activity, layout, items);
				
				builder.setSingleChoiceItems (adapter, selected, new DialogInterface.OnClickListener () {
					
					@Override
					public void onClick (DialogInterface dialog, int id) {
						
						mId = id;
						listener.onClick (dialog, id);
						
					}
					
				});
				
			}
			
			AlertDialog dialog = builder.create ();
			dialog.show ();
			
			return dialog (dialog, listener, mId, posButtonTitle, negButtonTitle);
			
		}
		
		public static AlertDialog dialog (final AlertDialog dialog, final DialogChoiseNegInterface listener, final int id, int posButtonTitle, int negButtonTitle) {
			
			if (posButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_POSITIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onPositiveClick (dialog, id);
					}
					
				});
			
			if (negButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_NEGATIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onNegativeClick (dialog, id);
					}
					
				});
			
			return dialog;
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int layout, List<?> items, int selected, DialogChoiseInterface listener) {
			return dialog (activity, style, UI.toString (activity, title), layout, Arrays.toStringArray (items), selected, listener);
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int layout, String[] items, int selected, DialogChoiseInterface listener) {
			return dialog (activity, style, UI.toString (activity, title), layout, items, selected, listener);
		}
		
		public static AlertDialog dialog (Activity activity, int style, String title, int layout, String[] items, int selected, final DialogChoiseInterface listener) {
			
			final AlertDialog.Builder builder = dialog (activity, style, title, "", 0, 0, 0, true);
			
			if (Int.size (items) > 0) {
				
				ArrayAdapter adapter = new ArrayAdapter<> (activity, layout, items);
				
				builder.setSingleChoiceItems (adapter, selected, new DialogInterface.OnClickListener () {
					
					@Override
					public void onClick (DialogInterface dialog, int id) {
						
						dialog.dismiss ();
						
						mId = id;
						listener.onClick (dialog, id);
						
					}
					
				});
				
			}
			
			AlertDialog dialog = builder.create ();
			dialog.show ();
			
			return dialog;
			
		}
		
		public static void debug (Map<?, ?> items) {
			OS.debug (_debug (items));
		}
		
		public static void debug (Intent intent) {
			debug (pro.acuna.andromeda.Arrays.toArray (intent));
		}
		
		public static String _debug (Map<?, ?> items) {
			
			List<String> msg = new ArrayList<> ();
			
			for (Object key : items.keySet ())
				msg.add (key + ": " + items.get (key));
			
			return pro.acuna.jabadaba.Arrays.implode (msg);
			
		}
		
		public static void debug (Activity activity, Map<String, String> items) {
			debug (activity, _debug (items));
		}
		
		private static int dialogStyle () {
			return 0;
		}
		
		public static void debug (Activity activity, Object... msg) {
			debug (activity, msg, "\n");
		}
		
		public static void debug (Activity activity, Object[] msg, String sep) {
			dialog (activity, dialogStyle (), 0, Arrays.implode (sep, msg));
		}
		
		public static void debug (Activity activity, List<?> msg) {
			debug (activity, msg, "\n");
		}
		
		public static void debug (Activity activity, List<?> msg, String sep) {
			dialog (activity, dialogStyle (), 0, Arrays.implode (sep, msg));
		}
		
		public interface DialogListInterface {
			
			View onView (AlertDialog.Builder builder, View view);
			void onClick (DialogInterface dialog, ListItem item);
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int layout, int listLayout, List<ListItem> items, DialogListInterface listener) {
			return dialog (activity, style, UI.toString (activity, title), layout, listLayout, items, listener);
		}
		
		public static AlertDialog dialog (final Activity activity, int style, String title, int layout, final int listLayout, final List<ListItem> items, final DialogListInterface listener) {
			
			final AlertDialog.Builder builder = dialog (activity, style, title, "", 0, 0, 0, true);
			
			ArrayAdapter<ListItem> adapter = new ArrayAdapter<ListItem> (activity, layout, items) {
				
				@NonNull
				public View getView (int position, View view, @NonNull ViewGroup parent) {
					
					ListItem item = items.get (position);
					
					LayoutInflater inflater = (LayoutInflater) activity.getSystemService (Activity.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate (listLayout, parent, false); // TODO
					
					ImageView icon = view.findViewById (R.id.icon);
					TextView title = view.findViewById (R.id.title);
					
					icon.setImageDrawable (item.icon);
					title.setText (item.getArgv (0));
					
					view = listener.onView (builder, view);
					
					return view;
					
				}
				
			};
			
			builder.setAdapter (adapter, new DialogInterface.OnClickListener () {
				
				@Override
				public void onClick (DialogInterface dialog, int position) {
					
					ListItem item = items.get (position);
					listener.onClick (dialog, item);
					
				}
				
			});
			
			AlertDialog dialog = builder.create ();
			dialog.show ();
			
			return dialog;
			
		}
		
		public interface DialogCheckboxInterface {
			
			View onView (AlertDialog.Builder builder, View view);
			void onClick (DialogInterface dialog, ListItem item);
			
			void onPositiveClick (AlertDialog dialog, List<ListItem> items);
			void onNegativeClick (AlertDialog dialog, List<ListItem> items);
			
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, List<ListItem> items, DialogCheckboxInterface listener, int posButtonTitle, int negButtonTitle) {
			return dialog (activity, style, title, R.layout.dialog, R.layout.dialog_list_checkbox, items, listener, posButtonTitle, negButtonTitle);
		}
		
		public static AlertDialog dialog (Activity activity, int style, int title, int layout, int listLayout, List<ListItem> items, DialogCheckboxInterface listener, int posButtonTitle, int negButtonTitle) {
			return dialog (activity, style, UI.toString (activity, title), layout, listLayout, items, listener, posButtonTitle, negButtonTitle);
		}
		
		private static List<ListItem> newItems;
		
		public static AlertDialog dialog (final Activity activity, int style, String title, int layout, final int listLayout, final List<ListItem> items, final DialogCheckboxInterface listener, int posButtonTitle, int negButtonTitle) {
			
			newItems = new ArrayList<> ();
			newItems.addAll (items);
			
			final AlertDialog.Builder builder = dialog (activity, style, title, "", posButtonTitle, negButtonTitle, 0, false);
			
			String[] listItems = new String[Int.size (items)];
			
			for (int i = 0; i < Int.size (items); ++i) {
				
				ListItem item = items.get (i);
				listItems[i] = item.getArgv (0);
				
			}
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String> (activity, layout, listItems) {
				
				@SuppressWarnings ("NullableProblems")
				public View getView (int position, View view, ViewGroup parent) {
					
					final ListItem item = items.get (position);
					
					LayoutInflater inflater = (LayoutInflater) activity.getSystemService (Activity.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate (listLayout, parent, false); // TODO
					
					LinearLayout linearLayout = view.findViewById (R.id.linear_layout);
					
					final CheckBox checkbox = view.findViewById (R.id.checkbox);
					TextView title = view.findViewById (R.id.title);
					
					checkbox.setChecked (item.checked);
					
					linearLayout.setOnClickListener (new View.OnClickListener () {
						
						@Override
						public void onClick (View v) {
							
							if (item.checked)
								newItems.remove (item);
							else
								newItems.add (item);
							
							checkbox.setChecked (!item.checked);
							item.setChecked (!item.checked);
							
						}
						
					});
					
					title.setText (item.getArgv (0));
					
					view = listener.onView (builder, view);
					
					return view;
					
				}
				
			};
			
			builder.setAdapter (adapter, new DialogInterface.OnClickListener () {
				
				@Override
				public void onClick (DialogInterface dialog, int position) {
					
					ListItem item = items.get (position);
					listener.onClick (dialog, item);
					
				}
				
			});
			
			AlertDialog dialog = builder.create ();
			dialog.show ();
			
			return dialog (dialog, listener, posButtonTitle, negButtonTitle);
			
		}
		
		private static AlertDialog dialog (final AlertDialog dialog, final DialogCheckboxInterface listener, int posButtonTitle, int negButtonTitle) {
			
			if (posButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_POSITIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onPositiveClick (dialog, newItems);
					}
					
				});
			
			if (negButtonTitle > 0)
				dialog.getButton (AlertDialog.BUTTON_NEGATIVE).setOnClickListener (new View.OnClickListener () {
					
					@Override
					public void onClick (View v) {
						listener.onNegativeClick (dialog, newItems);
					}
					
				});
			
			return dialog;
			
		}
		
		public static int getKey (String key, List<ListItem> array) {
			
			for (int i = 0; i < Int.size (array); ++i)
				if (array.get (i).getArgv (2).equals (key)) return i;
			
			return -1;
			
		}
		
		public interface DownloadListener {
			
			void onError (Exception e);
			void onHTTPError (int code, String result);
			void onFinish (int code, String result);
			
		}
		
		public static class DownloadDialog extends AsyncTask<String, Long, Integer> {
			
			private Activity activity;
			private DownloadListener listener;
			
			private int mess;
			private long total;
			private Exception error;
			
			private ProgressDialog progress;
			
			public DownloadDialog (Activity activity, int style, int mess, DownloadListener listener) {
				this (activity, style, mess, listener, -1);
			}
			
			public DownloadDialog (Activity activity, int style, int mess, DownloadListener listener, long total) {
				
				this.activity = activity;
				this.mess = mess;
				this.listener = listener;
				this.total = total;
				
			}
			
			@Override
			protected void onPreExecute () {
				
				progress = new ProgressDialog (activity);
				
				progress.setMessage (UI.toString (activity, mess).replace ("%length%", "0").replace ("%size%", "0"));
				progress.setProgressStyle (ProgressDialog.STYLE_HORIZONTAL);
				progress.setProgress (0);
				
				progress.setCancelable (false);
				
				progress.show ();
				
				super.onPreExecute ();
				
			}
			
			@Override
			protected Integer doInBackground (String... params) {
				
				try {
					
					Net.download (params[0], new File (params[1]), params[2], new Net.ProgressListener () {
						
						@Override
						public void onStart (long size) {}
						
						@Override
						public void onProgress (long length, long size) {
							
							if (size == 1) size = total;
							publishProgress (length, size);
							
						}
						
						@Override
						public void onError (int code, String result) {}
						
						@Override
						public void onFinish (int code, String result) {
							listener.onFinish (code, result);
						}
						
					}, total);
					
				} catch (HttpRequestException e) {
					error = e;
				}
				
				return null;
				
			}
			
			@Override
			protected void onProgressUpdate (Long... argv) {
				
				progress.setMessage (UI.toString (activity, mess)
															 .replace ("%length%", Files.mksize (argv[0], "kb") + " kb")
															 .replace ("%size%", Files.mksize (argv[1], "kb") + " kb"));
				
				progress.setProgress ((int) Int.prop (argv[0], argv[1]));
				
			}
			
			@Override
			protected void onPostExecute (Integer code) {
				
				progress.dismiss ();
				
				if (code != Net.HTTP_CODE_OK)
					listener.onHTTPError (code, Net.httpCodes ().get (code));
				else if (error != null)
					listener.onError (error);
				
			}
			
		}
		
		public interface ColorPickerDialogInterface {
			
			void onSubmit (AlertDialog dialog, int color);
			void onDismiss (AlertDialog dialog, int color);
			
		}
		
		public static void colorPickerDialog (Activity activity, int style, int title, int color, ColorPickerDialogInterface listener) {
			colorPickerDialog (activity, style, UI.toString (activity, title), color, listener, R.layout.color_picker);
		}
		
		public static void colorPickerDialog (Activity activity, int style, String title, final int color, final ColorPickerDialogInterface listener, int layout) {
			
			dialog (activity, style, title, layout, new DialogViewNegInterface () {
				
				ColorPicker picker;
				
				@Override
				public View onView (AlertDialog.Builder builder, View view) {
					
					picker = view.findViewById (R.id.picker);
					picker.setOldCenterColor (color);
					
					picker.addSaturationBar ((SaturationBar) view.findViewById (R.id.saturation_bar));
					picker.addValueBar ((ValueBar) view.findViewById (R.id.value_bar));
					
					return view;
					
				}
				
				@Override
				public void onPositiveClick (AlertDialog dialog, View view) {
					
					dialog.dismiss ();
					listener.onSubmit (dialog, picker.getColor ());
					
				}
				
				@Override
				public void onNegativeClick (AlertDialog dialog, View view) {
					
					dialog.dismiss ();
					listener.onDismiss (dialog, picker.getColor ());
					
				}
				
			}, android.R.string.ok, R.string.cancel);
			
		}
		
		public static void error (Activity activity, int style, Object mess) {
			
			UI.dialog (activity, style, R.string.title_error, mess, new UI.DialogPosInterface () {
				
				@Override
				public void onPositiveClick (AlertDialog dialog) {
					dialog.dismiss ();
				}
				
			}, android.R.string.ok);
			
		}
		
	}