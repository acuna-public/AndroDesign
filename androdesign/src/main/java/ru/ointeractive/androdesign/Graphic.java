  package ru.ointeractive.androdesign;
  /*
   Created by Acuna on 18.06.2018
  */
  
  import android.graphics.Bitmap;
  import android.graphics.Canvas;
  import android.graphics.drawable.Drawable;
  import android.os.Build;
  import android.support.v4.graphics.drawable.DrawableCompat;
  
  public class Graphic {
    
    public static Bitmap toBitmap (Drawable drawable) {
      
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        drawable = (DrawableCompat.wrap (drawable)).mutate ();
      
      Bitmap bitmap = ru.ointeractive.andromeda.graphic.Graphic.createBitmap (drawable.getIntrinsicWidth (), drawable.getIntrinsicHeight (), Bitmap.Config.ARGB_8888);
      
      Canvas canvas = new Canvas (bitmap);
      
      drawable.setBounds (0, 0, canvas.getWidth (), canvas.getHeight ());
      drawable.draw (canvas);
      
      return bitmap;
      
    }
    
  }