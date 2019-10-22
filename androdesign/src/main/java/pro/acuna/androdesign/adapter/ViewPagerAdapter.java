  package pro.acuna.androdesign.adapter;
  /*
   Created by Acuna on 12.12.2017
  */
  
  import android.content.Context;
  import android.support.v4.app.Fragment;
  import android.support.v4.app.FragmentManager;
  import android.support.v4.app.FragmentStatePagerAdapter;
  
  import java.util.ArrayList;
  import java.util.List;
  
  import pro.acuna.jabadaba.Int;
  
  public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    
    public List<String> mFragmentTitleList = new ArrayList<> ();
    public List<Fragment> mFragmentList = new ArrayList<> ();
    
    private Context context;
    
    public ViewPagerAdapter (Context context, FragmentManager manager) {
      
      super (manager);
      this.context = context;
      
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
      addTab (context.getString (title), fragment);
    }
    
    public void addTab (Fragment fragment) {
      mFragmentList.add (fragment);
    }
    
    public void addTab (String title, Fragment fragment) {
      
      mFragmentTitleList.add (title);
      mFragmentList.add (fragment);
      
    }
    
  }