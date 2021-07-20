	package ru.ointeractive.androdesign;
	
	import android.content.Context;
	import android.support.v7.view.menu.MenuBuilder;
	import android.util.AttributeSet;
	import android.view.View;
	
	/**
	 * State for the current menu.
	 * <p>
	 * Groups can not be nested unless there is another menu (which will have
	 * its state class).
	 */
	public class MenuState {
		
		/*
		 * Group state is set on items as they are added, allowing an item to
		 * override its group state. (As opposed to set on items at the group end tag.)
		 */
		public int groupId;
		public int groupCategory;
		public int groupOrder;
		public int groupCheckable;
		public boolean groupVisible;
		public boolean groupEnabled;
		
		public boolean itemAdded;
		public int itemId;
		public int itemCategoryOrder;
		public String itemTitle;
		public String itemTitleCondensed;
		public int itemIconResId;
		public char itemAlphabeticShortcut;
		public char itemNumericShortcut;
		/**
		 * Sync to attrs.xml enum:
		 * - 0: none
		 * - 1: all
		 * - 2: exclusive
		 */
		public int itemCheckable;
		public boolean itemChecked;
		public boolean itemVisible;
		public boolean itemEnabled;
		public String itemListenerMethodName;
		public int itemShowAsAction;
		public int itemActionLayout;
		public String itemActionViewClassName;
		
		public static final int defaultGroupId = View.NO_ID;
		public static final int defaultItemId = View.NO_ID;
		public static final int defaultItemCategory = 0;
		public static final int defaultItemOrder = 0;
		public static final int defaultItemCheckable = 0;
		public static final boolean defaultItemChecked = false;
		public static final boolean defaultItemVisible = true;
		public static final boolean defaultItemEnabled = true;
		public static final int defaultItemShowAsAction = 0;
		public static final int defaultIconResId = View.NO_ID;
		
		public static final String XML_NS = "http://schemas.android.com/apk/res/android";
		
		/**
		 * Mirror of package-scoped Menu.CATEGORY_MASK.
		 */
		public static final int Menu__CATEGORY_MASK = 0xffff0000;
		/**
		 * Mirror of package-scoped Menu.USER_MASK.
		 */
		public static final int Menu__USER_MASK = 0x0000ffff;
		
		public Context mContext;
		
		public MenuState (Context context) {
			
			mContext = context;
			resetGroup ();
			
		}
		
		public void resetGroup () {
			groupId = defaultGroupId;
			groupCategory = defaultItemCategory;
			groupOrder = defaultItemOrder;
			groupCheckable = defaultItemCheckable;
			groupVisible = defaultItemVisible;
			groupEnabled = defaultItemEnabled;
		}
		
		/**
		 * Called when the parser is pointing to a group tag.
		 */
		public void readGroup (AttributeSet attrs) {
			
			//TypedArray a = mContext.obtainStyledAttributes(attrs, com.android.internal.R.styleable.MenuGroup);
			
			//groupId = a.getResourceId(com.android.internal.R.styleable.MenuGroup_id, defaultGroupId);
			groupId = attrs.getAttributeResourceValue (XML_NS, "id", defaultGroupId);
			
			//groupCategory = a.getInt(com.android.internal.R.styleable.MenuGroup_menuCategory, defaultItemCategory);
			groupCategory = attrs.getAttributeIntValue (XML_NS, "menuCategory", defaultItemCategory);
			
			//groupOrder = a.getInt(com.android.internal.R.styleable.MenuGroup_orderInCategory, defaultItemOrder);
			groupOrder = attrs.getAttributeIntValue (XML_NS, "orderInCategory", defaultItemOrder);
			
			//groupCheckable = a.getInt(com.android.internal.R.styleable.MenuGroup_checkableBehavior, defaultItemCheckable);
			groupCheckable = attrs.getAttributeIntValue (XML_NS, "checkableBehavior", defaultItemCheckable);
			
			//groupVisible = a.getBoolean(com.android.internal.R.styleable.MenuGroup_visible, defaultItemVisible);
			groupVisible = attrs.getAttributeBooleanValue (XML_NS, "visible", defaultItemVisible);
			
			//groupEnabled = a.getBoolean(com.android.internal.R.styleable.MenuGroup_enabled, defaultItemEnabled);
			groupEnabled = attrs.getAttributeBooleanValue (XML_NS, "enabled", defaultItemEnabled);
			
			//a.recycle();
			
		}
		
		/**
		 * Called when the parser is pointing to an item tag.
		 */
		public void readItem (AttributeSet attrs) {
			//TypedArray a = mContext.obtainStyledAttributes(attrs, com.android.internal.R.styleable.MenuItem);
			
			// Inherit attributes from the group as default value
			
			//itemId = a.getResourceId(com.android.internal.R.styleable.MenuItem_id, defaultItemId);
			itemId = attrs.getAttributeResourceValue (XML_NS, "id", defaultItemId);
			
			//final int category = a.getInt(com.android.internal.R.styleable.MenuItem_menuCategory, groupCategory);
			final int category = attrs.getAttributeIntValue (XML_NS, "menuCategory", groupCategory);
			
			//final int order = a.getInt(com.android.internal.R.styleable.MenuItem_orderInCategory, groupOrder);
			final int order = attrs.getAttributeIntValue (XML_NS, "orderInCategory", groupOrder);
			
			//itemCategoryOrder = (category & Menu.CATEGORY_MASK) | (order & Menu.USER_MASK);
			itemCategoryOrder = (category & Menu__CATEGORY_MASK) | (order & Menu__USER_MASK);
			
			//itemTitle = a.getString(com.android.internal.R.styleable.MenuItem_title);
			final int itemTitleId = attrs.getAttributeResourceValue (XML_NS, "title", 0);
			
			if (itemTitleId != 0)
				itemTitle = mContext.getString (itemTitleId);
			else
				itemTitle = attrs.getAttributeValue (XML_NS, "title");
			
			//itemTitleCondensed = a.getString(com.android.internal.R.styleable.MenuItem_titleCondensed);
			final int itemTitleCondensedId = attrs.getAttributeResourceValue (XML_NS, "titleCondensed", 0);
			if (itemTitleCondensedId != 0) {
				itemTitleCondensed = mContext.getString (itemTitleCondensedId);
			} else {
				itemTitleCondensed = attrs.getAttributeValue (XML_NS, "titleCondensed");
			}
			
			//itemIconResId = a.getResourceId(com.android.internal.R.styleable.MenuItem_icon, 0);
			itemIconResId = attrs.getAttributeResourceValue (XML_NS, "icon", defaultIconResId);
			
			//itemAlphabeticShortcut = getShortcut(a.getString(com.android.internal.R.styleable.MenuItem_alphabeticShortcut));
			itemAlphabeticShortcut = getShortcut (attrs.getAttributeValue (XML_NS, "alphabeticShortcut"));
			
			//itemNumericShortcut = getShortcut(a.getString(com.android.internal.R.styleable.MenuItem_numericShortcut));
			itemNumericShortcut = getShortcut (attrs.getAttributeValue (XML_NS, "numericShortcut"));
			
			//if (a.hasValue(com.android.internal.R.styleable.MenuItem_checkable)) {
			if (attrs.getAttributeValue (XML_NS, "checkable") != null) {
				// Item has attribute checkable, use it
				//itemCheckable = a.getBoolean(com.android.internal.R.styleable.MenuItem_checkable, false) ? 1 : 0;
				itemCheckable = attrs.getAttributeBooleanValue (XML_NS, "checkable", false) ? 1 : 0;
			} else {
				// Item does not have attribute, use the group's (group can have one more state
				// for checkable that represents the exclusive checkable)
				itemCheckable = groupCheckable;
			}
			
			//itemChecked = a.getBoolean(com.android.internal.R.styleable.MenuItem_checked, defaultItemChecked);
			itemChecked = attrs.getAttributeBooleanValue (XML_NS, "checked", defaultItemChecked);
			
			//itemVisible = a.getBoolean(com.android.internal.R.styleable.MenuItem_visible, groupVisible);
			itemVisible = attrs.getAttributeBooleanValue (XML_NS, "visible", groupVisible);
			
			//itemEnabled = a.getBoolean(com.android.internal.R.styleable.MenuItem_enabled, groupEnabled);
			itemEnabled = attrs.getAttributeBooleanValue (XML_NS, "enabled", groupEnabled);
			
			//presumed emulation of 3.0+'s MenuInflator:
			itemListenerMethodName = attrs.getAttributeValue (XML_NS, "onClick");
			itemShowAsAction = attrs.getAttributeIntValue (XML_NS, "showAsAction", defaultItemShowAsAction);
			itemActionLayout = attrs.getAttributeResourceValue (XML_NS, "actionLayout", 0);
			itemActionViewClassName = attrs.getAttributeValue (XML_NS, "actionViewClass");
			
			//a.recycle();
			
			itemAdded = false;
			
		}
		
		public char getShortcut (String shortcutString) {
			
			if (shortcutString == null)
				return 0;
			else
				return shortcutString.charAt (0);
			
		}
		
		protected void addItem () {}
		
	}