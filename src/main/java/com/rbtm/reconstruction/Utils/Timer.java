package com.rbtm.reconstruction.Utils;

import static java.lang.System.currentTimeMillis;

public class Timer {
    private long startStageTime;
    private String currentStage;

    public Timer() {
        currentStage = "";
        startStageTime = currentTimeMillis();
    }

    public void startStage(String stage){
        startStageTime = currentTimeMillis();
        currentStage = stage;
    }

    public void endStage() {
        long endTime = currentTimeMillis();
        if(currentStage != "") {
            System.out.println("STAGE "+ currentStage + ": " + (endTime - startStageTime)/1000 + " s");
        }
        startStageTime = endTime;
    }

    public void nextStage(String stage) {
        endStage();
        startStage(stage);
    }
}
