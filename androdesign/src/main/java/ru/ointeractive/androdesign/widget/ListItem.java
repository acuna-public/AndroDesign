  package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 17.08.2016
  */
  
  import android.graphics.Bitmap;
 
  import org.json.JSONArray;
  import org.json.JSONException;
  import org.json.JSONObject;
  
  import ru.ointeractive.jabadaba.Arrays;
  import ru.ointeractive.jabadaba.Int;
  
  public class ListItem {
    
    public Bitmap image, icon;
    public String[] params;
    private JSONArray JSONArrayParams;
    private JSONObject JSONObjParams;
    public boolean checked = false, checkbox = false, active = true;
    public int id = 0, drawable = 0, seekVal = -1, seekMinVal = 0, seekMaxVal = 100;
    
    public ListItem () {}
    
    public ListItem (ListItem item) {
      
      icon = item.icon;
      image = item.image;
      params = item.params;
      checked = item.checked;
      id = item.id;
      active = item.active;
      drawable = item.drawable;
      seekVal = item.seekVal;
      
    }
    
    public ListItem setChecked (boolean checked) {
      
      this.checked = checked;
      return this;
      
    }
    
    public ListItem setCheckbox (boolean checkbox) {
      
      this.checkbox = checkbox;
      return this;
      
    }
    
    public ListItem setId (int id) {
      
      this.id = id;
      return this;
      
    }
    
    public ListItem setIcon (Bitmap icon) {
      
      this.icon = icon;
      return this;
      
    }
    
    public ListItem setImage (Bitmap image) {
      
      this.image = image;
      return this;
      
    }
    
    public ListItem setActive (boolean active) {
      
      this.active = active;
      return this;
      
    }
    
    public ListItem setDrawable (int drawable) {
      
      this.drawable = drawable;
      return this;
      
    }
    
    public ListItem setSeekbar (int value) {
      
      this.seekVal = value;
      return this;
      
    }
    
    public ListItem setSeekbarMinValue (int value) {
      
      this.seekMinVal = value;
      return this;
      
    }
    
    public ListItem setSeekbarMaxValue (int value) {
      
      this.seekMaxVal = value;
      return this;
      
    }
    
    public int argvNum () {
      
      if (JSONArrayParams != null)
        return Int.size (JSONArrayParams);
      else if (JSONObjParams != null)
        return Int.size (JSONObjParams);
      else
        return Int.size (params);
      
    }
    
    public ListItem setArgv (String... argv) {
      
      this.params = argv;
      return this;
      
    }
    
    public ListItem setArgv (JSONArray argv) {
      
      JSONArrayParams = argv;
      return this;
      
    }
    
    public ListItem setArgv (JSONObject argv) {
      
      JSONObjParams = argv;
      return this;
      
    }
    
    public void setArgv (int id, int value) throws JSONException {
      setArgv (id, String.valueOf (value));
    }
    
    public void setArgv (int id, String value) throws JSONException {
      
      if (JSONArrayParams != null)
        JSONArrayParams.put (id, value);
      else if (JSONObjParams != null)
        JSONObjParams.put (Arrays.getKey (id, JSONObjParams), value);
      else
        params[id] = value;
      
    }
    
    public String getArgv (int id) {
      
      try {
        
        if (JSONArrayParams != null)
          return String.valueOf (JSONArrayParams.get (id));
        else if (JSONObjParams != null)
          return String.valueOf (JSONObjParams.get (Arrays.getKey (id, JSONObjParams)));
        else
          return params[id];
        
      } catch (JSONException e) {
        return e.getMessage ();
      } catch (ArrayIndexOutOfBoundsException e) {
        return "";
      }
      
    }
    
    @Override
    public String toString () {
      return getArgv (0);
    }
    
  }