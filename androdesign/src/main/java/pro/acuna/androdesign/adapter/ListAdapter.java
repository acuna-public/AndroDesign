  package pro.acuna.androdesign.adapter;
  /*
   Created by Acuna on 03.07.2017
  */
  
  import android.app.Activity;
  import android.support.annotation.NonNull;
  import android.support.v7.widget.RecyclerView;
  import android.view.LayoutInflater;
  import android.view.View;
  import android.view.ViewGroup;
  import android.widget.AdapterView;
  import android.widget.TextView;
  
  import java.util.ArrayList;
  import java.util.List;
  import java.util.Map;
  
  import pro.acuna.androdesign.widget.ListItem;
  import pro.acuna.jabadaba.Int;
  
  public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    
    private List<ListItem> items;
    private static Integer[] layouts;
    private int inflater;
    
    private Activity activity;
    private ItemListener listener;
    
    public interface ItemListener {
      
      void onClick (View view, ViewHolder holder, ListItem item);
      void onLongClick (View view, ViewHolder holder, ListItem item);
      
    }
    
    public ListAdapter addListener (ItemListener listener) {
      
      this.listener = listener;
      return this;
      
    }
    
    public ListAdapter (Activity activity, int inflater, int id, int text) {
      this (activity, inflater, id, activity.getString (text));
    }
    
    public ListAdapter (Activity activity, int inflater, int id, Exception e) {
      this (activity, inflater, id, e.toString ());
    }
    
    public ListAdapter (Activity activity, int inflater, int id, String text) {
      
      List<String> items = new ArrayList<> ();
      items.add (text);
      
      initList (activity, inflater, id, items);
      
    }
    
    private void initList (Activity activity, int inflater, int id, List<String> items) {
      
      List<ListItem> listItems = new ArrayList<> ();
      
      for (String item : items)
        listItems.add (new ListItem ().setArgv (item));
      
      init (activity, inflater, id, listItems);
      
    }
    
    private void initList (Activity activity, int inflater, int id, Map<String, Integer> items) {
      
      List<ListItem> list = new ArrayList<> ();
      
      for (String key : items.keySet ())
        list.add (new ListItem ().setArgv (activity.getString (items.get (key)), key));
      
      init (activity, inflater, id, list);
      
    }
    
    public ListAdapter (Activity activity, int inflater, int id, Map<String, Integer> items) {
      initList (activity, inflater, id, items);
    }
    
    private void init (Activity activity, int inflater, int id, List<ListItem> items) {
      
      Integer[] layouts = {
        
        id
        
      };
      
      init (activity, inflater, layouts, items);
      
    }
    
    public ListAdapter (Activity activity, int inflater, int id, List<ListItem> items) {
      init (activity, inflater, id, items);
    }
    
    private void init (Activity activity, int inflater, Integer[] layouts, List<ListItem> items) {
      
      this.activity = activity;
      this.layouts = layouts;
      this.items = items;
      this.inflater = inflater;
      
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
      
      public TextView title;
      
      private ViewHolder (View view) {
        
        super (view);
        
        title = view.findViewById (layouts[0]);
        
      }
      
    }
    
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
      return new ViewHolder (LayoutInflater.from (parent.getContext ()).inflate (inflater, parent, false));
    }
    
    @Override
    public void onBindViewHolder (@NonNull final ViewHolder holder, int position) {
      
      final ListItem item = items.get (position);
      
      holder.title.setText (item.getArgv (0));
      
      holder.itemView.setOnClickListener (new View.OnClickListener () {
        
        @Override
        public void onClick (View view) {
          
          if (listener != null)
            listener.onClick (view, holder, item);
          
        }
        
      });
      
      holder.itemView.setOnLongClickListener (new AdapterView.OnLongClickListener () {
        
        @Override
        public boolean onLongClick (View view) {
          
          if (listener != null)
            listener.onLongClick (view, holder, item);
          
          return true;
          
        }
        
      });
      
    }
    
    @Override
    public int getItemCount () {
      return Int.size (items);
    }
    
  }