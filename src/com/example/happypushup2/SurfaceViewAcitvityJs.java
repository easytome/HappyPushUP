package com.example.happypushup2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.TimePicker;
import android.media.MediaPlayer.OnCompletionListener;

import java.io.IOException;
import java.text.DecimalFormat;





public class SurfaceViewAcitvityJs extends Activity implements SensorEventListener {

    private Chronometer myChronometer = null;
    private SurfaceViewAcitvityJs activity = null;
    private Button butStart = null;
    private Button butStop = null;
    DecimalFormat nf = new DecimalFormat("0.00");
    private SensorManager mSensorMgr = null;
    Sensor mSensor = null;
    private double mGX = 0;
    private TextView fwcGs = null;
    String time = null;
    String settime = "02:00";
    private TimePickerDialog tpd = null;

    private TextView fwcSj = null;
    String S1;
    String S2;
    //SoundPool mSoundPool = null;
    int mSound_0 = 0;
    
    
    
    
    
    
    
    
    private MediaPlayer mMediaPlayer;
    
    private void playLocalFile() {
            mMediaPlayer = MediaPlayer.create(this, R.raw.in_call_alarm);
            //播放工程res目录下的raw目录中的音乐文件in_call_alarm
     
            try {
                mMediaPlayer.prepare();
            } catch (IllegalStateException e) {
                
            } catch (IOException e) {
               
            }
     
            mMediaPlayer.start();
            
     
//            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
//                    public void onCompletion(MediaPlayer mp) {
//                        //播完了接着播或者关闭mMediaPlayer
//                });
        }
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_surface_view_acitvity_js);

        //取得组件
        fwcGs = (TextView) findViewById(R.id.textViewGs);
        fwcSj = (TextView) findViewById(R.id.textViewSj);
        fwcSj.setText(settime);

        this.butStart = (Button) super.findViewById(R.id.butStart);
        this.butStop = (Button) super.findViewById(R.id.butStop);
        butStart.setWidth(80);
        butStart.setHeight(60);
        butStop.setWidth(80);
        butStop.setHeight(60);
        
        
        
        //设置事件类,打开时间对话框
        this.butStart.setOnClickListener(new OnClickListenerImpl());
        
        

        //设置事件类,返回主界面
        this.butStop.setOnClickListener(new OnClickListenerImplStop ()) ;

        //创建一个声音播放池
        // mSoundPool = new SoundPool (2, AudioManager.STREAM_MUSIC,100);

        //音效加载
        //mSound_0 = mSoundPool.load(this, R.raw.voic_p1, 0);

        //计时器初始化
        this.myChronometer = (Chronometer) super.findViewById (R.id.myChronometer) ;
        this.myChronometer.setFormat("%s") ;

        //计时器侦听
        this.myChronometer.setOnChronometerTickListener(new OnChronometerTickListenerImpl());

        //SensorMannager 传感器管理对象
        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        //获取近距离感应器
        mSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // 注册 listener,第三个参数是检测的精确度
        mSensorMgr.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        
        playLocalFile();
    }

    //获取近距离感应器状态变化
    public void onSensorChanged(SensorEvent event) {
        //身体接近近距离感应器计半个数,离开感应器计半个数
        mGX = mGX + 0.5;

        //俯卧撑计数,显示到文本
        fwcGs.setText(nf.format(mGX));

        //如果是第一次触发近距离感应器,计时器开始计时
        if (mGX == 0.5) {
            myChronometer.start();
        }
        //播放音效,声音提示
        //mSoundPool.play(mSound_0, 1, 1, 0, 0, 1);
        if(mGX == 10.5 || mGX == 20.5 || mGX == 30.5){
        	OnFinishingListener();
        	Intent intent = new Intent(activity,GreatNew.class);
        	activity.startActivity(intent);
        }
    }
    //语音提示：你真棒
    private void OnFinishingListener() {
		// TODO Auto-generated method stub
    	mMediaPlayer = MediaPlayer.create(this, R.raw.nizhenbang);
        //播放工程res目录下的raw目录中的音乐文件 你真棒
 
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            
        } catch (IOException e) {
           
        }
 
        mMediaPlayer.start();
	}


	@Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {   }

    //计时器侦听事件,当计时器与设置的时间相等时,弹出对话框。
    private class OnChronometerTickListenerImpl implements Chronometer.OnChronometerTickListener {
        public void onChronometerTick(Chronometer chronometer) {
            time = chronometer.getText().toString().replaceAll("[^(\\d{2}:\\d {2})]", "");
            if (settime.equals(time)) {
                openOptionsDialog();
            }
        }
    }

    //计时结束时的对话框
    private void openOptionsDialog() {
        new AlertDialog.Builder(this).setTitle("End Up!").setMessage(fwcGs.getText())
                .setPositiveButton(" 确 认 ", new DialogInterface.
                        OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        myChronometer.stop();
                        myChronometer.setBase(SystemClock.elapsedRealtime());
                        mGX = 0.0;
                        fwcGs.setText(nf.format(mGX));
                    }
                }).show();
    }


    //时间对话框的设置
    private class OnClickListenerImpl implements View.OnClickListener {
        public void onClick(View view) {
        	mGX = 0;
        	fwcGs.setText(nf.format(mGX));
            Dialog dialog = new TimePickerDialog(SurfaceViewAcitvityJs. this,
                    new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                        {
                            if(hourOfDay<=9)
                            {
                                S2="0"+ String.valueOf(hourOfDay);
                            }

                            else
                            {
                                S2=String.valueOf(hourOfDay);
                            }

                            if(minute<=9)
                            {
                                S1="0"+ String.valueOf(minute);
                            }

                            else
                            {
                                S1=String.valueOf(minute);
                            }

                            fwcSj.setText(S2 + ":" + S1);

                            settime=(String)fwcSj.getText();

                        }
                    },02,00,true);

            dialog.show();
        }
    }

	
	
	private class OnClickListenerImplStop implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			mGX = 0;
        	fwcGs.setText(nf.format(mGX));
			fwcSj.setText("00" + ":" + "00");

            settime=(String)fwcSj.getText();
            myChronometer.stop();
		}
		
	}

}
