package com.example.primenumberchecker;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by gonpuga on 28/11/2017.
 */

public class HeadlessFragment extends Fragment {

    static interface TaskListener {
        void onPreExecute();
        void onProgressUpdate(double progress);
        void onPostExecute(boolean result);
        void onCancelled();
    }

    public static final String TAG = HeadlessFragment.class.getName();
    private TaskListener taskListener;
    private static MyAsyncTask myAsyncTask;
    private long numComprobar;

    public HeadlessFragment() {
        // Required empty public constructor
    }

    public static HeadlessFragment newInstance(Bundle arguments){
        HeadlessFragment f = new HeadlessFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            this.taskListener= (TaskListener) activity;
        }catch(ClassCastException ex){
            Log.e(TAG, "Activity must implement TaskListener interface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle parameters=this.getArguments();
        if(parameters!=null)
            this.numComprobar=parameters.getLong("numComprobar", 0);
        myAsyncTask=new MyAsyncTask();
        myAsyncTask.execute(this.numComprobar);
    }

    @Override
    public void onDetach() {
        this.taskListener = null;
        super.onDetach();
    }


    private class MyAsyncTask extends AsyncTask<Long, Double, Boolean> {
        @Override
        protected Boolean doInBackground(Long... n) {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                    ": doInBackground() starts");
            long numComprobar = n[0];
            if (numComprobar < 2 || numComprobar % 2 == 0)
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
            taskListener.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Double... progress) {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                    ": onProgressUpdate()");
            taskListener.onProgressUpdate(progress[0]);
        }

        @Override
        protected void onPostExecute(Boolean isPrime) {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                    ": onPostExecute()");
            taskListener.onPostExecute(isPrime);
        }

        @Override
        protected void onCancelled()
        {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                    ": onCancelled");
            taskListener.onCancelled();
            super.onCancelled();
        }
    }


}
