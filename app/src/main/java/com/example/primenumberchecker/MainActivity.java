package com.example.primenumberchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements TaskListener{
    private static final String TAG = MainActivity.class.getName();
    private EditText inputField, resultField;
    private Button primecheckbutton;
    private MyAsyncTask mAsyncTask;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputField = findViewById(R.id.inputField);
        resultField = findViewById(R.id.resultField);
        primecheckbutton = findViewById(R.id.primecheckbutton);
    }

    public void triggerPrimecheck(View v) {
        if(mAsyncTask==null || mAsyncTask.getStatus()== AsyncTask.Status.FINISHED){
            Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                    ": triggerPrimecheck() starts");
            long parameter = Long.parseLong(inputField.getText().toString());
            mAsyncTask = new MyAsyncTask(this);
            mAsyncTask.execute(parameter);
            Log.v(TAG, "Thread " + Thread.currentThread().getId() +
                    ": triggerPrimecheck() ends");
            Log.d(TAG, "Status: " + mAsyncTask.getStatus());
        }
        else if(mAsyncTask.getStatus()== AsyncTask.Status.RUNNING){
            Log.v(TAG, "Cancelling primality test" +
                    Thread.currentThread().getId());
            mAsyncTask.cancel(true);
        }
    }

    @Override
    public void onPreExecute() {
        resultField.setText("");
        primecheckbutton.setText("cancel");
    }

    @Override
    public void onProgressUpdate(double progress) {
        resultField.setText(String.format("%.1f%% completed", progress * 100));
    }

    @Override
    public void onPostExecute(boolean result) {
        resultField.setText(result + "");
        primecheckbutton.setText("Is it prime?");
    }

    @Override
    public void onCancelled() {
        resultField.setText("Primality test cancelled");
        primecheckbutton.setText("Is it prime?");
    }
}
