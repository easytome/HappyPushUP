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
            //���Ź���resĿ¼�µ�rawĿ¼�е������ļ�in_call_alarm
     
            try {
                mMediaPlayer.prepare();
            } catch (IllegalStateException e) {
                
            } catch (IOException e) {
               
            }
     
            mMediaPlayer.start();
            
     
//            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
//                    public void onCompletion(MediaPlayer mp) {
//                        //�����˽��Ų����߹ر�mMediaPlayer
//                });
        }
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_surface_view_acitvity_js);

        //ȡ�����
        fwcGs = (TextView) findViewById(R.id.textViewGs);
        fwcSj = (TextView) findViewById(R.id.textViewSj);
        fwcSj.setText(settime);

        this.butStart = (Button) super.findViewById(R.id.butStart);
        this.butStop = (Button) super.findViewById(R.id.butStop);
        butStart.setWidth(80);
        butStart.setHeight(60);
        butStop.setWidth(80);
        butStop.setHeight(60);
        
        
        
        //�����¼���,��ʱ��Ի���
        this.butStart.setOnClickListener(new OnClickListenerImpl());
        
        

        //�����¼���,����������
        this.butStop.setOnClickListener(new OnClickListenerImplStop ()) ;

        //����һ���������ų�
        // mSoundPool = new SoundPool (2, AudioManager.STREAM_MUSIC,100);

        //��Ч����
        //mSound_0 = mSoundPool.load(this, R.raw.voic_p1, 0);

        //��ʱ����ʼ��
        this.myChronometer = (Chronometer) super.findViewById (R.id.myChronometer) ;
        this.myChronometer.setFormat("%s") ;

        //��ʱ������
        this.myChronometer.setOnChronometerTickListener(new OnChronometerTickListenerImpl());

        //SensorMannager �������������
        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        //��ȡ�������Ӧ��
        mSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // ע�� listener,�����������Ǽ��ľ�ȷ��
        mSensorMgr.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        
        playLocalFile();
    }

    //��ȡ�������Ӧ��״̬�仯
    public void onSensorChanged(SensorEvent event) {
        //����ӽ��������Ӧ���ư����,�뿪��Ӧ���ư����
        mGX = mGX + 0.5;

        //���Գż���,��ʾ���ı�
        fwcGs.setText(nf.format(mGX));

        //����ǵ�һ�δ����������Ӧ��,��ʱ����ʼ��ʱ
        if (mGX == 0.5) {
            myChronometer.start();
        }
        //������Ч,������ʾ
        //mSoundPool.play(mSound_0, 1, 1, 0, 0, 1);
        if(mGX == 10.5 || mGX == 20.5 || mGX == 30.5){
        	OnFinishingListener();
        	Intent intent = new Intent(activity,GreatNew.class);
        	activity.startActivity(intent);
        }
    }
    //������ʾ�������
    private void OnFinishingListener() {
		// TODO Auto-generated method stub
    	mMediaPlayer = MediaPlayer.create(this, R.raw.nizhenbang);
        //���Ź���resĿ¼�µ�rawĿ¼�е������ļ� �����
 
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            
        } catch (IOException e) {
           
        }
 
        mMediaPlayer.start();
	}


	@Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {   }

    //��ʱ�������¼�,����ʱ�������õ�ʱ�����ʱ,�����Ի���
    private class OnChronometerTickListenerImpl implements Chronometer.OnChronometerTickListener {
        public void onChronometerTick(Chronometer chronometer) {
            time = chronometer.getText().toString().replaceAll("[^(\\d{2}:\\d {2})]", "");
            if (settime.equals(time)) {
                openOptionsDialog();
            }
        }
    }

    //��ʱ����ʱ�ĶԻ���
    private void openOptionsDialog() {
        new AlertDialog.Builder(this).setTitle("End Up!").setMessage(fwcGs.getText())
                .setPositiveButton(" ȷ �� ", new DialogInterface.
                        OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        myChronometer.stop();
                        myChronometer.setBase(SystemClock.elapsedRealtime());
                        mGX = 0.0;
                        fwcGs.setText(nf.format(mGX));
                    }
                }).show();
    }


    //ʱ��Ի��������
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
