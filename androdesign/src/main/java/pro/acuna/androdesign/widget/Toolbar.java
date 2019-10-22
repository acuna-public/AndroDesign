  package pro.acuna.androdesign.widget;
  /*
   Created by Acuna on 08.05.2018
  */
  
  import android.app.Activity;
  import android.graphics.Bitmap;
  import android.graphics.drawable.Drawable;
  import android.support.design.widget.AppBarLayout;
  import android.support.v7.app.ActionBar;
  import android.support.v7.app.AppCompatActivity;
  import android.util.AttributeSet;
  import android.view.View;
  import android.widget.ImageView;
  import android.widget.LinearLayout;
  import android.widget.TextView;

  import java.io.IOException;
  import java.io.InputStream;

  import pro.acuna.androdesign.R;
  import pro.acuna.andromeda.Graphic;
  import pro.acuna.jabadaba.exceptions.OutOfMemoryException;

  public class Toolbar extends android.support.v7.widget.Toolbar {
    
    private Activity activity;
    private ImageView image;
    private AppCompatActivity cActivity;
    private TextView titleView;
    public ActionBar actionBar;
    private String mTitle;
    private View view;
    private boolean isThemeHolo = false;
    
    public Toolbar (Activity activity) {
      
      super (activity);
      this.activity = activity;
      
    }
    
    public Toolbar (Activity activity, AttributeSet attrs) {
      
      super (activity, attrs);
      this.activity = activity;
      
    }
    
    public Toolbar (Activity activity, AttributeSet attrs, int defStyle) {
      
      super (activity, attrs, defStyle);
      this.activity = activity;
      
    }
    
    public Toolbar setThemeHolo (boolean isThemeHolo) {
      
      this.isThemeHolo = isThemeHolo;
      return this;
      
    }
    
    public Toolbar init () {
      return init (activity.getWindow ().getDecorView ());
    }
    
    public Toolbar init (View view) {
      
      this.view = view;
      
      image = view.findViewById (R.id.icon);
      
      View mToolbar = view.findViewById (R.id.line);
      if (isThemeHolo) mToolbar.setVisibility (View.VISIBLE);
      
      titleView = view.findViewById (R.id.toolbar_title);
      
      if (toolbar == null) toolbar = view.findViewById (R.id.toolbar);
      
      if (activity instanceof AppCompatActivity) {
        
        cActivity = (AppCompatActivity) activity;
        
        cActivity.setSupportActionBar (toolbar);
        actionBar = cActivity.getSupportActionBar ();
        
      }
      
      return this;
      
    }
    
    public Toolbar setIcon (Bitmap icon) {
      return setIcon (icon, true);
    }
    
    public Toolbar setIcon (Bitmap icon, boolean rounded) {
      
      image.setVisibility (View.VISIBLE);
      
      if (rounded) icon = Graphic.rounded (icon);
      image.setImageBitmap (icon);
      
      return this;
      
    }
    
    public Toolbar setIcon (Drawable icon) {
      return setIcon (icon, true);
    }
    
    public Toolbar setIcon (Drawable icon, boolean rounded) {
      
      image.setVisibility (View.VISIBLE);
      
      if (rounded) icon = Graphic.toDrawable (Graphic.rounded (icon));
      image.setImageDrawable (icon);
      
      return this;
      
    }
    
    public Toolbar setIcon (InputStream stream) throws IOException, OutOfMemoryException {
      return setIcon (stream, true);
    }
    
    public Toolbar setIcon (InputStream stream, boolean rounded) throws IOException, OutOfMemoryException {
      
      image.setVisibility (View.VISIBLE);
      
      Bitmap icon = Graphic.toBitmap (stream);
      if (rounded) icon = Graphic.rounded (icon);
      
      image.setImageBitmap (icon);
      
      return this;
      
    }
    
    private Listener listener;
    
    public interface Listener {
      
      void onClick (View view);
      void onScrollTop ();
      void onScrollBottom ();
      
    }
    
    public Toolbar setListener (final Listener listener) {
      
      this.listener = listener;
      
      toolbar.setOnClickListener (new View.OnClickListener () {
        
        @Override
        public void onClick (View view) {
          if (listener != null) listener.onClick (view);
        }
        
      });
      
      return this;
      
    }
    
    android.support.v7.widget.Toolbar toolbar;
    
    public Toolbar setToolbar (View toolbar) {
      return setToolbar ((android.support.v7.widget.Toolbar) toolbar);
    }
    
    public Toolbar setToolbar (android.support.v7.widget.Toolbar toolbar) {
      
      this.toolbar = toolbar;
      return this;
      
    }
    
    AppBarLayout appBar;
    
    public Toolbar setAppBar (View appBar) {
      return setAppBar ((AppBarLayout) appBar);
    }
    
    public Toolbar setAppBar (AppBarLayout appBar) {
      
      this.appBar = appBar;
      
      appBar.addOnOffsetChangedListener (new AppBarLayout.OnOffsetChangedListener () {
        
        boolean isShow = false;
        int scrollRange = -1;
        
        @Override
        public void onOffsetChanged (AppBarLayout appBarLayout, int verticalOffset) {
          
          if (scrollRange == -1)
            scrollRange = appBarLayout.getTotalScrollRange ();
          
          if (scrollRange + verticalOffset == 0) {
            
            isShow = true;
            show ();
            
            if (listener != null) listener.onScrollTop ();
            
          } else if (isShow) {
            
            isShow = false;
            hide ();
            
            if (listener != null) listener.onScrollBottom ();
            
          }
          
        }
        
      });
      
      return this;
      
    }
    
    public Toolbar show () {
      
      if (actionBar != null) actionBar.show ();
      return this;
      
    }
    
    public Toolbar hide () {
      
      actionBar.hide ();
      return this;
      
    }
    
    public void setTitle (int title) {
      setTitle (activity.getString (title));
    }
    
    public void setTitle (String title) {
      
      mTitle = title;
      titleView.setText (title);
      
    }
    
    public void setDescription (int title) {
      setDescription (activity.getString (title));
    }
    
    public void setDescription (String title) {
      
      LinearLayout layout = view.findViewById (R.id.title_layout);
      layout.setVisibility (View.GONE);
      
      layout = view.findViewById (R.id.descr_layout);
      layout.setVisibility (View.VISIBLE);
      
      titleView = view.findViewById (R.id.toolbar_descr_title);
      setTitle (mTitle);
      
      titleView = view.findViewById (R.id.toolbar_descr);
      titleView.setText (title);
      
    }
    
    public Toolbar setHomeButton (boolean set) {
      
      if (set) {
        
        if (actionBar != null) {
          
          actionBar.setDisplayHomeAsUpEnabled (true);
          actionBar.setHomeButtonEnabled (true);
          
        } else throw new NullPointerException ("getSupportActionBar () method returns null, be sure to add init (R.id.toolbar) method first");
        
      }
      
      return this;
      
    }
    
  }