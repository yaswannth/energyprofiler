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
    public List<Float> cpuSpeeds;
    public List<Float> cpuFrequencies;
    public List<Float> cpuPowers;
    public float cpuEnergy;
    public int numElements = 1000;
    private int[] data = new int[numElements];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoTextView = (TextView) findViewById(R.id.infoText) ;


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
 //           Method averagePower_nolevel = powerProfileClazz.getMethod("getAveragePower", new Class[]{String.class});

            //The device I have BLU R1 HD has only one cpu speed and corresponding power in its power profile
            String cpuSpeed0 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 0}).toString();
            /*String cpuSpeed1 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 1}).toString();
            String cpuSpeed2 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 2}).toString();
            String cpuSpeed3 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 3}).toString();
            String cpuSpeed4 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 4}).toString();
            String cpuSpeed5 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 5}).toString();
            String cpuSpeed6 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 6}).toString();
            String cpuSpeed7 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 7}).toString();
            String cpuSpeed8 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 8}).toString();
            String cpuSpeed9 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 9}).toString();
            String cpuSpeed10 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 10}).toString();
            String cpuSpeed11 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 11}).toString();
            String cpuSpeed12 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 12}).toString();
            String cpuSpeed13 = averagePower.invoke(powerProInstance, new Object[]{"cpu.speeds", 13}).toString();*/



            cpuSpeeds.add(Float.parseFloat(cpuSpeed0));
            /*cpuSpeeds.add(Float.parseFloat(cpuSpeed1));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed2));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed3));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed4));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed5));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed6));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed7));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed8));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed9));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed10));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed11));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed12));
            cpuSpeeds.add(Float.parseFloat(cpuSpeed13));*/



            String cpuPower0 = averagePower.invoke(powerProInstance, new Object[]{"cpu.active", 0}).toString();
            /*String cpuPower1 = averagePower.invoke(powerProInstance, new Object[]{"cpu.active", 1}).toString();
            String cpuPower2 = averagePower.invoke(powerProInstance, new Object[]{"cpu.active", 2}).toString();
            String cpuPower3 = averagePower.invoke(powerProInstance, new Object[]{"cpu.active", 3}).toString();*/

            cpuPowers.add(Float.parseFloat(cpuPower0));
            /*cpuPowers.add(Float.parseFloat(cpuPower1));
            cpuPowers.add(Float.parseFloat(cpuPower2));
            cpuPowers.add(Float.parseFloat(cpuPower3));*/
            String idlePower = averagePower.invoke(powerProInstance, new Object[]{"cpu.idle", 0}).toString();
            cpuPowers.add(Float.parseFloat(idlePower));

/*
            infoTextView.append("WiFi on: " + averagePower.invoke(powerProInstance, new Object[]{"wifi.on", 0}).toString() + "\n");
            infoTextView.append("WiFi active: " + averagePower.invoke(powerProInstance, new Object[]{"wifi.active", 0}).toString() + "\n");
            infoTextView.append("Gps on: " + averagePower.invoke(powerProInstance, new Object[]{"gps.on", 0}).toString() + "\n");
            infoTextView.append("Screen: " + averagePower_nolevel.invoke(powerProInstance, new Object[]{"screen.full"}).toString() + "\n");
*/

/*
            cpuTotal = new ArrayList<Float>();
            cpuAM = new ArrayList<Float>();
            memoryAM = new ArrayList<Integer>();
            memUsed = new ArrayList<String>();
            memAvailable = new ArrayList<String>();
            memFree = new ArrayList<String>();

            pId = Process.myPid();

            am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            amMI = am.getProcessMemoryInfo(new int[]{ pId });
            mi = new ActivityManager.MemoryInfo();
*/


/*
            Log.d("Profiler", batteryCap.invoke(powerProInstance, null).toString());
            Log.d("Profiler", averagePower.invoke(powerProInstance, new Object[]{"cpu.active", 1}).toString());
            Log.d("Profiler", averagePower.invoke(powerProInstance, new Object[]{"cpu.active", 2}).toString());
            Log.d("Profiler", averagePower.invoke(powerProInstance, new Object[]{"cpu.active", 3}).toString());
*/


            Profiler.getInstance().start();
            //do something
            Random random = new Random();
            for (int i = 0; i < data.length; i++){
                data[i] = random.nextInt(numElements);
            }

            bubbleSort();
            double energy = Profiler.getInstance().stop();
            infoTextView.append("Total Energy consumed to sort: " + numElements + " is = " + energy + "\n");
        } catch (Exception e) {e.printStackTrace();}
    }

    /*private long getCpuTime() {
        long cputime = 0;
        try {
            //CPU time for a specific process
            BufferedReader reader = new BufferedReader(new FileReader("/proc/" + pId + "/stat"));

            String[] sa = reader.readLine().split("[ ]+", 18);

            //utime + stime + cutime + cstime
            cputime = Long.parseLong(sa[13]) + Long.parseLong(sa[14]) + Long.parseLong(sa[15]) + Long.parseLong(sa[16]);
            reader.close();


        }catch (Exception e) {e.printStackTrace();}
        return cputime;
    }*/

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

            //cpuFreq = cmdCat("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq");

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

    public float getCpuEnergy(float pollDuration){
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
        }
    }
}
