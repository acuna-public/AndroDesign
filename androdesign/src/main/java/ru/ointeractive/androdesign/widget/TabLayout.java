  package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 18.01.2018
  */
  
  import android.content.Context;
  import android.util.AttributeSet;
  
  import ru.ointeractive.androdesign.adapter.ViewPagerAdapter;
  
  public class TabLayout extends android.support.design.widget.TabLayout {
    
    ViewPagerAdapter adapter;
    ViewPager pager;
    int mPosition = 0;
    
    public TabLayout (Context context) {
      super (context);
    }
    
    public TabLayout (Context context, AttributeSet attrs) {
      super (context, attrs);
    }
    
    public TabLayout setupWithViewPager (final ViewPager pager) {
      
      this.pager = pager;
      adapter = pager.getAdapter ();
      
      for (String title : adapter.mFragmentTitleList)
        addTab (newTab ().setText (title));
      
      TabLayout.Tab tab = getTabAt (pager.getCurrentItem ());
      if (tab != null) tab.select ();
      
      pager.setOnPageChangeListener (new ViewPager.OnPageChangeListener () {
        
        @Override
        public void onPageScrollStateChanged (int state) {}
        
        @Override
        public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {}
        
        @Override
        public void onPageSelected (int position) {
          
          mPosition = position;
          
          TabLayout.Tab tab = getTabAt (mPosition);
          if (tab != null) tab.select ();
          
          pager.setCurrentItem (mPosition);
          
        }
        
      });
      
      addOnTabSelectedListener (new TabLayout.OnTabSelectedListener () {
        
        @Override
        public void onTabSelected (TabLayout.Tab tab) {
          
          mPosition = tab.getPosition ();
          
          pager.setCurrentItem (mPosition, false);
          pager.getAdapter ().notifyDataSetChanged ();
          
        }
        
        @Override
        public void onTabUnselected (TabLayout.Tab tab) {}
        
        @Override
        public void onTabReselected (TabLayout.Tab tab) {}
        
      });
      
      return this;
      
    }
    
  }