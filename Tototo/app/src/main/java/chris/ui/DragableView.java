package chris.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by christiecui on 2016/4/20.
 */
public class DragableView extends ImageView implements View.OnTouchListener,View.OnClickListener{

    private static final String TAG = "DragableView";
    Context mContext;
    private float lastX,lastY;
    private float downX, downY;
    private boolean clickormove = true;
    private int screenWidth=0;
    private int screenHeight=0;

    public DragableView(Context context) {
        super(context);
        init(context);
    }

    public DragableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        setOnClickListener(this);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action=event.getAction();
        switch (action) {
            //按下
            case MotionEvent.ACTION_DOWN:
                //按下处坐标
                lastX=event.getRawX();
                lastY=event.getRawY();
                downX = lastX;
                downY = lastY;
                break;
            //移动
            case MotionEvent.ACTION_MOVE:
                //移动的距离
                float distanceX=event.getRawX()-lastX;
                float distanceY=event.getRawY()-lastY;
                //移动后控件的坐标
                int left = (int) (view.getLeft() + distanceX);
                int top = (int) (view.getTop() + distanceY);
                int right = (int) (view.getRight() + distanceX);
                int bottom = (int) (view.getBottom() + distanceY);
                //处理拖出屏幕的情况
                if (left <0) {
                    left =0;
                    right =view.getWidth();
                }
                if (right >screenWidth) {
                    right =screenWidth;
                    left =screenWidth-view.getWidth();
                }
                if (top <0) {
                    top =0;
                    bottom =view.getHeight();
                }
                if (bottom >screenHeight) {
                    bottom =screenHeight;
                    top =screenHeight-view.getHeight();
                }
                //显示图片
                view.layout(left, top, right, bottom);
                lastX=event.getRawX();
                lastY=event.getRawY();
                break;
            //抬起
            case MotionEvent.ACTION_UP:
                if (Math.abs((int) (event.getRawX() - downX)) > 5
                        || Math.abs((int) (event.getRawY() - downY)) > 5){
                    clickormove = false;
                } else{
                    clickormove = true;
                }
            default:
                break;
        }
        return false;
    }

    public void initScreenWidthAndHeight(Activity activity) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        Log.v(TAG, "screenWidth"+screenWidth+"------- screenHeight"+screenHeight);
    }

    @Override
    public void onClick(View v) {
        if (clickormove){
           Log.v(TAG,"iniininini");
        }
    }
}
