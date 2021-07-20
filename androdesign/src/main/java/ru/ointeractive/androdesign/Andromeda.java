  package ru.ointeractive.androdesign;
  /*
   Created by Acuna on 02.02.2018
  */
  
  import android.app.Activity;
  import android.app.AlertDialog;
  import android.content.Context;
  import android.os.Build;
  import android.support.v7.widget.LinearLayoutManager;
  import android.widget.TextView;
  
  import org.json.JSONArray;
  import org.json.JSONException;
  import org.json.JSONObject;
  
  import java.io.File;
  import java.io.FileInputStream;
  import java.io.FileNotFoundException;
  import java.io.IOException;
  import java.io.InputStream;
  import java.util.ArrayList;
  import java.util.LinkedHashMap;
  import java.util.List;
  import java.util.Map;
  
  import ru.ointeractive.androdesign.widget.RecyclerView;
  import ru.ointeractive.androdesign.widget.DividerItemDecoration;
  import ru.ointeractive.andromeda.apps.AppsManager;
  import ru.ointeractive.andromeda.DB;
  import ru.ointeractive.andromeda.Device;
  import ru.ointeractive.andromeda.Files;
  import ru.ointeractive.andromeda.graphic.Graphic;
  import ru.ointeractive.andromeda.OS;
  import ru.ointeractive.andromeda.apps.PackageData;
  import ru.ointeractive.andromeda.Prefs;
  import ru.ointeractive.andromeda.System;
  import ru.ointeractive.jabadaba.Arrays;
  import ru.ointeractive.jabadaba.Crypto;
  import ru.ointeractive.jabadaba.HttpRequest;
  import ru.ointeractive.jabadaba.Int;
  import ru.ointeractive.jabadaba.Locales;
  import ru.ointeractive.jabadaba.Net;
  import ru.ointeractive.jabadaba.Strings;
  import ru.ointeractive.jabadaba.exceptions.HttpRequestException;
  import ru.ointeractive.jabadaba.exceptions.OutOfMemoryException;
  
  public class Andromeda {
    
    public final static String PREF_DATA_DIR = "data_dir";
    public final static String PREF_LANG = "lang";
    public final static String PREF_APPS_CARDS = "apps_cards";
    public final static String PREF_BACKUPS_AUTO = "backups_auto";
    public final static String PREF_BACKUPS_AUTO_START_TIME = "backups_auto_start_time";
    public final static String PREF_BACKUPS_CRYPTO_ALGO = "backups_crypto_algo";
    public final static String PREF_BACKUPS_EXTERNAL = "backups_external";
    public final static String PREF_BACKUPS_INTERVAL = "backups_interval";
    public final static String PREF_BACKUPS_PASSWORD = "backups_password";
    public final static String PREF_BACKUPS_SYMLINKS = "backups_symlinks";
    public final static String PREF_UPDATE_REPOS_INTERVAL = "update_repos_interval";
    public final static String PREF_UPDATE_REPOS_START_TIME = "update_repos_start_time";
    public final static String PREF_ONLY_WIFI = "only_wifi";
    public final static String PREF_ROOT = "root";
    public final static String PREF_THEME = "theme";
    public final static String PREF_BACKUPS_NUM = "backups_num";
    public final static String PREF_BACKUPS_ENCRYPT = "backups_encrypt";
	  public final static String PREF_VERSION = "version";
	  public final static String PREF_LAUNCHED = "launched";
    
    public final static String THEME_DARK = "Dark";
    public final static String THEME_HOLO_DARK = "Holo Dark";
    public final static String THEME_LIGHT = "Light";
    public final static String THEME_TERMINAL = "Terminal";
    
    public static String FILE_REPOS = "repos.json";
    public static String FILE_BACKUPS = "backups.json";
    
    public static String FOLDER_BACKUPS = "backups";
    
    public boolean offline = false;
    private boolean loaded = false, updated;
    
    private final Map<String, Integer> themes = new LinkedHashMap<> ();
    
    public Context context;
    public Activity activity;
    public PackageData app;
    
    public AppsManager apps;
    public Prefs prefs;
    
    public String lang, userAgent, backupsAlgo, theme;
    public boolean isRoot, isThemeHolo, appsCards;
    public int orientation, backupsTime;
    public int prefBackupsNum;
    public JSONObject dialogsOpened;
    
    public List<String> langs = new ArrayList<> ();
    public Map<String, Map<String, String>> locales;
    public Map<String, String> langTitles;
    public String[] langTitlesArray;
    
    public Andromeda (Context context) {
      this (context, "andromeda");
    }
    
    public Andromeda (Context context, int appName) {
      this (context, context.getString (appName));
    }
    
    public Andromeda (Context context, String appName) {
      
      this.context = context;
      
      apps = new AppsManager (context);
      app = apps.getPackageData ();
      
      prefs = new Prefs (context, Strings.altName (appName));
      
      userAgent = OS.getUserAgent (context, appName);
      
      lang = prefs.get (PREF_LANG, Locales.getLang ());
      
      langs.add ("en");
      langs.add (lang);
      
      locales = Locales.getLocalesData (langs);
      
      langTitles = getLangTitles (new LinkedHashMap<> ());
      langTitlesArray = getLangTitles ();
      
      backupsAlgo = prefs.get (PREF_BACKUPS_CRYPTO_ALGO, Crypto.symmetricAlgorims[0]);
      backupsTime = backupsInterval (prefs.get (PREF_BACKUPS_INTERVAL, 1));
      
      isRoot = prefs.get (PREF_ROOT, false);
      
      orientation = Device.getOrientation (context);
      prefBackupsNum = prefs.get (PREF_BACKUPS_NUM, 0);
      
      theme = prefs.get (PREF_THEME, THEME_LIGHT);
      
      isThemeHolo = theme.equals (THEME_HOLO_DARK);
	
	    prefs.setDefPref (PREF_ROOT, isRoot);
	    prefs.setDefPref (PREF_ONLY_WIFI, true);
	    prefs.setDefPref (PREF_BACKUPS_EXTERNAL, false);
	    prefs.setDefPref (PREF_BACKUPS_SYMLINKS, false);
	    prefs.setDefPref (PREF_APPS_CARDS, false);
      
      appsCards = prefs.getBool (PREF_APPS_CARDS);
      
      try {
        dialogsOpened = prefs.get ("dialogs_opened", new JSONObject ());
      } catch (JSONException e) {
        // empty
      }
      
      updated = (prefs.getInt (PREF_VERSION) < app.versionCode);
      
    }
    
    public Andromeda init (Activity activity) {
      
      this.activity = activity;
      
      if (!loaded) {
        
        loaded = true;
        
        themes.put (THEME_LIGHT, R.style.Light);
        themes.put (THEME_DARK, R.style.Dark);
        themes.put (THEME_TERMINAL, R.style.Terminal);
        themes.put (THEME_HOLO_DARK, R.style.HoloDark);
        
        setTheme (theme);
        
      }
      
      return this;
      
    }
    
    public String[] getThemes () {
      
      String[] output = new String[Int.size (themes)];
      
      int i = 0;
      
      for (String key : themes.keySet ()) {
        
        output[i] = key;
        ++i;
        
      }
      
      return output;
      
    }
    
    public void setTheme (String theme) { // TODO: Check others
      
      activity.setTheme (themes.get (theme));
      prefs.set (PREF_THEME, theme);
      
      this.theme = theme;
      
    }
    
    String[] getLangTitles () {
      
      String[] langs = new String[Int.size (locales)];
      
      for (int i = 0; i < Int.size (locales); ++i)
        langs[i] = locales.get (this.langs.get (i)).get (Locales.LANG_DISPLAY);
      
      return langs;
      
    }
    
    public Map<String, String> getLangTitles (Map<String, String> langs) {
      
      for (int i = 0; i < Int.size (locales); ++i) {
        
        Map<String, String> locale = locales.get (this.langs.get (i));
        langs.put (this.langs.get (i), locale.get (Locales.LANG_DISPLAY));
        
      }
      
      return langs;
      
    }
    
    public RecyclerView recyclerView (int view) {
      return recyclerView (view, false);
    }
    
    public RecyclerView recyclerView (int view, boolean cards) {
      
      RecyclerView recyclerView = (RecyclerView) activity.findViewById (view);
      return recyclerView (recyclerView, cards);
      
    }
    
    public RecyclerView recyclerView (RecyclerView recyclerView) {
      return recyclerView (recyclerView, false);
    }
    
    public RecyclerView recyclerView (RecyclerView recyclerView, boolean cards) {
      
      recyclerView.setLayoutManager (new LinearLayoutManager (activity));
      
      if (!cards) {
        
        DividerItemDecoration decorator = new DividerItemDecoration (activity, LinearLayoutManager.VERTICAL);
        
        if (isThemeHolo) {
          
          decorator.setDrawable (Graphic.toDrawable (activity, R.drawable.line_divider));
          decorator.setPadding (20, 0, 20, 0);
          
        }
        
        recyclerView.addItemDecoration (decorator);
        //recyclerView.setItemAnimator (new DefaultItemAnimator ());
        
      }
      
      return recyclerView;
      
    }
    
    public String backupsPassword () throws Crypto.DecryptException {
      return prefs.get (PREF_BACKUPS_PASSWORD, "", true);
    }
    
    public JSONArray repoData () throws JSONException, IOException, OutOfMemoryException {
      return new JSONArray (Files.read (context, FILE_REPOS));
    }
    
    public JSONArray repoData (int id) throws JSONException, IOException, OutOfMemoryException {
      
      JSONArray items = repoData ();
      return items.getJSONArray (id);
      
    }
    
    public JSONArray repoData (int id, int type) throws JSONException, IOException, OutOfMemoryException {
      
      JSONArray items = repoData (id);
      return items.getJSONArray (type);
      
    }
    
    public int backupsInterval (int backupsTime) {
      return (backupsTime > 0 ? (3600 * 24 * backupsTime * 1000) : backupsTime);
    }
    
    public String getExternalFilesDir () {
      return lastSaveDir (PREF_DATA_DIR);
    }
    
    public String getExternalFilesDir (String... file) {
      return getExternalFilesDir () + "/" + Arrays.implode ("/", file);
    }
    
    public String lastSaveDir (String item) {
      return prefs.get (item, OS.getExternalFilesDir (context, true));
    }
    
    public String[] prepComp (String comp) {
      return Arrays.pregExplode ("\\s+", comp);
    }
    
    public JSONArray jsonDate () {
      
      JSONArray output = new JSONArray ();
      
      output.put (Locales.date (false));
      output.put (Locales.time (false));
      
      return output;
      
    }
    
    public void deleteDir (Map<String, String> urlData) throws IOException {
      
      String dir = getExternalFilesDir (urlData.get ("domain"));
      ru.ointeractive.jabadaba.Files.delete (dir);
      
    }
    
    public boolean isConnected () {
      return Device.isConnected (context);
    }
    
    public boolean isOnline () {
      return Device.isOnline (context, true);
    }
    
    public JSONObject fileChecksum (File file) throws Crypto.EncryptException, JSONException, FileNotFoundException {
      return fileChecksum (new FileInputStream (file));
    }
    
    public JSONObject fileChecksum (InputStream file) throws Crypto.EncryptException, JSONException {
      return fileChecksum (file, Arrays.toJSONArray (new String[] {Crypto.DIGEST_SHA1, Crypto.DIGEST_MD5}));
    }
    
    public JSONObject fileChecksum (File file, JSONArray checksums) throws Crypto.EncryptException, JSONException, FileNotFoundException {
      return fileChecksum (new FileInputStream (file), checksums);
    }
    
    public JSONObject fileChecksum (InputStream file, JSONArray checksums) throws Crypto.EncryptException, JSONException {
      
      JSONObject array = new JSONObject ();
      
      for (int i = 0; i < Int.size (checksums); ++i) {
        
        String key = checksums.getString (i);
        array.put (DB.prepColName (key), Crypto.digest (key, file));
        
      }
      
      return array;
      
    }
    
    public HttpRequest openConn (String url) throws HttpRequestException {
      return Net.request (url, userAgent, 5000);
    }
    
    public int dialogStyle () {
      
      if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        return (theme.equals ("Light") ? R.style.DialogLight : R.style.DialogDark); // TODO Make transparent not in AndroDesign
      else
        return (theme.equals ("Light") ? R.style.TransparentDialogLight : R.style.TransparentDialogDark);
      
    }
    
    public void uncaughtException () {
      
      Thread.setDefaultUncaughtExceptionHandler (new ru.ointeractive.jabadaba.System.ExceptionHandler (new ru.ointeractive.jabadaba.System.ExceptionListener () {
        
        @Override
        public void uncaughtException (Thread t, Throwable e) {
          
          System.error (context, e, userAgent, "crash", false);
          System.killProcess ();
          
        }
        
      }));
      
    }
    
    public void checkPermissions () {
      
      if (updated) {
        
        ru.ointeractive.androdesign.OS.checkPermissions (activity, apps.getPermissions ());
        
        prefs.set (PREF_VERSION, app.versionCode);
        updated = false;
        
      }
      
    }
    
    public boolean isLaunched () {
      return prefs.getBool (PREF_LAUNCHED);
    }
    
    public void setLaunched () {
      prefs.set (PREF_LAUNCHED, true);
    }
		
	  public boolean isUpdated () {
		  return updated;
	  }
		
	  public void setUpdated () {
    
		  prefs.set (PREF_VERSION, app.versionCode);
		  updated = true;
		  
	  }
		
	  public void rootInit () {
      
      isRoot = Device.isRoot ();
      prefs.set (PREF_ROOT, isRoot);
      
    }
    
    public boolean checkRoot () {
      return prefs.getBool (PREF_ROOT);
    }
    
    public void error (Activity activity, Exception e) {
      error (activity, e.getMessage ());
    }
    
    public void error (Activity activity, List<?> items) {
      error (activity, Arrays.implode (items));
    }
    
    public void error (Activity activity, int mess) {
      error (activity, activity.getString (mess));
    }
    
    public void error (Activity activity, String mess) {
      UI.dialog (activity, dialogStyle (), R.string.title_error, mess);
    }
    
    public void error (Activity activity, String msg, UI.DialogNegInterface listener) {
      UI.dialog (activity, dialogStyle (), R.string.title_error, msg, listener, android.R.string.ok, android.R.string.cancel);
    }
    
    public List<String> getReposData (JSONArray repos, int key) throws JSONException {
      return getReposData (repos, key, -1);
    }
    
    public List<String> getReposData (JSONArray repos, int key, int id) throws JSONException {
      
      List<String> urls = new ArrayList<> ();
      
      for (int i = 0; i < Int.size (repos); ++i) {
        
        if (i != id) {
          
          JSONArray item = repos.getJSONArray (i);
          item = item.getJSONArray (0);
          
          urls.add (item.getString (key));
          
        }
        
      }
      
      return urls;
      
    }
    
    public String repoLang (JSONArray langs) throws JSONException {
      return (Arrays.contains (lang, langs) ? lang : this.langs.get (0));
    }
    
    public void errorActivity (int title, Exception e) {
      errorActivity (title, e.getMessage ());
    }
    
    public void errorActivity (int title, String mess) {
      errorActivity (activity.getString (title), mess);
    }
    
    public void errorActivity (String title, String mess) {
      
      activity.setContentView (R.layout.activity_error);
      
      //Toolbar toolbar = toolbar (); // TODO
      
      //toolbar.setTitle (title);
      //toolbar.setHomeButton (true);
      
      TextView text = activity.findViewById (R.id.text);
      text.setText (mess);
      
    }
    
	  public void whatsnewDialog (int title, int text) {
		  whatsnewDialog (title, activity.getString (text));
	  }
		
	  public void whatsnewDialog (int title, String text) {
		  whatsnewDialog (activity.getString (title), text);
	  }
	  
	  public void whatsnewDialog (String title, String text) {
    	
	    if (isUpdated () && !text.equals ("")) {
	    	
		    UI.dialog (activity, dialogStyle (), title, text, new UI.DialogPosInterface () {
		    	
			    @Override
			    public void onPositiveClick (AlertDialog dialog) {
						dialog.dismiss ();
			    }
			    
		    }, R.string.ok);
		    
		    setUpdated ();
				
	    }
	    
    }
    
    public void setLang (String lang) {
    	
	    this.lang = lang;
	    prefs.set (Andromeda.PREF_LANG, lang);
	    
    }
    
  }