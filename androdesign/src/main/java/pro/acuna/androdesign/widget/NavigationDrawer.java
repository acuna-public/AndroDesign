	package pro.acuna.androdesign.widget;
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
	import android.view.Menu;
	import android.view.MenuItem;
	import android.view.SubMenu;
	import android.view.View;
	import android.widget.ImageView;
	import android.widget.LinearLayout;
	
	import pro.acuna.androdesign.Graphic;
	import pro.acuna.andromeda.OS;
	
	public class NavigationDrawer {
		
		private Activity activity;
		private int drawerLayout, openMess, closeMess;
		private View view;
		
		public NavigationDrawer (Activity activity) {
			
			this.activity = activity;
			
		}
		
		public DrawerListener listener;
		
		public interface DrawerListener {
			
			void onDrawerOpened (View view);
			void onDrawerClosed (View view);
			void onCreateView (NavigationView view);
			void onItemSelected (DrawerLayout drawer, MenuItem item);
			
		}
		
		public NavigationDrawer setListener (DrawerListener mListener) {
			
			listener = mListener;
			
			if (listener != null) {
				
				navigationView.setNavigationItemSelectedListener (new NavigationView.OnNavigationItemSelectedListener () {
					
					@Override
					public boolean onNavigationItemSelected (@NonNull MenuItem item) {
						
						listener.onItemSelected (drawer, item);
						return true;
						
					}
					
				});
				
				listener.onCreateView (navigationView);
				
			}
			
			return this;
			
		}
		
		private DrawerLayout drawer;
		private NavigationView navigationView;
		private Menu menu;
		private SubMenu subMenu;
		private DrawerMenu dMenu;
		
		public class DrawerMenu {
			
			private int groupId = 0, itemId = 0, order = 0;
			
			public DrawerMenu () {
				
				menu = navigationView.getMenu ();
				
			}
			
			public void addGroup (int title) {
				
				subMenu = menu.addSubMenu (title);
				
			}
			
		}
		
		public void addMenu (DrawerMenu menu) {
			dMenu = menu;
		}
		
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
			
			this.navigationView = view;
			this.view = navigationView.getHeaderView (0);
			
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
			
			//if (toolbar instanceof pro.acuna.androdesign.widget.Toolbar)
			//	((pro.acuna.androdesign.widget.Toolbar) toolbar).actionBar.setHomeAsUpIndicator (R.drawable.ic_mode_edit_white_18dp);
			
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
			
			ColorStateList navMenuIconList = new ColorStateList(
				
				new int[][] {
					
					new int[] { android.R.attr.state_checked },
					new int[] { android.R.attr.state_enabled },
					new int[] { android.R.attr.state_pressed },
					new int[] { android.R.attr.state_focused },
					new int[] { android.R.attr.state_pressed },
					
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
					
					new int[] { android.R.attr.state_checked },
					new int[] { android.R.attr.state_enabled },
					new int[] { android.R.attr.state_pressed },
					new int[] { android.R.attr.state_focused },
					new int[] { android.R.attr.state_pressed },
					
				},
				
				new int[] {
					
					color,
					navDefaultTextColor,
					navDefaultTextColor,
					navDefaultTextColor,
					navDefaultTextColor,
					
				}

			);
			
			navigationView.setItemIconTintList (navMenuIconList);
			navigationView.setItemTextColor (navMenuTextList);
			
		}
		
		public NavigationDrawer setAvatar (int id, int avatar) {
			
			ImageView imageView = view.findViewById (id);
			
			LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams (48, 48);
			imageView.setLayoutParams (parms);
			
			Bitmap bitmap = pro.acuna.andromeda.Graphic.toBitmap (activity, avatar);
			
			imageView.setImageDrawable (Graphic.rounded (activity, bitmap));
			//imageView.setBackgroundResource (R.drawable.white_border);
			
			return this;
			
		}
		
	}