package com.example.bankapplication;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;

public class LoadingScreen extends AsyncTask<Integer, Void, Void> {
    public static ProgressBar progressBar;

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        SystemClock.sleep(10000);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCancelled() {
        progressBar.setVisibility(View.GONE);
    }
}


