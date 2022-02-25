  package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 08.05.2018
  */
  
  import android.app.Activity;
  import android.content.Context;
  import android.content.ContextWrapper;
  import android.content.res.TypedArray;
  import android.graphics.Bitmap;
  import android.graphics.drawable.Drawable;
  import android.support.design.widget.AppBarLayout;
  import android.support.v7.app.ActionBar;
  import android.support.v7.app.AppCompatActivity;
  import android.util.AttributeSet;
  import android.view.ContextThemeWrapper;
  import android.view.View;
  import android.widget.LinearLayout;
  import android.widget.TextView;

  import java.io.InputStream;

  import ru.ointeractive.androdesign.R;
  import ru.ointeractive.andromeda.graphic.Graphic;
  import upl.core.exceptions.OutOfMemoryException;

  public class Toolbar extends android.support.v7.widget.Toolbar {
    
    private TextView titleView;
    private final AppCompatActivity cActivity;
    private boolean isThemeHolo = false;
    
    public Toolbar (Context context) {
      this (context, null);
    }
    
    public Toolbar (Context context, AttributeSet attrs) {
      this (context, attrs, R.attr.toolbarStyle);
    }
    
    public Toolbar (Context context, AttributeSet attrs, int defStyle) {
      
      super (context, attrs, defStyle);
      
	    titleView = findViewById (R.id.toolbar_title);
	    
	    if (context instanceof AppCompatActivity)
	      cActivity = ((AppCompatActivity) context);
	    else
		    cActivity = (AppCompatActivity) ((ContextWrapper) context).getBaseContext ();
	    
	    if (cActivity != null)
		    cActivity.setSupportActionBar (this);
	    
	    TypedArray array = context.obtainStyledAttributes (attrs, R.styleable.Toolbar);
	    
	    for (int i = 0; i < array.getIndexCount (); ++i) {
	    	
		    int attr = array.getIndex (i);
		    
		    //if (attr == R.styleable.Toolbar_title)
			  //  setTitle (array.getText (attr).toString ());
		    //else if (attr == R.styleable.Toolbar_description)
			  //  setDescription (array.getText (attr).toString ());
		   
	    }
	    
	    array.recycle ();
	    
    }
    
    public Toolbar setThemeHolo (boolean isThemeHolo) {
      
      this.isThemeHolo = isThemeHolo;
      return this;
      
    }
    
    public void setLogo (Bitmap icon) {
    	setLogo (icon, true);
    }
    
    public void setLogo (Bitmap icon, boolean rounded) {
      
      if (rounded) icon = Graphic.rounded (icon);
	    setLogo (Graphic.toDrawable (icon));
     
    }
    
    @Override
    public void setLogo (Drawable icon) {
      setLogo (icon, true);
    }
    
    public void setLogo (Drawable icon, boolean rounded) {
      
      if (rounded) icon = Graphic.toDrawable (Graphic.rounded (icon));
      super.setLogo (icon);
      
    }
    
    public void setLogo (InputStream stream) throws OutOfMemoryException {
      setLogo (stream, true);
    }
    
    public void setLogo (InputStream stream, boolean rounded) throws OutOfMemoryException {
      
      Bitmap icon = Graphic.toBitmap (stream);
      if (rounded) icon = Graphic.rounded (icon);
	
	    setLogo (Graphic.toDrawable (icon));
     
    }
    
    private Listener listener;
    
    public interface Listener {
      
      void onClick (View view);
      void onScrollTop ();
      void onScrollBottom ();
      
    }
    
    public Toolbar setListener (final Listener listener) {
      
      this.listener = listener;
      
      setOnClickListener (new View.OnClickListener () {
        
        @Override
        public void onClick (View view) {
          if (listener != null) listener.onClick (view);
        }
        
      });
      
      return this;
      
    }
    
    public Toolbar setAppBar (View appBar) {
      return setAppBar ((AppBarLayout) appBar);
    }
    
    public Toolbar setAppBar (AppBarLayout appBar) {
      
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
     
    	ActionBar actionBar = cActivity.getSupportActionBar ();
      if (actionBar != null) actionBar.show ();
      
      return this;
      
    }
    
    public Toolbar hide () {
	
	    ActionBar actionBar = cActivity.getSupportActionBar ();
	    if (actionBar != null) actionBar.hide ();
	    
      return this;
      
    }
		
	  @Override
	  public void setTitle (int title) {
    	setTitle (getContext ().getString (title));
	  }
	  
	  @Override
    public void setTitle (CharSequence title) {
    
		  titleView = findViewById (R.id.toolbar_descr_title);
		  
		  if (titleView != null)
        titleView.setText (title);
		  else
		  	super.setTitle (title);
		  
		  View lineView = findViewById (R.id.line);
		  if (isThemeHolo) lineView.setVisibility (View.VISIBLE);
		  
	  }
    
    public void setDescription (int title) {
      setDescription (getContext().getString (title));
    }
    
    public void setDescription (String title) {
      
      LinearLayout layout = findViewById (R.id.title_layout);
      layout.setVisibility (View.GONE);
      
      layout = findViewById (R.id.descr_layout);
      layout.setVisibility (View.VISIBLE);
      
      titleView = findViewById (R.id.toolbar_descr);
	
	    if (titleView != null)
		    titleView.setText (title);
	    else
		    super.setSubtitle (title);
	    
    }
    
    public void setHomeButton (boolean set) {
    	
	    if (cActivity != null) {
	    	
		    cActivity.setSupportActionBar (this);
		    
		    ActionBar actionBar = cActivity.getSupportActionBar ();
		    
		    if (set && actionBar != null) {
		    	
			    actionBar.setDisplayHomeAsUpEnabled (true);
			    actionBar.setHomeButtonEnabled (true);
			    
		    }
		    
	    }
     
    }
    
  }