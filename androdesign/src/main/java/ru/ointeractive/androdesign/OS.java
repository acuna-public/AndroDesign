	package ru.ointeractive.androdesign;
	
	import android.app.Activity;
	import android.content.Context;
	import android.content.pm.PackageManager;
	import android.os.Build;
	import android.support.v4.app.ActivityCompat;
	
	import java.util.ArrayList;
	import java.util.List;
	
	import ru.ointeractive.jabadaba.Arrays;
	import ru.ointeractive.jabadaba.Int;
	
	public class OS {
		
    public static boolean allowPermissions () {
    	return Build.VERSION.SDK_INT >= 23;
    }
    
    public static void checkPermissions (Activity activity, String... permissions) {
      
      if (allowPermissions ()) {
        
        List<String> needed = new ArrayList<> ();
        
        for (String perm : permissions) {
          
          if (!checkPermission (activity, perm))
            needed.add (perm);
          
        }
        
        requestPermissions (activity, Arrays.toStringArray (needed));
        
      }
      
    }
    
    public static void requestPermissions (Activity activity, String... perm) {
    	
      if (Int.size (perm) > 0)
        ActivityCompat.requestPermissions (activity, perm, 100);
      
    }
    
    public static boolean permissionGranted (Context context, String... permissions) {
      
      if (allowPermissions ()) {
        
        for (String perm : permissions)
          if (!checkPermission (context, perm))
            return false;
          
      }
      
      return true;
      
    }
    
    public static int permissionStatus (Context context, String permission) {
      return context.checkCallingPermission (permission);
    }
    
    public static boolean checkPermission (Context context, String permission) {
      return permissionStatus (context, permission) == PackageManager.PERMISSION_GRANTED;
    }
    
	}