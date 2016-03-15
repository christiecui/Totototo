package chris.engine;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chris.engine.callback.AppCallback;
import chris.engine.callback.CallbackHelper;
import chris.model.SimpleAppModel;


/**
 * Created by cuiqi on 16/1/18.
 * modified by christiecui on 2016/3/15
 */
public class AppEngine extends TransactionBaseEngine<AppCallback>{

    ArrayList<SimpleAppModel> models = new ArrayList<SimpleAppModel>();
    public void refresh(String text){
        send(1,text);
    }

    @Override
    protected void onRequestSuccessed(int seq, String request, JSONObject response) {
        JSONObject object = response;

        ArrayList<SimpleAppModel> templist = new ArrayList<SimpleAppModel>();
        models.clear();
        if(response != null){
            SimpleAppModel model = new SimpleAppModel();
            try {
                model.appNameText = object.getString("a");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            templist.add(model);
        }
        models.addAll(templist);

        notifyDataChangedInMainThread(new CallbackHelper.Caller<AppCallback>() {

            @Override
            public void call(AppCallback cb) {
                Log.i("cuiqi", "in");
                cb.refreshUI(models);
            }
        });
    }

    @Override
    protected void onRequestFailed(int seq, int errorCode, String request, JSONObject response) {

    }
}
