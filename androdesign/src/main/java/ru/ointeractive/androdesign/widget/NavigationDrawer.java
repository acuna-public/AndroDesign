  package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 10.04.2018
  */
  
  import android.app.Activity;
  import android.content.res.ColorStateList;
  import android.graphics.Bitmap;
  import android.graphics.Color;
  import android.support.annotation.NonNull;
  import android.support.design.widget.NavigationView;
  import android.support.v4.view.GravityCompat;
  import android.support.v4.widget.DrawerLayout;
  import android.support.v7.app.ActionBarDrawerToggle;
  import android.support.v7.widget.Toolbar;
  import android.util.ArrayMap;
  import android.view.Menu;
  import android.view.MenuItem;
  import android.view.SubMenu;
  import android.view.View;
  import android.widget.ImageView;
  import android.widget.LinearLayout;
  import android.widget.TextView;
	
  import java.util.ArrayList;
  import java.util.LinkedHashMap;
  import java.util.List;
  import java.util.Map;
	
  import ru.ointeractive.andromeda.graphic.Graphic;
  import upl.core.Int;
	
  public class NavigationDrawer {
    
    private Activity activity;
    private View view;
    
    public NavigationDrawer (Activity activity) {
      
      this.activity = activity;
      
    }
    
    public DrawerListener listener;
    
    public interface DrawerListener {
      
      void onDrawerOpened (View view);
      void onDrawerClosed (View view);
      void onItemSelected (DrawerLayout drawer, MenuItem item);
      
    }
    
    public NavigationDrawer setListener (DrawerListener mListener) {
      
      listener = mListener;
      
      if (listener != null) {
        
        nView.setNavigationItemSelectedListener (new NavigationView.OnNavigationItemSelectedListener () {
          
          @Override
          public boolean onNavigationItemSelected (@NonNull MenuItem item) {
            
            listener.onItemSelected (drawer, item);
            return true;
            
          }
          
        });
        
      }
      
      return this;
      
    }
    
    private DrawerLayout drawer;
    private NavigationView nView;
    private Menu menu;
    private int itemId = 0, groupId = 0;
    
    public NavigationDrawer setLayout (View drawer) {
      return setLayout ((DrawerLayout) drawer);
    }
    
    public NavigationDrawer setLayout (DrawerLayout drawer) {
      
      this.drawer = drawer;
      return this;
      
    }
    
    public NavigationDrawer setView (View view) {
      return setView ((NavigationView) view);
    }
    
    public NavigationDrawer setView (NavigationView view) {
      
      this.nView = view;
      this.view = nView.getHeaderView (0);
      
      return this;
      
    }
    
    public NavigationDrawer build (Toolbar toolbar) {
      
      //drawer.setDrawerShadow (R.drawable.drawer_shadow, GravityCompat.START);
      
      ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (activity, drawer, toolbar, 0, 0) {
        
        @Override
        public void onDrawerClosed (View drawerView) {
          
          if (listener != null)
            listener.onDrawerClosed (drawerView);
          
        }
        
        @Override
        public void onDrawerOpened (View drawerView) {
          
          if (listener != null)
            listener.onDrawerOpened (drawerView);
          
        }
        
      };
      
      drawer.addDrawerListener (toggle);
      
      //if (toolbar instanceof ru.ointeractive.androdesign.widget.Toolbar)
      //  ((ru.ointeractive.androdesign.widget.Toolbar) toolbar).actionBar.setHomeAsUpIndicator (R.drawable.ic_mode_edit_white_18dp);
      
      toggle.syncState ();
      
      return this;
      
    }
    
    public void open () {
      drawer.openDrawer (GravityCompat.START);
    }
    
    public void close () {
      drawer.closeDrawer (GravityCompat.START);
    }
    
    public void toggle () {
      
      if (drawer.isDrawerVisible (GravityCompat.START))
        close ();
      else
        open ();
      
    }
    
    public void setMenuItemColors (int color) {
      
      int navDefaultIconColor = Color.parseColor ("#737373");
      int navDefaultTextColor = Color.parseColor ("#202020");
      
      ColorStateList navMenuIconList = new ColorStateList (
        
                                                            new int[][] {
          
                                                              new int[] {android.R.attr.state_checked},
                                                              new int[] {android.R.attr.state_enabled},
                                                              new int[] {android.R.attr.state_pressed},
                                                              new int[] {android.R.attr.state_focused},
                                                              new int[] {android.R.attr.state_pressed},
            
                                                              },
        
                                                            new int[] {
          
                                                              color,
                                                              navDefaultIconColor,
                                                              navDefaultIconColor,
                                                              navDefaultIconColor,
                                                              navDefaultIconColor,
            
                                                              }
      
      );
      
      ColorStateList navMenuTextList = new ColorStateList (
        
                                                            new int[][] {
          
                                                              new int[] {android.R.attr.state_checked},
                                                              new int[] {android.R.attr.state_enabled},
                                                              new int[] {android.R.attr.state_pressed},
                                                              new int[] {android.R.attr.state_focused},
                                                              new int[] {android.R.attr.state_pressed},
            
                                                              },
        
                                                            new int[] {
          
                                                              color,
                                                              navDefaultTextColor,
                                                              navDefaultTextColor,
                                                              navDefaultTextColor,
                                                              navDefaultTextColor,
            
                                                              }
      
      );
      
      nView.setItemIconTintList (navMenuIconList);
      nView.setItemTextColor (navMenuTextList);
      
    }
    
    public NavigationDrawer setAvatar (int id, int avatar) {
      
      Bitmap bitmap = Graphic.toBitmap (activity, avatar);
      return setAvatar (id, bitmap);
      
    }
    
    public NavigationDrawer setAvatar (int id, Bitmap bitmap) {
      
      ImageView imageView = view.findViewById (id);
      
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (48, 48);
      imageView.setLayoutParams (params);
      
      imageView.setImageBitmap (Graphic.rounded (bitmap));
      //imageView.setBackgroundResource (R.drawable.white_border);
      
      return this;
      
    }
    
    public NavigationDrawer setUserName (int id, CharSequence text, View.OnClickListener listener) {
      
      TextView textView = view.findViewById (id);
      
      textView.setText (text);
      textView.setOnClickListener (listener);
      
      return this;
      
    }
    
    public NavigationDrawer setAccountName (int id, CharSequence text) {
      
      TextView textView = view.findViewById (id);
      textView.setText (text);
      
      return this;
      
    }
    
    private List<String> menuItems = new ArrayList<> ();
    private Map<String, Integer> menuIcons = new LinkedHashMap<> ();
    
    public NavigationDrawer addItem (int title) {
    	return addItem (activity.getString (title));
    }
    
    public NavigationDrawer addItem (String title) {
    	
    	menuItems.add (title);
    	return this;
    	
    }
	
	  public NavigationDrawer addItem (int title, int icon) {
    	return addItem (activity.getString (title), icon);
	  }
	  
	  public NavigationDrawer addItem (String title, int icon) {
    	
    	menuIcons.put (title, icon);
    	return this;
    	
    }
    
	  public NavigationDrawer addGroup (int title) {
      
      Menu menu = nView.getMenu ();
      int i = 0;
      
      SubMenu subMenu = menu.addSubMenu (title);
      
      if (Int.size (menuItems) > 0) {
      	
	      for (String key : menuItems) {
		
		      subMenu.add (groupId, i, i, key);
		      i++;
		
	      }
	      
      } else {
      	
	      for (String key : menuIcons.keySet ()) {
		
		      subMenu.add (groupId, i, i, key).setIcon (menuIcons.get (key));
		      i++;
		
	      }
	
      }
      
      ++groupId;
      
	    menuItems = new ArrayList<> ();
	    menuIcons = new LinkedHashMap<> ();
	    
      return this;
      
    }
    
  }