package com.example.primenumberchecker;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Gonzalo on 28/11/2017.
 */

public class MyAsyncTask extends AsyncTask<Long, Double, Boolean> {
    private final static String TAG=MyAsyncTask.class.getName();
    private final TaskListener listener;
    public MyAsyncTask(TaskListener listener){this.listener=listener;}

    @Override
    protected Boolean doInBackground(Long... n) {
        Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                ": doInBackground() starts");
        long numComprobar = n[0];
        if (numComprobar<2 || numComprobar % 2 == 0)
            return false;
        double limit = Math.sqrt(numComprobar) + 0.0001;
        double progress = 0;
        for (long factor = 3; factor < limit && !isCancelled(); factor += 2) {
            if (numComprobar % factor == 0)
                return false;

            if (factor > limit * progress / 100) {
                publishProgress(progress / 100);
                progress += 5;
            }
        }
        Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                ": doInBackground() ends");
        return true;
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                ": onPreExecute()");
        listener.onPreExecute();
    }
    @Override
    protected void onProgressUpdate(Double... progress) {
        Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                ": onProgressUpdate()");
        listener.onProgressUpdate(progress[0]);
    }

    @Override
    protected void onPostExecute(Boolean isPrime) {
        Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                ": onPostExecute()");
        listener.onPostExecute(isPrime);
    }

    @Override
    protected void onCancelled()
    {
        Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                ": onCancelled");
        listener.onCancelled();
        super.onCancelled();
    }
}
