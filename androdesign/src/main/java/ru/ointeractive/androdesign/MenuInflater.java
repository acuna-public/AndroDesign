	package ru.ointeractive.androdesign;
	
	import android.content.Context;
	import android.content.res.XmlResourceParser;
	import android.util.AttributeSet;
	import android.util.Xml;
	import android.view.InflateException;
	
	import org.xmlpull.v1.XmlPullParser;
	import org.xmlpull.v1.XmlPullParserException;
	
	import java.io.IOException;
	
	public class MenuInflater extends android.view.MenuInflater {
		
		/**
		 * Menu tag name in XML.
		 */
		private static final String XML_MENU = "menu";
		/**
		 * Group tag name in XML.
		 */
		private static final String XML_GROUP = "group";
		/**
		 * Item tag name in XML.
		 */
		private static final String XML_ITEM = "item";
		
		protected Context mContext;
		
		public MenuInflater (Context context) {
			
			super (context);
			mContext = context;
			
		}
		
		public void inflate (int menuRes, MenuState menu) {
			
			try (XmlResourceParser parser = mContext.getResources ().getLayout (menuRes)) {
				
				AttributeSet attrs = Xml.asAttributeSet (parser);
				parseMenu (parser, attrs, menu);
				
			} catch (XmlPullParserException e) {
				throw new InflateException ("Error inflating menu XML", e);
			} catch (IOException e) {
				throw new InflateException ("Error inflating menu XML", e);
			}
			
		}
		
		/**
		 * Called internally to fill the given menu. If a sub menu is seen, it will
		 * call this recursively.
		 */
		private void parseMenu (XmlPullParser parser, AttributeSet attrs, MenuState menuState) throws XmlPullParserException, IOException {
			
			int eventType = parser.getEventType ();
			String tagName;
			boolean lookingForEndOfUnknownTag = false;
			String unknownTagName = null;
			
			// This loop will skip to the menu start tag
			
			do {
				
				if (eventType == XmlPullParser.START_TAG) {
					
					tagName = parser.getName ();
					
					if (tagName.equals (XML_MENU)) {
						
						// Go to next tag
						eventType = parser.next ();
						
						break;
						
					}
					
					throw new RuntimeException ("Expecting menu, got " + tagName);
					
				}
				
				eventType = parser.next ();
				
			} while (eventType != XmlPullParser.END_DOCUMENT);
			
			boolean reachedEndOfMenu = false;
			
			while (!reachedEndOfMenu) {
				
				switch (eventType) {
					
					case XmlPullParser.START_TAG:
						
						if (lookingForEndOfUnknownTag)
							break;
						
						tagName = parser.getName ();
						
						switch (tagName) {
							
							case XML_GROUP:
								menuState.readGroup (attrs);
								break;
							
							case XML_ITEM:
								menuState.readItem (attrs);
								break;
							
							case XML_MENU:
								// A menu start tag denotes a submenu for an item
								
								// Parse the submenu into returned SubMenu
								parseMenu (parser, attrs, new MenuState (mContext));
								break;
							
							default:
								lookingForEndOfUnknownTag = true;
								unknownTagName = tagName;
								break;
							
						}
						
						break;
					
					case XmlPullParser.END_TAG:
						
						tagName = parser.getName ();
						if (lookingForEndOfUnknownTag && tagName.equals (unknownTagName)) {
							lookingForEndOfUnknownTag = false;
							unknownTagName = null;
						} else if (tagName.equals (XML_GROUP)) {
							menuState.resetGroup ();
						} else if (tagName.equals (XML_ITEM)) {
							// Add the item if it hasn't been added (if the item was
							// a submenu, it would have been added already)
							if (!menuState.itemAdded)
								menuState.addItem ();
							
						} else if (tagName.equals (XML_MENU)) {
							reachedEndOfMenu = true;
						}
						
						break;
					
					case XmlPullParser.END_DOCUMENT:
						throw new RuntimeException ("Unexpected end of document");
					
				}
				
				eventType = parser.next ();
				
			}
			
		}
		
	}