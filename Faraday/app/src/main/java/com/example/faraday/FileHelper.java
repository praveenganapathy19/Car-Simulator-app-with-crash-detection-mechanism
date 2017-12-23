package com.example.faraday;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Mukul Gosavi on 11/25/2017.
 */

public class FileHelper {

    Context c;
    public FileHelper(Context con)
    {
        c=con;
    }

    final static String fileName = "data.txt";
    final static String recvfileName1 = "recv_m.txt";
    final static String recvfileName2 = "recv_p.txt";
    final static String recvfileName3 = "recv_s.txt";
    final static String path_r = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Recv/" ;
    final static String path_d = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" ;
    private static String str_a[] = {"0", "0", "0", "0", "0", "0", "0", "0", "0"};

    final static String TAG = FileHelper.class.getName();

    public static String[] ReadFile( Context context){
        //final  String path = context.getFilesDir().getPath() + "/";
        String line = null;
        //File file = new File(context.getFilesDir().getPath(), "content.txt");

        try {
            Log.d(TAG,path_r);
            int i = 0;

            FileInputStream fileInputStream1 = new FileInputStream (new File(path_r + recvfileName1));
            InputStreamReader inputStreamReader1 = new InputStreamReader(fileInputStream1);
            BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
            StringBuilder stringBuilder1 = new StringBuilder();
            while ( (line = bufferedReader1.readLine()) != null)
            {
                //stringBuilder.append(line + System.getProperty("line.separator"));
                str_a[i] = line;
                i++;
            }
            fileInputStream1.close();
            bufferedReader1.close();

            FileInputStream fileInputStream2 = new FileInputStream (new File(path_r + recvfileName2));
            InputStreamReader inputStreamReader2 = new InputStreamReader(fileInputStream2);
            BufferedReader bufferedReader2 = new BufferedReader(inputStreamReader2);
            StringBuilder stringBuilder2 = new StringBuilder();
            while ( (line = bufferedReader2.readLine()) != null)
            {
                //stringBuilder.append(line + System.getProperty("line.separator"));
                str_a[i] = line;
                i++;
            }
            fileInputStream2.close();
            bufferedReader2.close();

            FileInputStream fileInputStream3 = new FileInputStream (new File(path_r + recvfileName3));
            InputStreamReader inputStreamReader3 = new InputStreamReader(fileInputStream3);
            BufferedReader bufferedReader3 = new BufferedReader(inputStreamReader3);
            StringBuilder stringBuilder3 = new StringBuilder();
            while ( (line = bufferedReader3.readLine()) != null)
            {
                //stringBuilder.append(line + System.getProperty("line.separator"));
                str_a[i] = line;
                i++;
            }
            fileInputStream3.close();
            bufferedReader3.close();

            parseValues();
        }
        catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }
        catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return str_a;
    }

    public static boolean saveToFile( String data, Context context){
        try {
            //String path=context.getFilesDir().getPath() + "/";
            new File(path_d   ).mkdir();
            File file = new File(path_d + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file,false);
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());
            Log.d("tag",path_d);
            return true;
        }  catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }  catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return  false;
    }


    private static void parseValues() {
        //int temp = 3 * (CarFunctions.rPiNo - 1);

        if(CarFunctions.rPiNo == 1)
        {
            CarTrackSim.coordX = Float.parseFloat(str_a[0]);
            CarTrackSim.coordY = Float.parseFloat(str_a[1]);
            CarTrackSim.coordTheta = Float.parseFloat(str_a[2]);
            CarTrackSim.coordX_1 = Float.parseFloat(str_a[3]);
            CarTrackSim.coordY_1 = Float.parseFloat(str_a[4]);
            CarTrackSim.coordTheta_1 = Float.parseFloat(str_a[5]);
            CarTrackSim.coordX_2 = Float.parseFloat(str_a[6]);
            CarTrackSim.coordY_2 = Float.parseFloat(str_a[7]);
            CarTrackSim.coordTheta_2 = Float.parseFloat(str_a[8]);
        }
        else if(CarFunctions.rPiNo == 2)
        {
            CarTrackSim.coordX = Float.parseFloat(str_a[3]);
            CarTrackSim.coordY = Float.parseFloat(str_a[4]);
            CarTrackSim.coordTheta = Float.parseFloat(str_a[5]);
            CarTrackSim.coordX_1 = Float.parseFloat(str_a[6]);
            CarTrackSim.coordY_1 = Float.parseFloat(str_a[7]);
            CarTrackSim.coordTheta_1 = Float.parseFloat(str_a[8]);
            CarTrackSim.coordX_2 = Float.parseFloat(str_a[0]);
            CarTrackSim.coordY_2 = Float.parseFloat(str_a[1]);
            CarTrackSim.coordTheta_2 = Float.parseFloat(str_a[2]);
        }
        else if(CarFunctions.rPiNo == 3) {
            CarTrackSim.coordX = Float.parseFloat(str_a[6]);
            CarTrackSim.coordY = Float.parseFloat(str_a[7]);
            CarTrackSim.coordTheta = Float.parseFloat(str_a[8]);
            CarTrackSim.coordX_1 = Float.parseFloat(str_a[0]);
            CarTrackSim.coordY_1 = Float.parseFloat(str_a[1]);
            CarTrackSim.coordTheta_1 = Float.parseFloat(str_a[2]);
            CarTrackSim.coordX_2 = Float.parseFloat(str_a[3]);
            CarTrackSim.coordY_2 = Float.parseFloat(str_a[4]);
            CarTrackSim.coordTheta_2 = Float.parseFloat(str_a[5]);
        }
    }

}