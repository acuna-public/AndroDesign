  package pro.acuna.androdesign;
  /*
   Created by Acuna on 18.06.2018
  */
  
  import android.content.Context;
  import android.graphics.Bitmap;
  import android.graphics.Canvas;
  import android.graphics.Paint;
  import android.graphics.PorterDuff;
  import android.graphics.PorterDuffXfermode;
  import android.graphics.Rect;
  import android.graphics.RectF;
  import android.graphics.drawable.Drawable;
  import android.os.Build;
  import android.support.v4.graphics.drawable.DrawableCompat;
  import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
  import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
  
  public class Graphic {
    
    public static Bitmap toBitmap (Drawable drawable) {
      
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        drawable = (DrawableCompat.wrap (drawable)).mutate ();
      
      Bitmap bitmap = pro.acuna.andromeda.Graphic.createBitmap (drawable.getIntrinsicWidth (), drawable.getIntrinsicHeight (), Bitmap.Config.ARGB_8888);
      
      Canvas canvas = new Canvas (bitmap);
      
      drawable.setBounds (0, 0, canvas.getWidth (), canvas.getHeight ());
      drawable.draw (canvas);
      
      return bitmap;
      
    }
  
  }