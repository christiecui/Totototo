package chris.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import chris.link.IntentUtils;
import tototo.christiecui.tototo.R;


public class LancherUI extends TTFragmentActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    NavigationView navigationView;

    int iconsArrayRes = 0;
    int titlesArrayRes = 0;
    TypedArray icons;
    String[] titles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanch_ui);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        initView();
    }

    private void initView() {


        iconsArrayRes = R.array.tab_icons;
        titlesArrayRes = R.array.tab_titles;

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int menuId = menuItem.getItemId();
                switch (menuId){
                    case R.id.drawer_home:
                        break;
                    case R.id.drawer_favourite:
                        break;
                    case R.id.drawer_settings:
                        break;
                    case R.id.drawer_developer_gate:
                        Context context = LancherUI.this;
                        Intent intent = new Intent(context, FirstUI.class);
                        context.startActivity(intent);
                        break;
                }
                return true;

            }
        });

        icons = getResources().obtainTypedArray(iconsArrayRes);
        titles = getResources().getStringArray(titlesArrayRes);

        SampleFragmentPagerAdapter pagerAdapter =
                new SampleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);



        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }

        viewPager.setCurrentItem(1);

    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;

        private Context context;


        public View getTabView(int position) {
            Resources resource = getResources();
            View v = LayoutInflater.from(context).inflate(R.layout.lancher_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            tv.setText(titles[position]);
            ImageView img = (ImageView) v.findViewById(R.id.imageView);
            img.setImageResource(icons.getResourceId(position, 0));
            return v;
        }

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position + 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }



}
