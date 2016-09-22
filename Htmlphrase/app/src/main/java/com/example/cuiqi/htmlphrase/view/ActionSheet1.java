package com.example.cuiqi.htmlphrase.view;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cuiqi.htmlphrase.R;

/**
 * Created by cuiqi on 16/7/24.
 */
public class ActionSheet1 extends Dialog {

    private Button mCancel;
    private Button addAppbrandButton;
    private LinearLayout mScollLayout;
    private Context mContext;

    private ListView mMenuItems;
    private SubMenuAdapter mAdapter;

    private Animation mShowAnim;
    private Animation mDismissAnim;

    private boolean isDismissing;
    private View rootView;

    private MMMenu mMenu;

    private ConfirmCallback mConfirmCallback;
    private OnCreateMMMenuListener onCreateMenuListener;
    private OnMMMenuItemSelectedListener onMenuSelectedListener;
    private IIconCreator iconCreator;

    //linshi
    private LayoutInflater mInflater;

    public ActionSheet1(Context context, ConfirmCallback callback) {
        super(context, R.style.ActionSheetDialog);
        getWindow().setGravity(Gravity.BOTTOM);
        mConfirmCallback = callback;
        mContext = context;
        initView(mContext);
        initAnim(mContext);
        mInflater = LayoutInflater.from(context);
    }

    private void initAnim(Context context) {
        mShowAnim = AnimationUtils.loadAnimation(context, R.anim.translate_up);
        mDismissAnim = AnimationUtils.loadAnimation(context, R.anim.translate_down);
        mDismissAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismissMe();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initView(Context context) {
        mMenu = new MMMenu();
        rootView = View.inflate(context, R.layout.dialog_action_sheet, null);
//        int width = getScreenWidth(context);
//        LinearLayout.LayoutParams paras = (LinearLayout.LayoutParams) rootView.getLayoutParams();
//        paras.width = width;
//        Log.i("qq","width"+width);
//        rootView.setLayoutParams(paras);
        mCancel = (Button) rootView.findViewById(R.id.menu_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        addAppbrandButton = (Button)rootView.findViewById(R.id.add);
        addAppbrandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfirmCallback.onResult();
            }
        });
        mMenuItems = (ListView) rootView.findViewById(R.id.menu_items);
        mAdapter = new SubMenuAdapter();
        mMenuItems.setAdapter(mAdapter);
        mMenuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (onMenuSelectedListener != null) {
                    onMenuSelectedListener.onMMMenuItemSelected(mMenu.getItem(position), position);
                }
            }
        });


        this.setContentView(rootView);

    }

    public void tryShow() {
        if (onCreateMenuListener != null) {
            onCreateMenuListener.onCreateMMMenu(mMenu);
        }
    }

    public void setOnCreateMenuListener(OnCreateMMMenuListener listener) {
        this.onCreateMenuListener = listener;
    }

    public void setOnMenuSelectedListener(OnMMMenuItemSelectedListener listener) {
        this.onMenuSelectedListener = listener;
    }


    public void setIconCreator(IIconCreator iconCreator) {
        this.iconCreator = iconCreator;
    }


//    public ActionSheet1 addMenuItem(String items) {
//        mAdapter.add(items);
//        return this;
//    }

    public void toggle() {
        if (isShowing()) {
            dismiss();
        } else {
            show();
        }
    }

    @Override
    public void show() {
        mAdapter.notifyDataSetChanged();
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        super.show();
        rootView.startAnimation(mShowAnim);
    }

    @Override
    public void dismiss() {
        if(isDismissing) {
            return;
        }
        isDismissing = true;
        rootView.startAnimation(mDismissAnim);
    }

    private void dismissMe() {
        super.dismiss();
        isDismissing = false;
    }

    public interface OnCreateMMMenuListener {
        public void onCreateMMMenu(MMMenu menu);
    }

    public interface OnMMMenuItemSelectedListener {
        public void onMMMenuItemSelected(MenuItem menu, int index);
    }


    public interface IIconCreator {
        void onAttach(ImageView iconIV, MenuItem menuItem);
    }



    private class SubMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenu.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            return 1;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getNorItem(convertView, parent, position);
            return convertView;
        }

        private View getNorItem(View convertView, ViewGroup parent, int position) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.mm_submenu_item, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.icon = (ImageView)convertView.findViewById(R.id.icon);
                holder.ly = (LinearLayout)convertView.findViewById(R.id.ly);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MenuItem menuItem = mMenu.getItem(position);
            holder.title.setText(menuItem.getTitle());
            addTag(holder);
            if (menuItem.getIcon() != null) {
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageDrawable(menuItem.getIcon());
            } else if (iconCreator != null) {
                holder.icon.setVisibility(View.VISIBLE);
                iconCreator.onAttach(holder.icon, menuItem);
            } else {
                holder.icon.setVisibility(View.GONE);
            }
            return convertView;
        }

        private void addTag(ViewHolder holder) {
            holder.ly.removeAllViews();

            for(int i = 0 ; i < 3 ; i++) {
                TextView tx = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.app_brand_tag_view,holder.ly,false);
                holder.ly.addView(tx);
            }
        }

        private class ViewHolder {
            TextView title;
            ImageView icon;
            LinearLayout ly;
        }
    }
}
