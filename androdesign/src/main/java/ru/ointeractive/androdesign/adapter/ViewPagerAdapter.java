  package ru.ointeractive.androdesign.adapter;
  /*
   Created by Acuna on 12.12.2017
  */
  
  import android.support.v4.app.Fragment;
  import android.support.v4.app.FragmentManager;
  import android.support.v4.app.FragmentStatePagerAdapter;
  import android.support.v7.app.AppCompatActivity;
	
  import java.util.ArrayList;
  import java.util.List;
  
  import upl.core.Int;
  
  public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    
    public List<String> mFragmentTitleList = new ArrayList<> ();
    public List<Fragment> mFragmentList = new ArrayList<> ();
    
    private AppCompatActivity mActivity;
    
    public ViewPagerAdapter (AppCompatActivity activity, FragmentManager manager) {
      
      super (manager);
      mActivity = activity;
      
    }
    
    @Override
    public Fragment getItem (int position) {
      return mFragmentList.get (position);
    }
    
    @Override
    public int getCount () {
      return Int.size (mFragmentList);
    }
    
    @Override
    public CharSequence getPageTitle (int position) {
      return mFragmentTitleList.get (position);
    }
    
    public void addTab (int title, Fragment fragment) {
      addTab (mActivity.getString (title), fragment);
    }
    
    public void addTab (Fragment fragment) {
      mFragmentList.add (fragment);
    }
    
    public void addTab (String title, Fragment fragment) {
      
      mFragmentTitleList.add (title);
      addTab (fragment);
      
    }
    
    public void changeTab (int tab, Fragment fragment) {
      mFragmentList.set (tab, fragment);
    }
    
    public void clear () {
    	mFragmentList = new ArrayList<> ();
    }
    
    public boolean isEnd (int position) {
    	return (position + 1 == getCount ());
    }
    
    public Fragment getFragment (int position) {
      return mFragmentList.get (position);
	  }
   
  }