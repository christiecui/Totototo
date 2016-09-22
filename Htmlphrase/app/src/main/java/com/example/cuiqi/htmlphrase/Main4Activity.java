package com.example.cuiqi.htmlphrase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cuiqi.htmlphrase.view.ActionSheet1;
import com.example.cuiqi.htmlphrase.view.AppBrandPopBanner;
import com.example.cuiqi.htmlphrase.view.AppBrandTagView;
import com.example.cuiqi.htmlphrase.view.ConfirmCallback;
import com.example.cuiqi.htmlphrase.view.MMMenu;

public class Main4Activity extends AppCompatActivity {

    private ActionSheet1 mActionSheet;
    private Button show;
    private Button dismiss;
    private AppBrandPopBanner popBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        initView();
    }

    private void initView() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.lalala);
        popBanner = (AppBrandPopBanner) findViewById(R.id.banner);
        show = (Button)findViewById(R.id.show);
        dismiss = (Button)findViewById(R.id.dismiss);
        mActionSheet = new ActionSheet1(this, new ConfirmCallback() {
            @Override
            public void onResult() {
                Log.i("qq","do someCGI");
            }
        });

        mActionSheet.setOnCreateMenuListener(new ActionSheet1.OnCreateMMMenuListener() {
            @Override
            public void onCreateMMMenu(MMMenu menu) {
                menu.add(1, "wo shi 1", R.drawable.ic_launcher);
                menu.add(2, "wo shi 2", R.drawable.ic_launcher);
                menu.add(3, "wo shi 3", R.drawable.ic_launcher);
                menu.add(3, "wo shi 4", R.drawable.ic_launcher);
                menu.add(3, "wo shi 5", R.drawable.ic_launcher);
                menu.add(3, "wo shi 5", R.drawable.ic_launcher);
                menu.add(3, "wo shi 5", R.drawable.ic_launcher);
                menu.add(3, "wo shi 5", R.drawable.ic_launcher);
                menu.add(3, "wo shi 5", R.drawable.ic_launcher);
                menu.add(3, "wo shi 5", R.drawable.ic_launcher);

            }
        });

        mActionSheet.setOnMenuSelectedListener(new ActionSheet1.OnMMMenuItemSelectedListener() {
            @Override
            public void onMMMenuItemSelected(MenuItem menu, int index) {
                Log.i("qq","index"+ index);
            }

        });

        mActionSheet.setIconCreator(new ActionSheet1.IIconCreator() {
            @Override
            public void onAttach(ImageView iconIV, MenuItem menuItem) {
                iconIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
            }
        });

        mActionSheet.tryShow();
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionSheet.toggle();
            }
        });
        popBanner.show();
    }
}
