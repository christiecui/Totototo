package com.example.cuiqi.htmlphrase.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

/**
 * Created by cuiqi on 16/7/24.
 */
public class MMMenuItem implements MenuItem {

    private int itemId;
    private int groupId;
    private CharSequence title;
    private int titleId;
    private int iconId;
    private Drawable iconDrawable;
    private ContextMenu.ContextMenuInfo menuInfo;
    private OnMenuItemClickListener menuItemClickListener;
    private String webUrl;

    public MMMenuItem(int itemId, int groupId) {
        this.itemId = itemId;
        this.groupId = groupId;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public int getGroupId() {
        return groupId;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public MenuItem setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    @Override
    public MenuItem setTitle(int title) {
        this.titleId = title;
        return this;
    }

    @Override
    public CharSequence getTitle() {
        if (title == null) {
            if (titleId != 0) {
//                return MMApplicationContext.getContext().getString(titleId);
            }
            return null;

        } else {
            return title;
        }
    }

    @Override
    public MenuItem setTitleCondensed(CharSequence title) {
        return this;
    }

    @Override
    public CharSequence getTitleCondensed() {
        return null;
    }

    @Override
    public MenuItem setIcon(Drawable icon) {
        this.iconDrawable = icon;
        return this;
    }

    @Override
    public MenuItem setIcon(int iconRes) {
        this.iconId = iconRes;
        return this;
    }

    @Override
    public Drawable getIcon() {
        if (iconDrawable == null) {
            if (iconId != 0) {
//                return MMApplicationContext.getContext().getResources().getDrawable(iconId);
            }
            return null;
        } else {
            return iconDrawable;
        }
    }

    public MenuItem setWebUrl(String webUrl) {
        this.webUrl = webUrl;
        return this;
    }

    public String getWebUrl() {
        return webUrl;
    }

    @Override
    public MenuItem setIntent(Intent intent) {
        return this;
    }

    @Override
    public Intent getIntent() {
        return null;
    }

    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar) {
        return this;
    }

    @Override
    public MenuItem setNumericShortcut(char numericChar) {
        return this;
    }

    @Override
    public char getNumericShortcut() {
        return 0;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar) {
        return this;
    }

    @Override
    public char getAlphabeticShortcut() {
        return 0;
    }

    @Override
    public MenuItem setCheckable(boolean checkable) {
        return this;
    }

    @Override
    public boolean isCheckable() {
        return false;
    }

    @Override
    public MenuItem setChecked(boolean checked) {
        return this;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public MenuItem setVisible(boolean visible) {
        return this;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public MenuItem setEnabled(boolean enabled) {
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean hasSubMenu() {
        return false;
    }

    @Override
    public SubMenu getSubMenu() {
        return null;
    }

    @Override
    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        this.menuItemClickListener = menuItemClickListener;
        return this;
    }

    public boolean performClick() {
        if (menuItemClickListener != null) {
            return menuItemClickListener.onMenuItemClick(this);
        }
        return false;
    }

    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return menuInfo;
    }

    public void setMenuInfo(ContextMenu.ContextMenuInfo menuInfo) {
        this.menuInfo = menuInfo;
    }

    @Override
    public boolean collapseActionView() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean expandActionView() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ActionProvider getActionProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getActionView() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isActionViewExpanded() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public MenuItem setActionProvider(ActionProvider arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MenuItem setActionView(View arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MenuItem setActionView(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MenuItem setOnActionExpandListener(OnActionExpandListener arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setShowAsAction(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public MenuItem setShowAsActionFlags(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
