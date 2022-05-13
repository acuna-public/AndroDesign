  package ru.ointeractive.androdesign;
  /*
   Created by Acuna on 18.06.2018
  */
  
  import android.graphics.Bitmap;
  import android.graphics.Canvas;
  import android.graphics.drawable.Drawable;
  import android.graphics.drawable.ShapeDrawable;
  import android.graphics.drawable.shapes.RoundRectShape;
  import android.os.Build;
  import android.support.annotation.RequiresApi;
  import android.support.v4.graphics.drawable.DrawableCompat;
  import android.view.View;

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
    
	  @RequiresApi (api = Build.VERSION_CODES.JELLY_BEAN)
	  public static void setBackground (View view, int color, float r) {
    
		  ShapeDrawable shape = new ShapeDrawable (new RoundRectShape (new float[] {r, r, r, r, r, r, r, r}, null, null));
		  shape.getPaint ().setColor (color);
		  
		  view.setBackground (shape);
		  
	  }
	  
  }