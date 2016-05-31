package chris.ui;

import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import tototo.christiecui.tototo.R;

public class SurfaceDemoUI extends AppCompatActivity implements View.OnTouchListener,MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener,SurfaceHolder.Callback {

    private static final String TAG = "SurfaceDemoUI";
    SurfaceView surfaceView;
//    ImageView mButton;
    Context context;
    DragableView dragableView;

//    private float lastX,lastY;
//    private float downX, downY;
//    private boolean clickormove = true;
//    private int screenWidth=0;
//    private int screenHeight=0;

    private MediaPlayer mMediaPlayer;
    private SurfaceHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_surface_demo_ui);
        //设置当前窗体为全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
        initSurface();
    }

    private void initSurface() {
        holder = surfaceView.getHolder();
        /* 设置回调函数 */
        holder.addCallback(this);
    }

    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
//        mButton=(ImageView) findViewById(R.id.button);
//        mButton.setOnTouchListener(this);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (clickormove){
//                    Toast.makeText(SurfaceDemoUI.this, "single click",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        mButton.setVisibility(View.GONE);
        dragableView = (DragableView)findViewById(R.id.button2);
        dragableView.initScreenWidthAndHeight(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
//        int action=event.getAction();
//        switch (action) {
//            //按下
//            case MotionEvent.ACTION_DOWN:
//                //按下处坐标
//                lastX=event.getRawX();
//                lastY=event.getRawY();
//                downX = lastX;
//                downY = lastY;
//                break;
//            //移动
//            case MotionEvent.ACTION_MOVE:
//                //移动的距离
//                float distanceX=event.getRawX()-lastX;
//                float distanceY=event.getRawY()-lastY;
//                //移动后控件的坐标
//                int left = (int) (view.getLeft() + distanceX);
//                int top = (int) (view.getTop() + distanceY);
//                int right = (int) (view.getRight() + distanceX);
//                int bottom = (int) (view.getBottom() + distanceY);
//                //处理拖出屏幕的情况
//                if (left <0) {
//                    left =0;
//                    right =view.getWidth();
//                }
//                if (right >screenWidth) {
//                    right =screenWidth;
//                    left =screenWidth-view.getWidth();
//                }
//                if (top <0) {
//                    top =0;
//                    bottom =view.getHeight();
//                }
//                if (bottom >screenHeight) {
//                    bottom =screenHeight;
//                    top =screenHeight-view.getHeight();
//                }
//                //显示图片
//                view.layout(left, top, right, bottom);
//                lastX=event.getRawX();
//                lastY=event.getRawY();
//                break;
//            //抬起
//            case MotionEvent.ACTION_UP:
//                if (Math.abs((int) (event.getRawX() - downX)) > 5
//                        || Math.abs((int) (event.getRawY() - downY)) > 5){
//                    clickormove = false;
//                } else{
//                    clickormove = true;
//                }
//            default:
//                break;
//        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(TAG, "surfaceCreated called");
        playVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG, "surfaceChanged called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, "surfaceDestroyed called");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        Log.v(TAG, "onBufferingUpdate percent:" + percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.v(TAG, "onCompletion called");
    }

    private void playVideo()
    {
        try
        {
            String path = "http://www.html5videoplayer.net/videos/toystory.mp4";
            //path = "/sdcard/test.3gp";
            /* 构建MediaPlayer对象 */
            mMediaPlayer = new MediaPlayer();
            /* 设置媒体文件路径 */
            mMediaPlayer.setDataSource(path);
            /* 设置通过SurfaceView来显示画面 */
            mMediaPlayer.setDisplay(holder);
             /* 准备 */
            mMediaPlayer.prepare();
            /* 设置事件监听 */
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        catch (Exception e)
        {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.v(TAG, "onPrepared called");
        int mVideoWidth = mMediaPlayer.getVideoWidth();
        int mVideoHeight = mMediaPlayer.getVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0)
        {
            /* 设置视频的宽度和高度 */
            holder.setFixedSize(mVideoWidth, mVideoHeight);

            Log.v(TAG, "mVideoWidth"+ mVideoWidth +"------- mVideoHeight"+ mVideoHeight);
             /* 开始播放 */
            mMediaPlayer.start();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        Log.v(TAG,"contentViewTop"+contentViewTop);
    }
}
