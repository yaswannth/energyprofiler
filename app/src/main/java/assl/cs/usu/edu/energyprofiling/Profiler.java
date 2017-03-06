package assl.cs.usu.edu.energyprofiling;

/**
 * Created by ywkwon on 2/15/2017.
 */

public class Profiler {

    private static Profiler instance;
    private MainActivity mainActivity = new MainActivity();
    private static boolean running = false;
    public static long POLL_DURATION = 1000;
    public double totalEnergy = 0;
    private double cpuEnergy = 0;
    private Profiler() {

    }

    public static Profiler getInstance() {
        if(instance == null) {
            instance = new Profiler();
            running = true;
        }
        return instance;
    }

    public void start() {
        //cal
        readCPUUsage();
    }

    public double stop() {
        running = false;
        totalEnergy = totalEnergy + cpuEnergy;
        return totalEnergy;
    }

    private void readCPUUsage() {
        //read current CPU frequency
        try {
            while (running)
                Thread.sleep(POLL_DURATION);
            cpuEnergy = mainActivity.getCpuEnergy(POLL_DURATION);
        }catch (Exception e) {e.printStackTrace();}
    }
}
