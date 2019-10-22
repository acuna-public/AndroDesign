  package pro.acuna.androdesign;
  /*
   Created by Acuna on 10.11.2017
  */
  
  import android.os.Bundle;
  import android.support.annotation.NonNull;
  import android.support.v4.app.Fragment;
  import android.support.v4.view.ViewPager;
  import android.support.v7.app.AppCompatActivity;
  import android.support.v7.widget.LinearLayoutManager;
  import android.support.v7.widget.RecyclerView;
  import android.view.LayoutInflater;
  import android.view.View;
  import android.view.ViewGroup;
  
  import java.util.List;
  
  import pro.acuna.androdesign.adapter.ImagesAdapter;
  import pro.acuna.androdesign.adapter.ViewPagerAdapter;
  import pro.acuna.andromeda.Graphic;
  import pro.acuna.jabadaba.Int;
  
  public class ImageActivity extends AppCompatActivity {
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
      
      super.onCreate (savedInstanceState);
      
      setContentView (R.layout.activity_full);
      
      Bundle bundle = getIntent ().getExtras ();
      
      ViewPager viewPager = findViewById (R.id.view_pager);
      ViewPagerAdapter pager = new ViewPagerAdapter (this, getSupportFragmentManager ());
      
      ImagesFragment fragment = new ImagesFragment ();
      
      if (bundle != null) {
        
        List<String> images = bundle.getStringArrayList ("images");
        
        if (images != null) {
          
          if (Int.size (images) > 0) {
            
            for (String image : images)
              pager.addTab (image, fragment.newInstance (image));
            
          } else {
            
            String image = images.get (bundle.getInt ("position"));
            pager.addTab (image, fragment.newInstance (image));
            
          }
          
        } else {
          
          String image = bundle.getString ("image");
          pager.addTab (image, fragment.newInstance (image));
          
        }
        
        viewPager.setAdapter (pager);
        viewPager.setCurrentItem (bundle.getInt ("position"));
        
      }
      
    }
    
    public static class ImagesFragment extends Fragment {
      
      private ImagesFragment newInstance (String url) {
        
        Bundle bundle = new Bundle ();
        
        bundle.putString ("url", url);
        
        ImagesFragment fragment = new ImagesFragment ();
        fragment.setArguments (bundle);
        
        return fragment;
        
      }
      
      @Override
      public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate (R.layout.content_image, container, false);
        
        RecyclerView recyclerView = view.findViewById (R.id.recycler_view);
        
        recyclerView.setLayoutManager (new LinearLayoutManager (getContext ()));
        recyclerView.setAdapter (new ImagesAdapter (getContext (), R.layout.list_image, R.id.img_full, Graphic.toBitmap (getContext (), R.drawable.loading)));
        
        new DownloadImage (getContext (), recyclerView, R.layout.list_image, R.id.img_full).execute (getArguments ().getString ("url"));
        
        return view;
        
      }
      
    }
    
  }