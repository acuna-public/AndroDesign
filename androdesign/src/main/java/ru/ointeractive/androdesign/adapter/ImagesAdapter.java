  package ru.ointeractive.androdesign.adapter;
  /*
   Created by Acuna on 09.11.2017
  */
  
  import android.content.Context;
  import android.graphics.Bitmap;
  import android.support.annotation.NonNull;
  import android.support.v7.widget.RecyclerView;
  import android.view.LayoutInflater;
  import android.view.View;
  import android.view.ViewGroup;
  import android.widget.ImageView;
  
  import ru.ointeractive.andromeda.graphic.Graphic;
  
  public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.CustomViewHolder> {
    
    private Context context;
    private int inflater, id;
    private Bitmap image;
    
    public ImagesAdapter (Context context, int inflater, int id, Bitmap image) {
      
      this.context = context;
      this.inflater = inflater;
      this.id = id;
      this.image = image;
      
    }
    
    class CustomViewHolder extends RecyclerView.ViewHolder {
      
      ImageView image;
      
      private CustomViewHolder (View view) {
        
        super (view);
        
        image = (ImageView) view.findViewById (id);
        
      }
      
    }
    
    @Override
    public void onBindViewHolder (@NonNull CustomViewHolder holder, final int position) {
      
      Graphic.ResizeData data = Graphic.getResizeData (image).doOrientationResize (context);
      
      holder.image.setImageBitmap (data.image);
      holder.image.setPadding (0, Graphic.getPaddingTop (context, data.height), 0, 0);
      
    }
    
    @Override
    public int getItemCount () {
      return 1;
    }
    
    @Override @NonNull
    public CustomViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
      return new CustomViewHolder (LayoutInflater.from (parent.getContext ()).inflate (inflater, parent, false));
    }
    
  }