package com.example.push;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PushCountActivity extends Activity implements SensorEventListener {
    private Chronometer myChronometer = null;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_push_count);

        //取得组件
        fwcGs = (TextView) findViewById(R.id.textViewGs);
        fwcSj = (TextView) findViewById(R.id.textViewSj);
        fwcSj.setText(settime);

        this.butStart = (Button) super.findViewById(R.id.butStart);
        this.butStop = (Button) super.findViewById(R.id.butStop);

        //设置事件类,打开时间对话框
        this.butStart.setOnClickListener(new OnClickListenerImpl());

        //设置事件类,返回主界面
        //this.butStop.setOnClickListener(new OnClickListenerImplStop ()) ;

        //创建一个声音播放池
        // mSoundPool = new SoundPool (2, AudioManager.STREAM_MUSIC,100);

        //音效加载
        //mSound_0 = mSoundPool.load(this, R.raw.voic_p1, 0);

        //计时器初始化
        this.myChronometer = (Chronometer) super.findViewById(R.id.myChronometer);
        this.myChronometer.setFormat("%s");

        //计时器侦听
        this.myChronometer.setOnChronometerTickListener(new OnChronometerTickListenerImpl());

        //SensorMannager 传感器管理对象
        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        //获取近距离感应器
        mSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // 注册 listener,第三个参数是检测的精确度
        mSensorMgr.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

        getAllSensor();
    }

    private void getAllSensor() {
        String[] mSensorType = {
                "加速度", "磁场", "方向", "陀螺仪", "光线",
                "压力", "温度", "距离", "重力", "线性加速度",
                "旋转矢量", "湿度", "环境温度", "无标定磁场", "无标定旋转矢量",
                "未校准陀螺仪", "特殊动作", "步行检测", "计步器", "地磁旋转矢量",
                "心跳", "倾斜检测", "唤醒手势", "瞥一眼", "捡起来"};
        Map<Integer, String> mapSensor = new TreeMap<Integer, String>();
        TextView tv_sensor = findViewById(R.id.tv_sensor);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        String show_content = "当前支持的传感器包括：\n";
        for (Sensor sensor : sensorList) {
            if (sensor.getType() >= mSensorType.length) {
                continue;
            }
            mapSensor.put(sensor.getType(), sensor.getName());
        }
        for (Map.Entry<Integer, String> item_map : mapSensor.entrySet()) {
            int type = item_map.getKey();
            String name = item_map.getValue();
            String content = String.format("%d %s：%s\n", type, mSensorType[type - 1], name);
            show_content += content;
        }
        tv_sensor.setText(show_content);
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
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

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


    //时间对话框的设置,如图 2 所示
    private class OnClickListenerImpl implements View.OnClickListener {
        public void onClick(View view) {
            Dialog dialog = new TimePickerDialog(PushCountActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            if (hourOfDay <= 9) {
                                S2 = "0" + String.valueOf(hourOfDay);
                            } else {
                                S2 = String.valueOf(hourOfDay);
                            }

                            if (minute <= 9) {
                                S1 = "0" + String.valueOf(minute);
                            } else {
                                S1 = String.valueOf(minute);
                            }

                            fwcSj.setText(S2 + ":" + S1);

                            settime = (String) fwcSj.getText();

                        }
                    }, 02, 00, true);

            dialog.show();
        }
    }
}


