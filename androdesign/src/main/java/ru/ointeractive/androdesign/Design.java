	package ru.ointeractive.androdesign;
	
	import android.support.v4.app.Fragment;
	import android.support.v4.app.FragmentActivity;
	import android.support.v4.app.FragmentTransaction;
	
	public class Design {
		
		public static FragmentTransaction fragmentReplace (FragmentActivity activity, int container, Fragment fragment) {
			
			FragmentTransaction ft = activity.getSupportFragmentManager ().beginTransaction ();
			
			ft.replace (container, fragment);
			ft.commitAllowingStateLoss ();
			
			return ft;
			
		}
		
		public static FragmentTransaction fragmentRecreate (FragmentActivity activity, int container, Fragment fragment) throws IllegalAccessException, InstantiationException {
			
			FragmentTransaction ft = activity.getSupportFragmentManager ().beginTransaction ();
			
			ft.remove (fragment);
			Fragment newInstance = recreateFragment (activity, fragment);
			ft.add (container, newInstance);
			ft.commit ();
			
			return ft;
			
		}
		
		private static Fragment recreateFragment (FragmentActivity activity, Fragment f) throws InstantiationException, IllegalAccessException {
			
			Fragment.SavedState savedState = activity.getSupportFragmentManager ().saveFragmentInstanceState(f);
			
			Fragment newInstance = f.getClass().newInstance();
			newInstance.setInitialSavedState(savedState);
			
			return newInstance;
			
		}
		
	}