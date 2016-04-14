package chris.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import tototo.christiecui.tototo.R;

public class DevelopingUI extends AppCompatActivity {

    TextView v1;
    TextView v2;
    TextView v3;
    TextView v4;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop_ui);
        initView();
        context = this;
    }

    private void initView() {
        v1 = (TextView) findViewById(R.id.b1);
        v2 = (TextView) findViewById(R.id.b2);
        v3 = (TextView) findViewById(R.id.b3);

        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FirstUI.class);
                context.startActivity(intent);
            }
        });

        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SurfaceDemoUI.class);
                context.startActivity(intent);
            }
        });
    }


}
