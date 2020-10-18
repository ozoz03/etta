package com.oz.etta;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class MyCountDownTimer extends CountDownTimer {

    private ProgressBar progressBar;

    public MyCountDownTimer(ProgressBar progressBar, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.progressBar = progressBar;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int progress = (int) (millisUntilFinished / 100);
        progressBar.setProgress(progress);
    }

    @Override
    public void onFinish() {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 500);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,300);
        progressBar.setProgress(0);
    }
}
