package com.example.faraday;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;

import static android.R.attr.angle;


public class CarTrackSim extends AppCompatActivity {

    public static int carInMotionFlag = 0, carInDampedMotionFlag = 0, carSpeedUpFlag = 0;

    public static ImageView car_img, car_img_1, car_img_2, track_img;
    private static float initSpeed = 200, carSpeed, schedulingSpeed = 100;
    private static float carAccel = 0, carSteer = 0;
    private static float brakingDist = 0, bDistX = 0, bDistY = 0, bFlag = 0;
    private static int ignFlag = 0, divFlag = 0, trackNo;

    private static float tX, tY, tAngle = 0, count = 30;

    public static float coordX = 0, coordY = 0, coordTheta = 0, coordX_1 = 0, coordY_1 = 0, coordTheta_1 = 0, coordX_2 = 0, coordY_2 = 0, coordTheta_2 = 0;
    public static String str_val[] = {"0", "0", "0", "0", "0", "0", "0", "0", "0"};

    DecimalFormat df = new DecimalFormat("#.##");

    Handler view_update_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int temp_int;
            String temp_str;
            TextView tv_position = (TextView) findViewById(R.id.textView_Position);
            TextView tv_velocity = (TextView) findViewById(R.id.textView_Velocity);
            TextView tv_acceleration = (TextView) findViewById(R.id.textView_Acceleration);
            TextView tv_steer = (TextView) findViewById(R.id.textView_Steering_Angle);
            TextView tv_longi = (TextView) findViewById(R.id.textView_Angle);
            TextView tv_braking = (TextView) findViewById(R.id.textView_BrakingDist);
            temp_str = "Car(" + df.format(car_img.getX()) + ", " + df.format(car_img.getY()) + ")";
            tv_position.setText(temp_str);
            temp_str = "Vel: " + df.format(carSpeed) + " miles/hr";
            tv_velocity.setText(temp_str);
            temp_str = "Accel: " + df.format(carAccel);
            tv_acceleration.setText(temp_str);
            temp_str = "Steer Angle: " + df.format(carSteer);
            tv_steer.setText(temp_str);
            temp_str = "bDist: " + df.format(brakingDist);
            tv_braking.setText(temp_str);
            temp_str = "LongiAng: " + df.format(car_img.getRotation());
            tv_longi.setText(temp_str);
            /*
            //temp_str = str_names[temp_int] + "x: " + df.format(coordX) + " y: " + df.format(coordY) + " t: " + df.format(coordTheta);
            CarFunctions.CarTrackInit();
            //temp_str = "W: " + CarFunctions.carWidth + "    H: " + CarFunctions.carHeight;

            temp_int = ((CarFunctions.rPiNo - 1) + 1) % 3;
            temp_str = Integer.toString(CarFunctions.extTwoCarsCrashFlag);
            tv_longi.setText(temp_str);
            temp_str = "0 " + df.format(CarFunctions.vertexXMinExt) + " " + df.format(CarFunctions.vertexXMaxExt) + " " + df.format(CarFunctions.vertexYMinExt) + " " + df.format(CarFunctions.vertexYMaxExt);
            tv_velocity.setText(temp_str);
            temp_str = "Steer :  " + Float.toString(carSteer);
            tv_steer.setText(temp_str);
            temp_str = "1 " + df.format(CarFunctions.vertexXMinExt_1) + " " + df.format(CarFunctions.vertexXMaxExt_1) + " " + df.format(CarFunctions.vertexYMinExt_1) + " " + df.format(CarFunctions.vertexYMaxExt_1);
            //temp_str = "rPiNo:  " + Integer.toString(CarFunctions.rPiNo);
            tv_acceleration.setText(temp_str);
            temp_str = "2 " + df.format(CarFunctions.vertexXMinExt_2) + " " + df.format(CarFunctions.vertexXMaxExt_2) + " " + df.format(CarFunctions.vertexYMinExt_2) + " " + df.format(CarFunctions.vertexYMaxExt_2);
            tv_braking.setText(temp_str);
            */
        }
    };

    /*
    Handler move_forward_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CarTrackSim.car_img.setX(Cx);
            CarTrackSim.car_img.setY(Cy);
        }
    };

    Handler turn_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CarTrackSim.car_img.setRotation(CarFunctions.carAngle + carSteer);
        }
    };
    */

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_track_sim);

        Button btn_acl = (Button) findViewById(R.id.btn_accel);
        Button btn_brk = (Button) findViewById(R.id.btn_decel);
        Button btn_reset = (Button) findViewById(R.id.btn_reset);
        Button btn_close = (Button) findViewById(R.id.btn_close);
        final SeekBar seekbar_steer = (SeekBar) findViewById(R.id.seekBar_steer);
        final ToggleButton btn_ign = (ToggleButton) findViewById(R.id.btn_ignition);

        verifyStoragePermissions(CarTrackSim.this);

        selectMyCar();

        updateTextViews();

        FileHelper.saveToFile((tX + System.getProperty("line.separator") + tY + System.getProperty("line.separator") + tAngle).toString(), CarTrackSim.this);

        btn_acl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if ((event.getAction() == MotionEvent.ACTION_DOWN) && (ignFlag == 1)) {
                    carInDampedMotionFlag = 0;
                    carSpeedUpFlag = 1;
                }
                if((event.getAction() == MotionEvent.ACTION_MOVE)||(event.getAction() == MotionEvent.ACTION_UP) || (ignFlag != 1)) {
                    carSpeedUpFlag = 0;
                    carAccel = 0;
                }
                return CarTrackSim.super.onTouchEvent(event);
            }
        });

        btn_brk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_DOWN) && (ignFlag == 1)) {
                    carSpeedUpFlag = 0;
                    carInDampedMotionFlag = 1;
                }
                if((event.getAction() == MotionEvent.ACTION_MOVE)||(event.getAction() == MotionEvent.ACTION_UP) || (ignFlag != 1)) {
                    carInDampedMotionFlag = 0;
                    carAccel = 0;
                    brakingDist = 0;
                }
                return CarTrackSim.super.onTouchEvent(event);
            }
        });

        btn_reset.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    btn_ign.setChecked(false);


                }
                return CarTrackSim.super.onTouchEvent(event);
            }
        });

        btn_close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
                return CarTrackSim.super.onTouchEvent(event);
            }
        });

        btn_ign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    ignFlag = 1;
                    carInMotionFlag = 1;
                    carInDampedMotionFlag = 0;
                    carSpeed = initSpeed;
                    CarMoveForward();
                } else {
                    // The toggle is disabled
                    carInDampedMotionFlag = 1;
                    ignFlag = 0;
                    carAccel = 0;
                    carSteer = 0;
                }
            }
        });


        seekbar_steer.setProgress(50);
        seekbar_steer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                carSteer = (float) (0.3 * (progress - 50));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbar_steer.setProgress(50);
            }
        });

    }


    private void updateTextViews() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10);
                        view_update_handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
    }


    private void CarMoveForward() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(CarFunctions.auto_mode == 1) {
                            AutoMode();
                        }
                        else {
                            RaceMode();
                        }
                        AllCarsMoveForward();
                        DelayMs((long) schedulingSpeed);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        new Thread(runnable).start();
    }


    private void AllCarsMoveForward() {

        str_val = FileHelper.ReadFile(CarTrackSim.this);

        CarTrackSim.car_img.setX(coordX);
        CarTrackSim.car_img.setY(coordY);
        CarTrackSim.car_img.setRotation(coordTheta);

        CarTrackSim.car_img_1.setX(coordX_1);
        CarTrackSim.car_img_1.setY(coordY_1);
        CarTrackSim.car_img_1.setRotation(coordTheta_1);

        CarTrackSim.car_img_2.setX(coordX_2);
        CarTrackSim.car_img_2.setY(coordY_2);
        CarTrackSim.car_img_2.setRotation(coordTheta_2);
    }


    public static void DelayMs(float speed) {
        int numberOfPixelPerSec = 1000;
        long n;
        n = (long) (numberOfPixelPerSec / speed);
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void selectMyCar() {

        String temp;

        track_img = (ImageView) findViewById(R.id.iv_track);

        temp = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        if(temp.equals("1f387d45d84d9f90")) {
            CarFunctions.rPiNo = 1;

            car_img = (ImageView) findViewById(R.id.iv_car_m);
            car_img.setX(track_img.getLeft() + 5);
            car_img.setY(track_img.getBottom() - track_img.getWidth() / 10 + 5);
            trackNo = 1;

            car_img_1 = (ImageView) findViewById(R.id.iv_car_p);
            car_img_1.setX(track_img.getLeft() + 5);
            car_img_1.setY(track_img.getBottom() - 2 * track_img.getWidth() / 10 + 5);

            car_img_2 = (ImageView) findViewById(R.id.iv_car_s);
            car_img_2.setX(track_img.getLeft() + 5);
            car_img_2.setY(track_img.getBottom() - 3 * track_img.getWidth() / 10 + 5);

            tX = 75;
            tY = 326;
            tAngle = 180;
            /*tX = 45;
            tY = 496;
            tAngle = 0;*/

        }
        else if(temp.equals("f362a1b39df5051")) {
            CarFunctions.rPiNo = 2;

            car_img = (ImageView) findViewById(R.id.iv_car_p);
            car_img.setX(track_img.getLeft() + 5);
            car_img.setY(track_img.getBottom() - 2 * track_img.getWidth() / 10 + 5);
            trackNo = 2;

            car_img_1 = (ImageView) findViewById(R.id.iv_car_s);
            car_img_1.setX(track_img.getLeft() + 5);
            car_img_1.setY(track_img.getBottom() - 3 * track_img.getWidth() / 10 + 5);

            car_img_2 = (ImageView) findViewById(R.id.iv_car_m);
            car_img_2.setX(track_img.getLeft() + 5);
            car_img_2.setY(track_img.getBottom() - track_img.getWidth() / 10 + 5);

            tX = 85;
            tY = 456;
            tAngle = 0;
        }
        else {
            CarFunctions.rPiNo = 3;

            car_img = (ImageView) findViewById(R.id.iv_car_s);
            car_img.setX(track_img.getLeft() + 5);
            car_img.setY(track_img.getBottom() - 3 * track_img.getWidth() / 10 + 5);
            trackNo = 3;

            car_img_1 = (ImageView) findViewById(R.id.iv_car_m);
            car_img_1.setX(track_img.getLeft() + 5);
            car_img_1.setY(track_img.getBottom() - track_img.getWidth() / 10 + 5);

            car_img_2 = (ImageView) findViewById(R.id.iv_car_p);
            car_img_2.setX(track_img.getLeft() + 5);
            car_img_2.setY(track_img.getBottom() - 2 * track_img.getWidth() / 10 + 5);

            tX = 125;
            tY = 410;
            tAngle = 0;
        }
    }


    private synchronized void RaceMode() {
        float cx, cy, cAngle;
        CarFunctions.CarTrackInit();
        if (carInMotionFlag == 1 && (CarFunctions.CheckBounds() && CarFunctions.CheckCarCrash())) {
            cx = CarFunctions.initPosX;
            cy = CarFunctions.initPosY;
            if (carInDampedMotionFlag == 1 && carSpeed > (float) 0) {
                if (bFlag == 0) {
                    bDistX = cx;
                    bDistY = cy;
                    bFlag = 1;
                }
                brakingDist = (float) Math.sqrt(Math.pow((car_img.getX() - bDistX), 2) + Math.pow((car_img.getY() - bDistY), 2));
                carSpeed = carSpeed + carAccel;
                carAccel = carAccel - (float) 0.01;
                if (carSpeed < 1) {
                    carSpeed = 0;
                    carAccel = 0;
                    bFlag = 0;
                }
                //carSpeed--;
            }
            if (carSpeedUpFlag == 1 && carSpeed < 500) {
                carSpeed = carSpeed + carAccel;
                carAccel = carAccel + (float) 0.01;
                if (carSpeed > 500) {
                    carSpeed = 500;
                    carAccel = 0;
                }
                //carSpeed++;
            }
            if (carSpeed > 0) {
                cx = (float) (cx + (carSpeed / schedulingSpeed) * Math.sin(Math.toRadians(CarFunctions.carAngle)));
                cy = (float) (cy - (carSpeed / schedulingSpeed) * Math.cos(Math.toRadians(CarFunctions.carAngle)));
                cAngle = CarFunctions.carAngle + carSteer;
                FileHelper.saveToFile((cx + System.getProperty("line.separator") + cy + System.getProperty("line.separator") + cAngle).toString(), CarTrackSim.this);
            }
        }
        else
        {
            CarFunctions.CarTrackInit();
            FileHelper.saveToFile((CarFunctions.initPosX + System.getProperty("line.separator") + CarFunctions.initPosY + System.getProperty("line.separator") + CarFunctions.carAngle).toString(), CarTrackSim.this);
        }
    }


    private synchronized void AutoMode() {
        float cx, cy;
        float cAngle;
        CarFunctions.CarTrackInit();
        CarFunctions.PreventCheckCarCrash();
        cx = CarFunctions.initPosX;
        cy = CarFunctions.initPosY;
        if(CarFunctions.extTwoCarsCrashFlag == 0) {
            RaceMode();
        }
        else
        {
            if(carInMotionFlag != 0) {
                if (count > 20) {
                    cAngle = CarFunctions.carAngle;
                    cx = (float) (cx - Math.sin(Math.toRadians(cAngle)));
                    cy = (float) (cy + Math.cos(Math.toRadians(cAngle)));
                    FileHelper.saveToFile((cx + System.getProperty("line.separator") + cy + System.getProperty("line.separator") + cAngle).toString(), CarTrackSim.this);
                    count--;
                }
                else if (count <= 20 && count > 10) {
                    cAngle = (float) (CarFunctions.carAngle + 9);
                    cx = (float) (cx + 1.2 * Math.sin(Math.toRadians(cAngle)));
                    cy = (float) (cy - Math.cos(Math.toRadians(cAngle)));
                    FileHelper.saveToFile((cx + System.getProperty("line.separator") + cy + System.getProperty("line.separator") + cAngle).toString(), CarTrackSim.this);
                    count--;
                }
                else if (count <= 10 && count > 0) {
                    cAngle = (float) (CarFunctions.carAngle - 9);
                    cx = (float) (cx + 1.2 * Math.sin(Math.toRadians(CarFunctions.carAngle)));
                    cy = (float) (cy - Math.cos(Math.toRadians(CarFunctions.carAngle)));
                    FileHelper.saveToFile((cx + System.getProperty("line.separator") + cy + System.getProperty("line.separator") + cAngle).toString(), CarTrackSim.this);
                    count --;
                }
                if (count <= 0){// && CarFunctions.PreventCheckCarCrash()) {
                    count = 30;
                    CarFunctions.extTwoCarsCrashFlag = 0;
                }
            }
            else
            {
                CarFunctions.CarTrackInit();
                FileHelper.saveToFile((CarFunctions.initPosX + System.getProperty("line.separator") + CarFunctions.initPosY + System.getProperty("line.separator") + CarFunctions.carAngle).toString(), CarTrackSim.this);
            }
        }
    }

}


