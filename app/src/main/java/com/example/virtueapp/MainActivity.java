package com.example.virtueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView d_onclick;
    private TextView f_hint,f_count,f_time;
    private Context mContext;
    private int tab = 1;
    private Vibrator myVibrator;
    private MediaPlayer mediaPlayer;
    private AssetManager aManager;
    private SoundPool mSoundPool = null;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    private ObjectAnimator animator;
    private Timer timer; //时钟对象

    private static final int LONG_DELAY = 3500; // 3.5 seconds
    private static final int SHORT_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
//        长按时触发
        d_onclick.setOnLongClickListener(v -> {
            initPopWindow(v);
            return false;
        });

    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();

//
//    }

    public void init(){
        d_onclick = findViewById(R.id.d_onclick);
        f_hint = findViewById(R.id.f_hint);
        f_hint.setAutoLinkMask(Linkify.ALL);
        f_count = findViewById(R.id.f_count);
        f_count.setAutoLinkMask(Linkify.ALL);
        f_time = findViewById(R.id.f_time);
        f_time.setAutoLinkMask(Linkify.ALL);
        d_onclick.setOnClickListener(this);

//              获得系统vibrator实例
        myVibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        mContext = MainActivity.this;


//        字体设置
        Typeface typeface = Typeface.createFromAsset(getAssets(),"SmileySans-Oblique.otf");
        f_count.setTypeface(typeface);
        f_hint.setTypeface(typeface);
        f_time.setTypeface(typeface);

        time();
//        btn_hehe.setTypeface(typeface);
//        btn_xixi.setTypeface(typeface);


    }

//    点击时触发
    @Override
    public void onClick(View v) {

        String s=String.valueOf(tab);
        byte[] buff=s.getBytes();
        int f=buff.length;

        if (v.getId() == R.id.d_onclick){

//                        f_hint.setTextSize(0);

                        //           启动振动
                        myVibrator.cancel();
                        myVibrator.vibrate(new long[]{5, 30}, -1);
                        Toast.makeText(mContext, "功德+1", Toast.LENGTH_SHORT).show();

                        PlayMusic();
                        myShow();
//                        hide();

                        startScaleOutAnimD(this,d_onclick);
                        startScaleInAnimD(this,d_onclick);
                        startScaleOutAnim(this,f_hint);
                        startScaleInAnim(this,f_hint);


                        tab = tab + 1;
        }
    }





//    打印输出计数
    public void myShow(){

        String s=String.valueOf(tab);
        byte[] buff=s.getBytes();
        int f=buff.length;

        if (tab < 0) {
            int num = Math.abs(tab);

            switch (f){
                case 1:f_count.setText("-00000000"+num);
                    break;
                case 2:f_count.setText("-0000000"+num);
                    break;
                case 3:f_count.setText("-000000"+num);
                    break;
                case 4:f_count.setText("-00000"+num);
                    break;
                case 5:f_count.setText("-0000"+num);
                    break;
                case 6:f_count.setText("-000"+num);
                    break;
                case 7:f_count.setText("-00"+num);
                    break;
                case 8:f_count.setText("-0"+num);
                    break;
                case 9:f_count.setText("-"+num);
                    break;
            }
        }else {
            switch (f){
                case 1:f_count.setText("0000000"+tab);
                    break;
                case 2:f_count.setText("000000"+tab);
                    break;
                case 3:f_count.setText("00000"+tab);
                    break;
                case 4:f_count.setText("0000"+tab);
                    break;
                case 5:f_count.setText("000"+tab);
                    break;
                case 6:f_count.setText("00"+tab);
                    break;
                case 7:f_count.setText("0"+tab);
                    break;
                case 8:f_count.setText(""+tab);
                    break;
            }
        }



    }

//    音乐
    private void PlayMusic() {
        mediaPlayer = null;
        mediaPlayer = MediaPlayer.create(this, R.raw.newmuyy);
        mediaPlayer.start();
    }


//    时间
    private void time(){

        if (timer == null){

            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Message message=new Message();
                    message.what=0;
                    mHandler.sendMessage(message);


                }
            };timer.schedule(task,0,1000);

        }


    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            // TODO Auto-generated method stub
            if(msg.what == 0){
                //这里可以进行UI操作，如Toast，Dialog等
                 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
                //获取当前时间
                Date date = new Date(System.currentTimeMillis());
                f_time.setText(""+simpleDateFormat.format(date));
            }
        }
    };

    @SuppressLint("ResourceType")
    public void hide(){

        if (tab == 520){

            f_hint.setTextSize(0);
            f_count.setTextSize(0);
            f_time.setTextSize(0);

            d_onclick.setImageResource(R.raw.love);
            ImageView iv = (ImageView) findViewById(R.id.d_onclick);
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(iv,"scaleX",0.65f,1.0f,1.0f,0.7f);
            anim1.setRepeatCount(5);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(iv,"scaleY",0.55f,0.9f,0.9f,0.6f);
            anim2.setRepeatCount(5);
            anim1.setInterpolator(new BounceInterpolator());
            anim1.setInterpolator(new BounceInterpolator());
            AnimatorSet set = new AnimatorSet();
            set.play(anim1).with(anim2);
            set.setDuration(1000);

            set.start();



        }else{


            f_hint.setTextSize(30);
            f_count.setTextSize(30);
            d_onclick.setImageResource(R.mipmap.woodblock_foreground);

        }
    }


//    动画设置
    public static void startScaleInAnim(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.my_inanimation);
        if (view != null)
            view.startAnimation(animation);
    }

    public static void startScaleOutAnim(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.my_ouanimation);
        if (view != null)
            view.startAnimation(animation);
    }

    public static void startScaleInAnimD(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.inanimation);
        if (view != null)
            view.startAnimation(animation);
    }

    public static void startScaleOutAnimD(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.ouanimation);
        if (view != null)
            view.startAnimation(animation);
    }


//长按实现按钮
    private void initPopWindow(View v) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_popip, null, false);
        Button btn_xixi = (Button) view.findViewById(R.id.btn_xixi);
        Button btn_hehe = (Button) view.findViewById(R.id.btn_hehe);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"SmileySans-Oblique.otf");
        f_count.setTypeface(typeface);
        f_hint.setTypeface(typeface);
        btn_hehe.setTypeface(typeface);
        btn_xixi.setTypeface(typeface);


        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, -150, -380);

        //设置popupWindow里的按钮的事件
        btn_xixi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (int music = 0;music<10;music++){
                    PlayMusic();
                    myVibrator.cancel();
                    myVibrator.vibrate(new long[]{10, 20}, -1);
                }

                //          播放音频
                PlayMusic();

                myVibrator.cancel();
                myVibrator.vibrate(new long[]{10, 20}, -1);

                tab = tab+100;
                myShow();


                Toast.makeText(MainActivity.this, "功德+100", Toast.LENGTH_SHORT).show();
                popWindow.dismiss();
            }
        });
        btn_hehe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int music = 0;music<10;music++){
                    PlayMusic();
                    myVibrator.cancel();
                    myVibrator.vibrate(new long[]{10, 20}, -1);
                }


                tab = tab-100;
                myShow();


                Toast.makeText(MainActivity.this, "功德-100", Toast.LENGTH_SHORT).show();
                popWindow.dismiss();
            }
        });
    }
}