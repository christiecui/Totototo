package chris.assistant;

/**
 * Created by cuiqi on 16/3/15.
 * 视图更新消息类
 */
public class ViewInvalidateMessage {
    public int what;
    public int arg1;
    public int arg2;
    public Object params;
    public ViewInvalidateMessageHandler target;

    public ViewInvalidateMessage(int what) {
        super();
        this.what = what;
    }

    public ViewInvalidateMessage(int what, Object params,
                                 ViewInvalidateMessageHandler target) {
        super();
        this.what = what;
        this.params = params;
        this.target = target;
    }

}
