  package ru.ointeractive.androdesign;
  /*
   Created by Acuna on 12.12.2017
  */
  
  import android.content.Context;
  import android.graphics.Bitmap;
  import android.os.AsyncTask;
  import android.support.v7.widget.RecyclerView;
  
  import java.io.IOException;
  import java.net.URL;
  import java.util.ArrayList;
  import java.util.List;
  
  import ru.ointeractive.androdesign.adapter.ImagesAdapter;
  import ru.ointeractive.andromeda.graphic.Graphic;
  import ru.ointeractive.jabadaba.Int;
  import ru.ointeractive.jabadaba.exceptions.HttpRequestException;
  import ru.ointeractive.jabadaba.exceptions.OutOfMemoryException;
  
  public class DownloadImage extends AsyncTask<String, Integer, Bitmap> {
    
    private Context context;
    private RecyclerView recyclerView;
    
    private int layout, id;
    
    private List<String> errors = new ArrayList<> ();
    
    public DownloadImage (Context context, RecyclerView recyclerView, int layout, int id) {
      
      this.context = context;
      this.recyclerView = recyclerView;
      this.layout = layout;
      this.id = id;
      
    }
    
    @Override
    protected Bitmap doInBackground (String... argv) {
      
      Bitmap bMap = null;
      
      try {
        bMap = Graphic.toBitmap (new URL (argv[0]), ""); // TODO
      } catch (HttpRequestException | IOException | OutOfMemoryException e) {
        errors.add (e.getMessage ());
      }
      
      return bMap;
      
    }
    
    @Override
    protected void onPostExecute (Bitmap bMap) {
      
      if (Int.size (errors) == 0) {
        
        ImagesAdapter adapter = new ImagesAdapter (context, layout, id, bMap);
        
        recyclerView.setAdapter (adapter);
        
      }
      
    }
    
  }
