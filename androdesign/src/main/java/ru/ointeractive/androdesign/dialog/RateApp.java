	package ru.ointeractive.androdesign.dialog;
	/*
   Created by Acuna on 30.09.2020
  */
	
	import android.app.Activity;
	import android.app.AlertDialog;
	
	import ru.ointeractive.androdesign.Andromeda;
	import ru.ointeractive.androdesign.R;
	import ru.ointeractive.androdesign.UI;
	import ru.ointeractive.andromeda.apps.AppsManager;
	
	public class RateApp {
		
		private Activity activity;
		private Andromeda andro;
		
		private static String PREF_SHOW = "rate_dialog_show";
		private static String PREF_LAUNCH_NUM = "rate_dialog_launch_num";
		
		private int mLaunchNum = 10, mCancelLaunchNum = 10, mStyle;
		
		public RateApp (Activity activity, int appName) {
			this (activity, activity.getString (appName));
		}
		
		public RateApp (Activity activity, String appName) {
			
			this.activity = activity;
      andro = new Andromeda (activity, appName);
      
      mStyle = andro.dialogStyle ();
      
		}
		
		public RateApp setLaunchNum (int num) {
			
			mLaunchNum = num;
			return this;
			
		}
		
		public RateApp setCancelLaunchNum (int num) {
			
			mCancelLaunchNum = num;
			return this;
			
		}
		
		public RateApp setStyle (int style) {
			
			mStyle = style;
			return this;
			
		}
		
		public void show () {
			
			int launchNum = andro.prefs.get (PREF_LAUNCH_NUM, 0);
			
			if (andro.prefs.get (PREF_SHOW, true)) {
				
				if (launchNum >= mLaunchNum || launchNum >= mCancelLaunchNum) {
					
					UI.dialog (activity, mStyle, R.string.rate_dialog_title, R.string.rate_dialog_message, new UI.DialogNeuInterface () {
						
						@Override
						public void onPositiveClick (AlertDialog dialog) { // Да
							
							dialog.dismiss ();
              andro.prefs.set (PREF_SHOW, false);
							
							AppsManager.openPlayUrl (activity);
							
						}
						
						@Override
						public void onNegativeClick (AlertDialog dialog) { // Нет
							
							dialog.dismiss ();
							andro.prefs.set (PREF_SHOW, false);
							
						}
						
						@Override
						public void onNeutralClick (AlertDialog dialog) { // Позже
							
							dialog.dismiss ();
							andro.prefs.set (PREF_LAUNCH_NUM, 0);
							
						}
						
					}, R.string.rate_dialog_ok, R.string.rate_dialog_no, R.string.rate_dialog_cancel);
					
				}
				
				launchNum++;
				andro.prefs.set (PREF_LAUNCH_NUM, launchNum);
				
			}
   
		}
		
	}