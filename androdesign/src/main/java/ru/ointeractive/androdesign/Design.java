	package ru.ointeractive.androdesign;
	
	import android.app.Activity;
	import android.app.Fragment;
	import android.app.FragmentTransaction;
	
	public class Design {
		
		public static FragmentTransaction fragmentReplace (Activity activity, int container, Fragment fragment) {
			
			FragmentTransaction fragmentTransaction = activity.getFragmentManager ().beginTransaction ();
			fragmentTransaction.replace (container, fragment);
			
			fragmentTransaction.commitAllowingStateLoss ();
			
			return fragmentTransaction;
			
		}
		
	}