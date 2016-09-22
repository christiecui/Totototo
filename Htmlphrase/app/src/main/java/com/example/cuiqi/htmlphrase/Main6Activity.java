package com.example.cuiqi.htmlphrase;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;

public class Main6Activity extends ActionBarActivity {

    private ListView mListView;
    private RelativeLayout mTitle;
    private int mTouchSlop;         //滑动限定停止值
    private ListAdapter mAdapter;   //适配器
    private float mFirstY;
    private float mCurrentY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

        initView();

    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mTitle = (RelativeLayout) findViewById(R.id.id_title);
        mAdapter = new ListAdapter(this,getData());
        mListView.setAdapter(mAdapter);
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFirstY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentY = event.getY();
                        if (mCurrentY - mFirstY > mTouchSlop) {
                            showOrHide(true); //下滑 显示titlebar
                        } else if (mFirstY - mCurrentY > mTouchSlop) {
                            showOrHide(false); //上滑 隐藏titlebar
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }

                return false;
            }
        });
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        for(int i = 0;i < 30;i++){
            data.add(String.valueOf(i));
        }
        return data;
    }

    private Animator mAnimationTitle;
    private Animator mAnimationContent;

    private void showOrHide(boolean tag) {
        if (mAnimationTitle != null && mAnimationTitle.isRunning()) {
            mAnimationTitle.cancel();
        }
        if (mAnimationContent != null && mAnimationContent.isRunning()) {
            mAnimationContent.cancel();
        }
        if (tag) {
            mAnimationTitle = ObjectAnimator.ofFloat(mTitle, "translationY", mTitle.getTranslationY(), 0);
            mAnimationContent = ObjectAnimator.ofFloat(mListView, "translationY", mListView.getTranslationY(), getResources().getDimension(R.dimen.title));
        } else {
            mAnimationTitle = ObjectAnimator.ofFloat(mTitle,"translationY",mTitle.getTranslationY(),-mTitle.getHeight());
            mAnimationContent = ObjectAnimator.ofFloat(mListView,"translationY",mListView.getTranslationY(),0);
        }
        mAnimationTitle.start();
        mAnimationContent.start();
    }

    class ListAdapter extends BaseAdapter{

        private List<String> list;
        private LayoutInflater inflater;

        public ListAdapter(Context content,List<String> list){
            this.list = list;
            inflater = LayoutInflater.from(content);
        }

        @Override
        public int getCount() {
            return list == null ? 0 :list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolper vh = null;
            if(convertView == null){
                vh = new ViewHolper();
                convertView = inflater.inflate(R.layout.lv_item,parent,false);
                vh.text = (TextView) convertView.findViewById(R.id.list_item);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolper) convertView.getTag();
            }

            vh.text.setText(list.get(position));

            return convertView;
        }

        class ViewHolper{
            TextView text;
        }
    }


}
