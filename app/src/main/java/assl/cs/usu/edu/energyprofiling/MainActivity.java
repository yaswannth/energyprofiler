package assl.cs.usu.edu.energyprofiling;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Process;
import android.os.Debug;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView infoTextView;
    private ActivityManager am;
    private int memTotal, pId;
    private Debug.MemoryInfo[] amMI;
    private ActivityManager.MemoryInfo mi;

    private List<String> memUsed, memAvailable, memFree, cached, threshold;
    private List<Float> cpuTotal, cpuAM;
    private List<Integer> memoryAM;
    public static List<Float> cpuSpeeds = new ArrayList<Float>();
    public static List<Float> cpuFrequencies = new ArrayList<Float>();
    public static List<Float> cpuPowers = new ArrayList<Float>();
    public static float cpuEnergy;
    public int numElements = 10;
    private int[] data = new int[numElements];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoTextView = (TextView) findViewById(R.id.infoText);

        try {
            Class<?> powerProfileClazz = Class.forName("com.android.internal.os.PowerProfile");

            //get constructor that takes a context object
            Class[] argTypes = {Context.class};
            Constructor constructor = powerProfileClazz
                    .getDeclaredConstructor(argTypes);
            Object[] arguments = {this};

            //Instantiate
            Object powerProInstance = constructor.newInstance(arguments);
            //define method
            Method averagePower = powerProfileClazz.getMethod("getAveragePower", new Class[]{String.class, int.class});
            //The device I have BLU R1 HD has only one cpu speed and corresponding power in its power profile
            String cpuSpeed0 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 0}).toString();
            cpuSpeeds.add(Float.parseFloat(cpuSpeed0));

            String cpuPower0 = averagePower.invoke(powerProInstance, new Object[]{"cpu.active", 0}).toString();
            cpuPowers.add(Float.parseFloat(cpuPower0));
            String idlePower = averagePower.invoke(powerProInstance, new Object[]{"cpu.idle", 0}).toString();
            cpuPowers.add(Float.parseFloat(idlePower));

            pId = Process.myPid();

            am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

            //do something
            Random random = new Random();
            for (int i = 0; i < data.length; i++){
                data[i] = random.nextInt(numElements);
            }
            double energy = 0;
            System.out.println(energy);
            Profiler.getInstance().start();
            bubbleSort();
            energy = Profiler.getInstance().stop();
            System.out.println("energy : " + energy);
            infoTextView.append("Total Energy consumed to sort: " + Integer.toString(numElements) + " is = " + Double.toString(energy) + "\n");
        } catch (Exception e) {e.printStackTrace();}
    }


    public void getCpuFreq() {
        try {
            //Runtime.getRuntime().exec("su -c \"echo 1234 > /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq\"");
            if(!cpuFrequencies.isEmpty())
                cpuFrequencies.clear();
            String cpuFreq0 = "";
            String cpuFreq1 = "";
            String cpuFreq2 = "";
            String cpuFreq3 = "";

            RandomAccessFile reader = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq", "r");
            cpuFreq0 = reader.readLine();
            reader.close();

            infoTextView.append("CPU frequency (core 0): " + cpuFreq0 + "\n");

            reader = new RandomAccessFile("/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq", "r");
            cpuFreq1 = reader.readLine();
            reader.close();
            infoTextView.append("CPU frequency (core 1): " + cpuFreq1 + "\n");

            reader = new RandomAccessFile("/sys/devices/system/cpu/cpu2/cpufreq/scaling_cur_freq", "r");
            cpuFreq2 = reader.readLine();
            reader.close();
            infoTextView.append("CPU frequency (core 2): " + cpuFreq2 + "\n");

            reader = new RandomAccessFile("/sys/devices/system/cpu/cpu3/cpufreq/scaling_cur_freq", "r");
            cpuFreq3 = reader.readLine();
            reader.close();
            infoTextView.append("CPU frequency (core 3): " + cpuFreq3 + "\n");
            cpuFrequencies.add(Float.parseFloat(cpuFreq0));
            cpuFrequencies.add(Float.parseFloat(cpuFreq1));
            cpuFrequencies.add(Float.parseFloat(cpuFreq2));
            cpuFrequencies.add(Float.parseFloat(cpuFreq3));
        }catch (Exception e) {e.printStackTrace();}
    }

    public static float getCpuEnergy(float pollDuration){
        float energy0, energy1, energy2, energy3;

        //My device has only 4 cores
        try{
            if(cpuFrequencies.get(0) > cpuSpeeds.get(0))
                energy0 = (float)(3.8 * cpuPowers.get(0) * pollDuration) / 1000000;
            else
                energy0 = (float)(3.8 * cpuPowers.get(1) * pollDuration) / 1000000;

            if(cpuFrequencies.get(1) > cpuSpeeds.get(0))
                energy1 = (float)(3.8 * cpuPowers.get(0) * pollDuration) / 1000000;
            else
                energy1 = (float)(3.8 * cpuPowers.get(1) * pollDuration) / 1000000;

            if(cpuFrequencies.get(2) > cpuSpeeds.get(0))
                energy2 = (float)(3.8 * cpuPowers.get(0) * pollDuration) / 1000000;
            else
                energy2 = (float)(3.8 * cpuPowers.get(1) * pollDuration) / 1000000;

            if(cpuFrequencies.get(3) > cpuSpeeds.get(0))
                energy3 = (float)(3.8 * cpuPowers.get(0) * pollDuration) / 1000000;
            else
                energy3 = (float)(3.8 * cpuPowers.get(1) * pollDuration) / 1000000;


            cpuEnergy = energy0 + energy1 + energy2 + energy3;
        }catch (Exception e) {e.printStackTrace();}
        return cpuEnergy;
    }

    private void bubbleSort() {
        int min;
        for (int i = 0; i < data.length - 1; i++) {
            min = i;
            for (int j = i + 1; j < data.length; j++) {
                if (data[j] < data[min]) {
                    min = j;
                }
            }

            if (i != min) {
                int tmp = data[i];
                data[i] = data[min];
                data[min] = tmp;
            }
            System.out.println("running");
        }
    }
}
