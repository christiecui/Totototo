package com.example.cuiqi.htmlphrase.view;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuiqi on 16/7/24.
 */
public class MMMenu implements ContextMenu {
    private List<MenuItem> mMenuItemList;
    //	private Context mContext;
    private CharSequence mHeaderTitle;

    public MMMenu() {
        mMenuItemList = new ArrayList<MenuItem>();
    }

    public List<MenuItem> getItemList() {
        return mMenuItemList;
    }

    public CharSequence getHeaderTitle() {
        return mHeaderTitle;
    }

    public boolean isMenuEmpty() {
        return mMenuItemList.size() == 0;
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        MMMenuItem menuItem = new MMMenuItem(itemId, groupId);
        menuItem.setTitle(titleRes);
        mMenuItemList.add(menuItem);
        return menuItem;
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        MMMenuItem menuItem = new MMMenuItem(itemId, groupId);
        menuItem.setTitle(title);
        mMenuItemList.add(menuItem);
        return menuItem;
    }

    @Override
    public MenuItem add(int titleRes) {
        MMMenuItem menuItem = new MMMenuItem(Menu.NONE, Menu.NONE);
        menuItem.setTitle(titleRes);
        mMenuItemList.add(menuItem);
        return menuItem;
    }

    @Override
    public MenuItem add(CharSequence title) {
        MMMenuItem menuItem = new MMMenuItem(Menu.NONE, Menu.NONE);
        menuItem.setTitle(title);
        mMenuItemList.add(menuItem);
        return menuItem;
    }

    public MenuItem add( int itemId, int titleRes, int iconRes) {
        MMMenuItem menuItem = new MMMenuItem(itemId, Menu.NONE);
        menuItem.setTitle(titleRes);
        menuItem.setIcon(iconRes);
        mMenuItemList.add(menuItem);
        return menuItem;
    }

    public MenuItem add( int itemId, int titleRes) {
        MMMenuItem menuItem = new MMMenuItem(itemId, Menu.NONE);
        menuItem.setTitle(titleRes);
        mMenuItemList.add(menuItem);
        return menuItem;
    }

    public MenuItem add(int itemId, CharSequence title) {
        MMMenuItem menuItem = new MMMenuItem(itemId, Menu.NONE);
        menuItem.setTitle(title);
        mMenuItemList.add(menuItem);
        return menuItem;
    }

    public MenuItem add(int itemId, CharSequence title, int iconRes) {
        MMMenuItem menuItem = new MMMenuItem(itemId, Menu.NONE);
        menuItem.setTitle(title);
        menuItem.setIcon(iconRes);
        mMenuItemList.add(menuItem);
        return menuItem;
    }

    public MenuItem add(MenuItem item) {
        if (item != null) {
            mMenuItemList.add(item);
        }
        return null;
    }

    @Override
    public SubMenu addSubMenu(CharSequence title) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int titleRes) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order,
                              CharSequence title) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        return null;
    }

    @Override
    public int addIntentOptions(int groupId, int itemId, int order,
                                ComponentName caller, Intent[] specifics, Intent intent, int flags,
                                MenuItem[] outSpecificItems) {
        return 0;
    }

    @Override
    public void removeItem(int id) {
    }

    @Override
    public void removeGroup(int groupId) {

    }

    @Override
    public void clear() {
        for (MenuItem item : mMenuItemList) {
            ((MMMenuItem) item).setMenuInfo(null);
            ((MMMenuItem) item).setOnMenuItemClickListener(null);
        }
        mMenuItemList.clear();
        mHeaderTitle = null;
    }

    @Override
    public void setGroupCheckable(int group, boolean checkable,
                                  boolean exclusive) {
    }

    @Override
    public void setGroupVisible(int group, boolean visible) {
    }

    @Override
    public void setGroupEnabled(int group, boolean enabled) {
    }

    @Override
    public boolean hasVisibleItems() {
        return false;
    }

    @Override
    public MenuItem findItem(int id) {
        for (MenuItem item : mMenuItemList) {
            if (item.getItemId() == id) {
                return item;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return mMenuItemList == null ? 0 : mMenuItemList.size();
    }

    @Override
    public MenuItem getItem(int index) {
        return mMenuItemList.get(index);
    }

    @Override
    public void close() {
    }

    @Override
    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        return false;
    }

    @Override
    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean performIdentifierAction(int id, int flags) {
        return false;
    }

    @Override
    public void setQwertyMode(boolean isQwerty) {
    }

    @Override
    public ContextMenu setHeaderTitle(int titleRes) {
        return this;
    }

    @Override
    public ContextMenu setHeaderTitle(CharSequence title) {
        if (title == null) {
            return this;
        }
        mHeaderTitle = title;
        return this;
    }

    @Override
    public ContextMenu setHeaderIcon(int iconRes) {
        return this;
    }

    @Override
    public ContextMenu setHeaderIcon(Drawable icon) {
        return this;
    }

    @Override
    public ContextMenu setHeaderView(View view) {
        return this;
    }

    @Override
    public void clearHeader() {
    }
}
