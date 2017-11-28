package com.example.primenumberchecker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private EditText inputField, resultField;
    private Button primecheckbutton;
    private MyAsyncTask mAsyncTask;
    private boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputField = (EditText) findViewById(R.id.inputField);
        resultField = (EditText) findViewById(R.id.resultField);
        primecheckbutton = (Button) findViewById(R.id.primecheckbutton);
    }

    public void triggerPrimecheck(View v) {
        if(!isRunning) {
            isRunning = true;
            Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                    ": triggerPrimecheck() starts");
            long parameter = Long.parseLong(inputField.getText().toString());
            mAsyncTask = new MyAsyncTask();
            mAsyncTask.execute(parameter);
            Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                ": triggerPrimecheck() ends");
        }
        else{
            isRunning=false;
            Log.v(TAG, "Cancelling primality test" +
                    Thread.currentThread().getId());
            mAsyncTask.cancel(true);
        }
    }   

    private class MyAsyncTask extends AsyncTask<Long, Double, Boolean> {
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
            resultField.setText("");
            primecheckbutton.setText("cancel");
        }

        @Override
        protected void onProgressUpdate(Double... progress) {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                    ": onProgressUpdate()");
            resultField.setText(String.format("%.1f%% completed", progress[0] * 100));
        }

        @Override
        protected void onPostExecute(Boolean isPrime) {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                    ": onPostExecute()");
            resultField.setText(isPrime + "");
            primecheckbutton.setText("Is it prime?");
            isRunning=false;

        }

        @Override
        protected void onCancelled()
        {
            Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                    ": onCancelled");
            super.onCancelled();
            resultField.setText("Primality test cancelled");
            primecheckbutton.setText("Is it prime?");
        }

    }
}
