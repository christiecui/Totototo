package chris.engine.callback;


import java.util.ArrayList;

import chris.model.SimpleAppModel;

/**
 * Created by cuiqi on 16/1/18.
 */
public interface AppCallback extends ActionCallback {
    public void refreshUI(ArrayList<SimpleAppModel> models) ;
}
