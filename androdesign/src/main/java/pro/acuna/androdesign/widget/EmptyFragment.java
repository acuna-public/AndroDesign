  package pro.acuna.androdesign.widget;
  /*
   Created by Acuna on 14.09.2017
  */
  
  import android.os.Bundle;
  import android.support.v4.app.Fragment;
  import android.support.v7.widget.DefaultItemAnimator;
  import android.support.v7.widget.LinearLayoutManager;
  import android.support.v7.widget.RecyclerView;
  import android.view.LayoutInflater;
  import android.view.View;
  import android.view.ViewGroup;
  
  import pro.acuna.androdesign.adapter.ItemsAdapter;
  
  public class EmptyFragment extends Fragment {
    
    public static EmptyFragment newInstance (int layout, int id, int emptyLayout, int view, int message) {
      
      Bundle bundle = new Bundle ();
      
      bundle.putInt ("layout", layout);
      bundle.putInt ("id", id);
      bundle.putInt ("empty_layout", emptyLayout);
      bundle.putInt ("view", view);
      bundle.putInt ("message", message);
      
      EmptyFragment fragment = new EmptyFragment ();
      fragment.setArguments (bundle);
      
      return fragment;
      
    }
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
      super.onCreate (savedInstanceState);
    }
    
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      
      View view = inflater.inflate (getArguments ().getInt ("layout"), container, false);
      
      RecyclerView recyclerView = view.findViewById (getArguments ().getInt ("view"));
      
      recyclerView.setLayoutManager (new LinearLayoutManager (getActivity ()));
      recyclerView.addItemDecoration (new DividerItemDecoration (getActivity (), LinearLayoutManager.VERTICAL));
      recyclerView.setItemAnimator (new DefaultItemAnimator ());
      
      int id = getArguments ().getInt ("id");
      int layout = getArguments ().getInt ("empty_layout");
      int message = getArguments ().getInt ("message");
      
      ItemsAdapter adapter = new ItemsAdapter (getActivity (), layout, id, message);
      recyclerView.setAdapter (adapter);
      
      return view;
      
    }
    
  }