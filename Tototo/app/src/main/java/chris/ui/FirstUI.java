package chris.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import tototo.christiecui.tototo.R;

public class FirstUI extends TTFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_ui);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }
}
