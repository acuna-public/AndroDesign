  package ru.ointeractive.androdesign.adapter;
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
  import android.widget.CheckBox;
  import android.widget.CompoundButton;
  import android.widget.ImageView;
  import android.widget.SeekBar;
  import android.widget.TableLayout;
  import android.widget.TextView;
  
  import java.util.ArrayList;
  import java.util.List;
  import java.util.Map;
  
  import ru.ointeractive.androdesign.R;
  import ru.ointeractive.androdesign.widget.ListItem;
  import upl.core.Int;
  
  public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    
    private List<ListItem> items;
    private Layouts layouts = new Layouts ();
    private int inflater;
    
    private ItemListener itemListener;
    private TextListener textListener;
    private SeekbarItemListener seekbarListener;
    
    public static final int TITLE = 0;
    public static final int DESCR = 1;
    public static final int NAME = 2;
    
    public interface ItemListener {
      
      void onShow (ViewHolder holder, ListItem item);
      void onItemCheck (ListItem item);
      void onItemUncheck (ListItem item);
      void onClick (View view, ViewHolder holder, ListItem item);
      void onLongClick (View view, ViewHolder holder, ListItem item);
      
    }
    
    public interface TextListener {
      
      void onClick (View view, ViewHolder holder, ListItem item);
      void onLongClick (View view, ViewHolder holder, ListItem item);
      
    }
    
    public interface SeekbarItemListener {
      
      void onShow (ViewHolder holder, ListItem item);
      void onItemCheck (ListItem item);
      void onItemUncheck (ListItem item);
      void onClick (View view, ViewHolder holder, ListItem item);
      void onLongClick (View view, ViewHolder holder, ListItem item);
      
      void onStartTrackingTouch (ViewHolder holder, ListItem item);
      void onProgressChanged (ViewHolder holder, ListItem item, int progress);
      void onStopTrackingTouch (ViewHolder holder, ListItem item);
      
    }
    
    public ItemsAdapter addListener (ItemListener listener) {
      
      this.itemListener = listener;
      return this;
      
    }
    
    public ItemsAdapter addListener (TextListener listener) {
      
      this.textListener = listener;
      return this;
      
    }
    
    public ItemsAdapter addListener (SeekbarItemListener listener) {
      
      this.seekbarListener = listener;
      return this;
      
    }
    
    public ItemsAdapter (Activity activity, int inflater, int id, int text) {
      this (inflater, id, activity.getString (text));
    }
    
    public ItemsAdapter (int inflater, int id, Exception e) {
      this (inflater, id, e.getMessage ());
    }
    
    public ItemsAdapter (int inflater, int id, String text) {
      
      List<String> items = new ArrayList<> ();
      items.add (text);
      
      initList (inflater, id, items);
      
    }
    
    private void initList (int inflater, int id, List<String> items) {
      
      List<ListItem> listItems = new ArrayList<> ();
      
      for (String item : items)
        listItems.add (new ListItem ().setArgv (item));
      
      init (inflater, id, listItems);
      
    }
    
    public ItemsAdapter (int inflater, int id, List<String> items) {
      initList (inflater, id, items);
    }
    
    private void initList (Activity activity, int inflater, int id, Map<String, Integer> items) {
      
      List<ListItem> list = new ArrayList<> ();
      
      for (String key : items.keySet ())
        list.add (new ListItem ().setArgv (activity.getString (items.get (key)), key));
      
      init (inflater, id, list);
      
    }
    
    public ItemsAdapter (Activity activity, int inflater, int titleId, Map<String, Integer> items) {
      initList (activity, inflater, titleId, items);
    }
    
    private void init (int inflater, int titleId, List<ListItem> items) {
      
      layouts.title = titleId;
      
      init (layouts, inflater, items);
      
    }
    
    public static class Layouts {
      public int icon = 0, title = 0, descr = 0, version = 0, checkbox = 0, seekbar = 0;
    }
    
    public ItemsAdapter (int inflater, List<ListItem> items) {
      this (new Layouts (), inflater, items);
    }
    
    public ItemsAdapter (Layouts layouts, int inflater, List<ListItem> items) {
      init (layouts, inflater, items);
    }
    
    public ItemsAdapter (int inflater) {
      init (layouts, inflater, new ArrayList<ListItem> ());
    }
    
    private void init (Layouts layouts, int inflater, List<ListItem> items) {
      
      this.layouts = layouts;
      this.inflater = inflater;
      this.items = items;
      
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
      
      public ImageView icon;
      public TextView title, descr, version, seekbarValue;
      public CheckBox checkbox;
      public SeekBar seekbar;
      public TableLayout seekbarWrapper;
      
      private ViewHolder (View view) {
        
        super (view);
        
        if (layouts.icon > 0) icon = (ImageView) view.findViewById (layouts.icon);
        if (layouts.title > 0) title = (TextView) view.findViewById (layouts.title);
        if (layouts.descr > 0) descr = (TextView) view.findViewById (layouts.descr);
        if (layouts.version > 0) version = (TextView) view.findViewById (layouts.version);
        if (layouts.checkbox > 0) checkbox = (CheckBox) view.findViewById (layouts.checkbox);
        
        if (layouts.seekbar > 0) {
          
          seekbar = (SeekBar) view.findViewById (layouts.seekbar);
          seekbarWrapper = (TableLayout) view.findViewById (R.id.seekbar_wrapper);
          seekbarValue = (TextView) view.findViewById (R.id.seekbar_value);
          
        }
        
      }
      
    }
    
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
      return new ViewHolder (LayoutInflater.from (parent.getContext ()).inflate (inflater, parent, false));
    }
    
    @Override
    public void onBindViewHolder (@NonNull final ViewHolder holder, int position) {
      
      try {
        
        final ListItem item = items.get (position);
        
        if (holder.icon != null) {
          
          //if (item.icon != null)
            holder.icon.setImageBitmap (item.icon);
          
        }
        
        if (holder.title != null)
          holder.title.setText (item.getArgv (0));
        
        if (holder.descr != null)
          holder.descr.setText (item.getArgv (1));
        
        if (item.argvNum () > 3 && holder.version != null)
          holder.version.setText (item.getArgv (3));
        
        if (holder.checkbox != null) {
          
          holder.checkbox.setEnabled (item.active);
          holder.checkbox.setVisibility ((item.argvNum () > 2 && item.checkbox) ? View.VISIBLE : View.GONE);
          
          holder.checkbox.setBackgroundResource (item.drawable);
          
          holder.checkbox.setOnClickListener (new View.OnClickListener () {
            
            @Override
            public void onClick (View view) {
              checkboxesClick (holder, item);
            }
            
          });
          
          holder.checkbox.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener () {
            
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
              
              // Чиним снимание чекбоксов при скролле
              
              int position = (int) buttonView.getTag ();
              if (Int.size (items) > 0) items.get (position).setChecked (buttonView.isChecked ());
              
              //
              
            }
            
          });
          
          // Чиним снимание чекбоксов при скролле
          
          holder.checkbox.setTag (position);
          holder.checkbox.setChecked (item.checked);
          
          //
          
        }
        
        holder.itemView.setOnClickListener (new View.OnClickListener () {
          
          @Override
          public void onClick (View view) {
            
            if (textListener != null)
              textListener.onClick (view, holder, item); // TODO
            else if (seekbarListener != null)
              seekbarListener.onClick (view, holder, item);
            else if (itemListener != null)
              itemListener.onClick (view, holder, item);
            
            if (holder.checkbox != null && item.checkbox) {
              
              holder.checkbox.setChecked (!holder.checkbox.isChecked ());
              checkboxesClick (holder, item);
              
            }
            
          }
          
        });
        
        holder.itemView.setOnLongClickListener (new AdapterView.OnLongClickListener () {
          
          @Override
          public boolean onLongClick (View view) {
            
            if (textListener != null)
              textListener.onLongClick (view, holder, item);
            else if (seekbarListener != null)
              seekbarListener.onLongClick (view, holder, item);
            else if (itemListener != null)
              itemListener.onLongClick (view, holder, item);
            
            return true;
            
          }
          
        });
        
        if (holder.seekbar != null) {
          
          if (item.seekVal > -1) {
            
            holder.descr.setVisibility (View.GONE);
            holder.seekbarWrapper.setVisibility (View.VISIBLE);
            
            holder.seekbar.setMax (item.seekMaxVal);
            holder.seekbar.setProgress (item.seekVal);
            
            holder.seekbarValue.setText (String.valueOf (item.seekVal));
            
            holder.seekbar.setOnSeekBarChangeListener (new SeekBar.OnSeekBarChangeListener () {
              
              @Override
              public void onStartTrackingTouch (SeekBar seekBar) {
                seekbarListener.onStartTrackingTouch (holder, item);
              }
              
              @Override
              public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
                
                if (progress < item.seekMinVal)
                  seekBar.setProgress (item.seekMinVal);
                else
                  seekbarListener.onProgressChanged (holder, item, progress);
                
              }
              
              @Override
              public void onStopTrackingTouch (SeekBar seekBar) {
                seekbarListener.onStopTrackingTouch (holder, item);
              }
              
            });
            
          } else {
            
            holder.descr.setVisibility (View.VISIBLE);
            holder.seekbarWrapper.setVisibility (View.GONE);
            
          }
          
        }
        
        if (seekbarListener != null)
          seekbarListener.onShow (holder, item);
        else if (itemListener != null)
          itemListener.onShow (holder, item);
        
      } catch (IndexOutOfBoundsException e) { // TODO
        // empty
      }
      
    }
    
    private void checkboxesClick (ViewHolder holder, ListItem item) {
      
      if (holder.checkbox.isChecked ()) {
        
        if (seekbarListener != null)
          seekbarListener.onItemCheck (item);
        else if (itemListener != null)
          itemListener.onItemCheck (item);
        
      } else {
        
        if (seekbarListener != null)
          seekbarListener.onItemUncheck (item);
        else if (itemListener != null)
          itemListener.onItemUncheck (item);
        
      }
      
    }
    
    @Override
    public long getItemId (int position) {
      return position;
    }
    
    @Override
    public int getItemCount () {
      return Int.size (items);
    }
    
  }