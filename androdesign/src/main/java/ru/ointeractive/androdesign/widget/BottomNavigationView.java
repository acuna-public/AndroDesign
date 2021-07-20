	package ru.ointeractive.androdesign.widget;
	
	import android.content.Context;
	import android.content.res.TypedArray;
	import android.util.AttributeSet;
	import android.widget.ImageView;
	import android.widget.LinearLayout;
	
	import ru.ointeractive.androdesign.MenuInflater;
	import ru.ointeractive.androdesign.R;
	
	public class BottomNavigationView extends LinearLayout {
		
		protected int mMenu, mLayout;
		
		public BottomNavigationView (Context context) {
			this (context, null);
		}
		
		public BottomNavigationView (Context context, AttributeSet attrs) {
			
			super (context, attrs, 0);
			
			TypedArray array = getContext ().obtainStyledAttributes (attrs, R.styleable.BottomNavigationView);
			
			for (int i = 0; i < array.getIndexCount (); ++i) {
				
				int attr = array.getIndex (i);
				
				if (attr == R.styleable.BottomNavigationView_menu)
					setMenu (array.getResourceId (attr, 0));
				if (attr == R.styleable.BottomNavigationView_item_layout)
					setItemLayout (array.getResourceId (attr, 0));
				
			}
			
			array.recycle ();
			
			setOrientation (LinearLayout.HORIZONTAL);
			
			new MenuInflater (getContext ()).inflate (mMenu, new MenuState (getContext ()));
			
		}
		
		protected class MenuState extends ru.ointeractive.androdesign.MenuState {
			
			public MenuState (Context context) {
				super (context);
			}
			
			@Override
			protected void addItem () {
				
				inflate (getContext (), mLayout, BottomNavigationView.this);
				
				ImageView imageView = findViewById (R.id.icon);
				
			}
			
		}
		
		public void setMenu (int menu) {
			mMenu = menu;
		}
		
		public void setItemLayout (int layout) {
			mLayout = layout;
		}
		
	}