package net.gymhark;

public class StopWatch {

    private long stopTime;
    private long elapsed;
    private boolean running = false;
    MainActivity main = new MainActivity();

    public void start() {
        main.starttime = System.currentTimeMillis();
        running = true;
    }


    public void stop() {
        stopTime = System.currentTimeMillis();
        running = false;
    }

    public void reset() {
        main.starttime = 0;
        stopTime = 0;
        running = false;
    }

    //elaspsed time in milliseconds
    public long getElapsedTimeMilli() {
        if (running) {
            elapsed = (System.currentTimeMillis() - main.starttime);
        }
        else {
            elapsed = (stopTime - main.starttime);
        }
        return elapsed;
    }
}