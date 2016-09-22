package com.example.cuiqi.htmlphrase;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.cuiqi.htmlphrase.download2.AppBrandDownloadWorker;
import com.example.cuiqi.htmlphrase.view.AppBrandViewHider;

import java.nio.channels.NotYetConnectedException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class Main5Activity extends AppCompatActivity {
    private TextView tx;
    private ListView wx;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        initView();
    }

    private void initView() {




        tx = (TextView) findViewById(R.id.tx);
        wx = (ListView)findViewById(R.id.wx);


        String appname = "小西家作";
        String prefix = "Web+ >";
        String completeAppName =prefix + appname;

        SpannableString fromName = new SpannableString(completeAppName);
        BackgroundColorSpan span = new BackgroundColorSpan(Color.YELLOW);
        fromName.setSpan(span, 0, prefix.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        Log.i("cuiqi123","1:" + prefix+ "  " + prefix.length());
//        Log.i("cuiqi123","1:" + prefix.length()+ 1+ "  " + prefix.length()+ appname.length()  );
        fromName.setSpan(new AppBrandClickSpan(), prefix.length(), prefix.length()+ appname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tx.setText(fromName, TextView.BufferType.SPANNABLE);

        String[] adapterData = new String[] { "Afghanistan", "Albania", "Algeria",
                "American Samoa", "Andorra", "Angola", "Anguilla",
                "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia",
                "Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain",
                "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize",
                "Benin", "Bermuda", "Bhutan", "Bolivia",
                "Bosnia and Herzegovina", "Botswana", "Bouvet Island" };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,
                adapterData);
        wx.setAdapter(arrayAdapter);


    }


    public void lalal() throws NotYetConnectedException ,Exception{
    }

    private void initHide() {
        height=tx.getHeight();
        Log.d("TAG", "height:"+height);
        wx.setOnTouchListener(new AppBrandViewHider(tx,wx, height ,wx.getHeight()));
    }


    public class  AppBrandClickSpan extends ClickableSpan{

        @Override
        public void onClick(View widget) {
            Log.i("cuiqi123","click~~~~~~~");
        }
    }
}

