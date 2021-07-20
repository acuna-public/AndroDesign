  package ru.ointeractive.androdesign;
  /*
   Created by Acuna on 28.05.2019
  */
  
  public class Application extends android.app.Application {
    
    @Override
    public void onCreate () {
      
      super.onCreate ();
      
      Andromeda andro = new Andromeda (getApplicationContext (), "Andromeda");
      andro.uncaughtException ();
      
    }
    
  }