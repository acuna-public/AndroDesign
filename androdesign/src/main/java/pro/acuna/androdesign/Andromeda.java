	package pro.acuna.androdesign;
	/*
	 Created by Acuna on 02.02.2018
	*/
	
	import android.app.Activity;
	import android.app.AlertDialog;
	import android.content.Context;
	import android.content.DialogInterface;
	import android.content.pm.PackageManager;
	import android.os.Build;
	import android.support.v4.app.ActivityCompat;
	import android.support.v4.content.ContextCompat;
	import android.support.v7.widget.LinearLayoutManager;
	import android.support.v7.widget.RecyclerView;
	import android.view.View;
	import android.widget.EditText;
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
	import java.util.HashMap;
	import java.util.LinkedHashMap;
	import java.util.List;
	import java.util.Map;
	
	import pro.acuna.andromeda.AsyncTaskLoading;
	import pro.acuna.androdesign.widget.DividerItemDecoration;
	import pro.acuna.androdesign.widget.Toolbar;
	import pro.acuna.andromeda.AppsManager;
	import pro.acuna.andromeda.BackupsManager;
	import pro.acuna.andromeda.DB;
	import pro.acuna.andromeda.Device;
	import pro.acuna.andromeda.Files;
	import pro.acuna.andromeda.Graphic;
	import pro.acuna.andromeda.OS;
	import pro.acuna.andromeda.PackageData;
	import pro.acuna.andromeda.Prefs;
	import pro.acuna.andromeda.System;
	import pro.acuna.archiver.Archiver;
	import pro.acuna.jabadaba.Arrays;
	import pro.acuna.jabadaba.Crypto;
	import pro.acuna.jabadaba.HttpRequest;
	import pro.acuna.jabadaba.Int;
	import pro.acuna.jabadaba.Locales;
	import pro.acuna.jabadaba.Net;
	import pro.acuna.jabadaba.Strings;
	import pro.acuna.jabadaba.exceptions.HttpRequestException;
	import pro.acuna.jabadaba.exceptions.OutOfMemoryException;
	import pro.acuna.storage.Storage;
	import pro.acuna.storage.StorageException;
	import pro.acuna.storage.providers.SDCard;
	import pro.acuna.storage.providers.SFTP;
	
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
		public final static String PREF_STORAGE_TYPE = "storage_type";
		public final static String PREF_THEME = "theme";
		public final static String PREF_BACKUPS_NUM = "backups_num";
		public final static String PREF_BACKUPS_ENCRYPT = "backups_encrypt";
		private final static String PREF_VERSION = "version";
		private final static String PREF_LAUNCHED = "launched";
		
		public final static String THEME_DARK = "Dark";
		public final static String THEME_HOLO_DARK = "Holo Dark";
		public final static String THEME_LIGHT = "Light";
		public final static String THEME_TERMINAL = "Terminal";
		
		public static String FILE_REPOS = "repos.json";
		public static String FILE_BACKUPS = "backups.json";
		
		public static String FOLDER_BACKUPS = "backups";
		
		public boolean offline = false;
		private boolean loaded = false, updated;
		
		public Map<String, Boolean> prefBoolValues = new HashMap<>();
		private final Map<String, Integer> themes = new LinkedHashMap<> ();
		
		private Context context;
		public Activity activity;
		public PackageData app;
		
		public AppsManager apps;
		public Prefs prefs;
		
		public String lang, userAgent, storageType, backupsAlgo, theme;
		public boolean isRoot, isStorageSDCard, obligeStorageSDCard, isThemeHolo, appsCards;
		public int orientation, backupsTime;
		public int prefBackupsNum;
		public JSONObject dialogsOpened = new JSONObject ();
		
		public List<String> langs = new ArrayList<> ();
		public Map<String, Map<String, String>> locales;
		public Map<String, String> langTitles;
		public String[] langTitlesArray;
		
		public String sdcard, sftp;
		
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
			
			langTitles = getLangTitles (new LinkedHashMap<String, String> ());
			langTitlesArray = getLangTitles ();
			
			backupsAlgo = prefs.get (PREF_BACKUPS_CRYPTO_ALGO, Crypto.symmetricAlgorims[0]);
			backupsTime = backupsInterval (prefs.get (PREF_BACKUPS_INTERVAL, 1));
			
			isRoot = prefs.get (PREF_ROOT, false);
			
			orientation = Device.getOrientation (context);
			prefBackupsNum = prefs.get (PREF_BACKUPS_NUM, 0);
			
			sdcard = new SDCard ().getName ();
			sftp = new SFTP ().getName ();
			
			storageType = prefs.get (PREF_STORAGE_TYPE, sdcard);
			obligeStorageSDCard = storageType.equals (sdcard);
			
			if (!obligeStorageSDCard && !isOnline ()) storageType = sdcard;
			isStorageSDCard = storageType.equals (sdcard);
			
			theme = prefs.get (PREF_THEME, THEME_LIGHT);
			isThemeHolo = theme.equals (THEME_HOLO_DARK);
			
			prefBoolValues.put (PREF_ROOT, isRoot);
			prefBoolValues.put (PREF_ONLY_WIFI, true);
			prefBoolValues.put (PREF_ONLY_WIFI, true);
			prefBoolValues.put (PREF_BACKUPS_EXTERNAL, false);
			prefBoolValues.put (PREF_BACKUPS_SYMLINKS, false);
			prefBoolValues.put (PREF_APPS_CARDS, false);
			
			appsCards = prefs.get (PREF_APPS_CARDS, prefBoolValues);
			
			try {
				dialogsOpened = prefs.get ("dialogs_opened", new JSONObject ());
			} catch (JSONException e) {
				// empty
			}
			
			updated = (prefs.getInt (PREF_VERSION) != app.versionCode);
			
		}
		
		public Andromeda init (Activity activity) {
			
			this.activity = activity;
			
			if (!loaded) {
				
				loaded = true;
				
				themes.put (THEME_LIGHT, R.style.Light);
				themes.put (THEME_DARK, R.style.Dark);
				themes.put (THEME_TERMINAL, R.style.Terminal);
				themes.put (THEME_HOLO_DARK, R.style.HoloDark);
				
				activity.setTheme (themes.get (theme));
				
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
		
		public void setTheme (String theme) {
			activity.setTheme (themes.get (theme));
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
		
		public Toolbar toolbar () {
			
			Toolbar toolbar = new Toolbar (activity).init ().setThemeHolo (isThemeHolo);
			if (isThemeHolo) toolbar.setIcon (app.icon);
			
			return toolbar;
			
		}
		
		public Toolbar toolbar (View view) {
			
			Toolbar toolbar = new Toolbar (activity).init (view).setThemeHolo (isThemeHolo);
			if (isThemeHolo) toolbar.setIcon (app.icon);
			
			return toolbar;
			
		}
		
		public RecyclerView recyclerView (int view) {
			return recyclerView (view, false);
		}
		
		public RecyclerView recyclerView (int view, boolean cards) {
			
			RecyclerView recyclerView = activity.findViewById (view);
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
		
		public String backupsPassword () throws pro.acuna.andromeda.Crypto.DecryptException {
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
			pro.acuna.jabadaba.Files.delete (dir);
			
		}
		
		public boolean isConnected () {
			return Device.isConnected (context);
		}
		
		public boolean isOnline () {
			return Device.isOnline (context, true);
		}
		
		public String getBackupsFile () {
			return getExternalFilesDir (FOLDER_BACKUPS) + "/" + FILE_BACKUPS;
		}
		
		public JSONArray setBackupData (String title) {
			
			JSONArray array3 = new JSONArray ();
			
			array3.put (Long.parseLong (Locales.date (7, false)));
			array3.put (obligeStorageSDCard || isOnline ());
			array3.put (title);
			
			return array3;
			
		}
		
		public JSONObject fileChecksum (File file) throws Crypto.EncryptException, JSONException, FileNotFoundException {
			return fileChecksum (new FileInputStream (file));
		}
		
		public JSONObject fileChecksum (InputStream file) throws Crypto.EncryptException, JSONException {
			return fileChecksum (file, Arrays.toJSONArray (new String[] { Crypto.DIGEST_SHA1, Crypto.DIGEST_MD5 }));
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
		
		public JSONArray getBackupData (int id, String name, JSONObject backupData) throws JSONException {
			return backupData.getJSONArray (name).getJSONArray (id);
		}
		
		public JSONObject setBackup (BackupsManager backup, String id, JSONObject array1, JSONArray data, Storage storager, boolean update) throws IOException, JSONException, StorageException, Crypto.EncryptException {
			return setBackup (backup, id, array1, data, storager, update, prefBackupsNum);
		}
		
		public JSONObject setBackup (BackupsManager backup, String id, JSONObject array1, JSONArray data, Storage storager, boolean update, int num) throws IOException, JSONException, StorageException, Crypto.EncryptException {
			return setBackup (backup, id, array1, data, storager, update, num, FOLDER_BACKUPS);
		}
		
		public JSONObject setBackup (BackupsManager backup, String id, JSONObject array1, JSONArray data, Storage storager, boolean update, int num, String backupsDir) throws IOException, JSONException, StorageException, Crypto.EncryptException {
			
			data.put (Int.size (backup.getBackupFile ()));
			data.put (fileChecksum (backup.getBackupFile ()));
			
			JSONArray array4 = new JSONArray ();
			array4.put (data);
			
			if (array1.has (id)) {
				
				JSONArray array2 = array1.getJSONArray (id);
				
				for (int i = 0; i < Int.size (array2); ++i) {
					
					data = array2.getJSONArray (i);
					
					if (update && !data.getBoolean (1)) data.put (1, true);
					
					if (num > 0 && i >= (num - 1)) {
						
						storager.delete (backupFile (backupsDir, id, String.valueOf (data.getLong (0))));
						pro.acuna.jabadaba.Files.delete (getExternalFilesDir (backupFile (id, String.valueOf (data.getLong (0)))));
						
					} else array4.put (data);
					
				}
				
			}
			
			array1.put (id, array4);
			
			return array1;
			
		}
		
		public String backupFile (String id, String date) {
			return backupFile (FOLDER_BACKUPS, id, date);
		}
		
		public String backupFile (String folder, String id, String date) {
			return folder + "/" + id + "-" + date + ".zip";
		}
		
		public Storage storagerInit (Storage storager) throws StorageException {
			return storager;
		}
		
		public Storage storagerInit (Storage storager, JSONArray data) throws StorageException {
			
			storager.init (data);
			
			return storager;
			
		}
		
		public Storage storagerInit (Storage storager, String type, JSONObject data) throws StorageException {
			return storagerInit (storager, type, data, true);
		}
		
		public Storage storagerInit (Storage storager, String type, JSONObject data, boolean refreshToken) throws StorageException {
			return storagerInit (storager, type, data, refreshToken, true);
		}
		
		public Storage storagerInit (Storage storager, String type, JSONObject data, boolean refreshToken, boolean auth) throws StorageException {
			
			storager.makeAuth (auth);
			
			storager.init (type, data);
			
			return storager;
			
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
			
			Thread.setDefaultUncaughtExceptionHandler (new pro.acuna.jabadaba.System.ExceptionHandler (new pro.acuna.jabadaba.System.ExceptionListener () {
				
				@Override
				public void uncaughtException (Thread t, Throwable e) {
					
					System.error (context, e, userAgent, "crash", false);
					System.killProcess ();
					
				}
				
			}));
			
		}
		
		public interface StorageListener {
			
			Storage onItemClick (Storage storage) throws StorageException;
			
		}
		
		public void startStorageActivity (int id, Storage mStorage, JSONObject data, Storage.ProvidersData providersData) throws StorageException, JSONException {
			startStorageActivity (id, mStorage, data, providersData, null);
		}
		
		public void startStorageActivity (int id, Storage mStorage, JSONObject data, final Storage.ProvidersData providersData, StorageListener listener) throws StorageException, JSONException {
			
			final String name = providersData.names[id];
			
			mStorage.makeAuth (false);
			mStorage = mStorage.init (name, data);
			
			final Storage storage = (listener != null ? listener.onItemClick (mStorage) : mStorage);
			
			JSONObject providerItem = storage.getProviderItems ();
			
			if (providerItem.getInt (Storage.ITEM_LAYOUT) > 0) {
				
				JSONObject items = new JSONObject ();
				
				JSONObject item2 = new JSONObject ();
				
				item2.put (SFTP.SERVER, R.id.ftp_server);
				item2.put (SFTP.PORT, R.id.ftp_port);
				item2.put (SFTP.USER, R.id.ftp_username);
				item2.put (SFTP.PASSWORD, R.id.ftp_password);
				item2.put (SFTP.PATH, R.id.ftp_rootpath);
				item2.put (SFTP.TIMEOUT, R.id.ftp_timeout);
				
				items.put (sftp, item2);
				
				final Map<String, Object> defData = storage.getDefData ();
				final JSONObject item = items.getJSONObject (name);
				
				UI.dialog (activity, dialogStyle (), providersData.titles[id], providerItem.getInt (Storage.ITEM_LAYOUT), new UI.DialogViewNegInterface () {
					
					@Override
					public View onView (AlertDialog.Builder builder, View view) {
						
						try {
							return storage.onDialogView (builder, view, item);
						} catch (StorageException e) {
							OS.alert (activity, e);
						}
						
						return view;
						
					}
					
					@Override
					public void onPositiveClick (final AlertDialog dialog, final View view) {
						
						final TextView check = view.findViewById (R.id.ftp_server_check);
						
						List<String> errors = new ArrayList<> ();
						
						String[] nonEmptyKeys = new String[] { SFTP.SERVER };
						
						Map<String, Integer> titles = new HashMap<> ();
						
						titles.put (SFTP.SERVER, R.string.ftp_server);
						
						final JSONObject data = new JSONObject ();
						
						try {
							
							JSONArray keys = item.names ();
							
							for (int i = 0; i < Int.size (keys); ++i) {
								
								String key = keys.getString (i);
								
								EditText editText = view.findViewById (item.getInt (key));
								String text = UI.getFieldText (editText);
								
								if (!text.equals ("") || !Arrays.contains (key, nonEmptyKeys)) {
									
									if (Int.isNumeric (text) && Int.isNumeric (defData.get (key))) {
										
										int text2 = Int.correct (text, (int) defData.get (key));
										data.put (key, text2);
										
									} else {
										
										if (key.equals (SFTP.PATH))
											text = Strings.trim ("/", text);
										
										data.put (key, text);
										
									}
									
								} else errors.add (activity.getString (R.string.mess_field_error).replace ("%f", activity.getString (titles.get (key))));
								
							}
							
							if (Int.size (errors) == 0) {
								
								final JSONObject data2 = new JSONObject ();
								data2.put (name, data);
								
								new AsyncTaskLoading (activity, R.string.loading_check_ftp, new AsyncTaskLoading.AsyncTaskInterface () {
									
									@Override
									public List<Object> doInBackground (Object... params) {
										
										List<Object> errors = new ArrayList<> ();
										
										try {
											
											Storage mStorage = storagerInit (storage, name, data2);
											
											mStorage.makeDir ("");
											mStorage.close ();
											
										} catch (StorageException e) {
											errors.add (e.getMessage ());
										}
										
										return errors;
										
									}
									
									@Override
									public void onPostExecute (List<Object> errors) {
										
										if (Int.size (errors) > 0) {
											
											if (System.debug)
												check.setText (Arrays.implode (errors));
											else
												check.setText (R.string.mess_check_ftp_error_1);
											
										} else {
											
											try {
												
												EditText editText = view.findViewById (item.getInt (SFTP.USER));
												String user = UI.getFieldText (editText);
												
												storage.setPrefs (user, data);
												dialog.dismiss ();
												
											} catch (JSONException | StorageException e) {
												check.setText (e.getMessage ());
											}
											
										}
										
									}
									
								}).execute ();
								
							}
							
						} catch (JSONException e) {
							errors.add (e.getMessage ());
						}
						
						if (Int.size (errors) > 0) check.setText (Arrays.implode (errors));
						
					}
					
					@Override
					public void onNegativeClick (AlertDialog dialog, View view) {
						dialog.dismiss ();
					}
					
				}, android.R.string.ok, R.string.cancel);
				
			} else if (providerItem.getInt (Storage.ITEM_LAYOUT) == 0)
				storage.startAuthActivity ();
			else if (storage.listener != null)
				storage.listener.onAuthSuccess ();
			
		}
		
		public void storageDialog (int title, final Storage storage, JSONObject data) throws StorageException {
			storageDialog (title, storage, data, null);
		}
		
		public void storageDialog (int title, final Storage storage, final JSONObject data, final StorageListener listener) throws StorageException {
			
			final Storage.ProvidersData providersData = storage.getProvidersData ();
			
			UI.dialog (activity, dialogStyle (), title, R.layout.dialog_list, providersData.titles, new UI.DialogChoiseInterface () {
				
				@Override
				public void onClick (DialogInterface dialog, final int id) {
					
					dialog.dismiss ();
					
					try {
						startStorageActivity (id, storage, data, providersData, listener);
					} catch (JSONException | StorageException e) {
						OS.alert (activity, e);
					}
					
				}
				
			});
			
		}
		
		public void checkPermissions () {
			
			if (updated && Build.VERSION.SDK_INT >= 23) {
				
				List<String> needed = new ArrayList<> ();
				
				for (String perm : apps.getPermissions ()) {
					
					if (ContextCompat.checkSelfPermission (context, perm) != PackageManager.PERMISSION_GRANTED)
						needed.add (perm);
					
				}
				
				if (Int.size (needed) > 0)
					ActivityCompat.requestPermissions (activity, Arrays.toStringArray (needed), 100);
				
			}
			
		}
		
		public void setUpdated () {
			
			prefs.set (PREF_VERSION, app.versionCode);
			updated = false;
			
		}
		
		public boolean isLaunched () {
			return prefs.getBool (PREF_LAUNCHED);
		}
		
		public void setLaunched () {
			prefs.set (PREF_LAUNCHED, true);
		}
		
		public void rootInit () {
			
			isRoot = Device.isRoot ();
			prefs.set (PREF_ROOT, isRoot);
			
		}
		
		public boolean checkRoot () {
			return prefs.get (PREF_ROOT, prefBoolValues);
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
		
		public String getRemoteApps (String packagesFile, String packagesLocalFile) throws JSONException, HttpRequestException, Archiver.DecompressException, OutOfMemoryException { // Получаем Packages.gz из репозитория
			
			if (!offline) {
				
				HttpRequest httpConn = Net.request (packagesFile, userAgent);
				return httpConn.getContent ();
				
			} else return Archiver.decompress (packagesLocalFile);
			
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
			
			Toolbar toolbar = toolbar ();
			
			toolbar.setTitle (title);
			toolbar.setHomeButton (true);
			
			TextView text = activity.findViewById (R.id.title);
			text.setText (mess);
			
		}
		
	}