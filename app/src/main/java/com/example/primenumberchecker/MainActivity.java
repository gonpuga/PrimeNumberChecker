package com.example.primenumberchecker;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements HeadlessFragment.TaskListener {
    private static final String TAG = MainActivity.class.getName();
    private EditText inputField, resultField;
    private Button primecheckbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputField = (EditText) findViewById(R.id.inputField);
        resultField = (EditText) findViewById(R.id.resultField);
        primecheckbutton = (Button) findViewById(R.id.primecheckbutton);
    }

    public void triggerPrimecheck(View v) {
        long parameter = Long.parseLong(inputField.getText().toString());
        Bundle parameters = new Bundle();
        parameters.putLong("numComprobar", parameter);
        HeadlessFragment fragment = HeadlessFragment.newInstance(parameters);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(android.R.id.content, fragment, HeadlessFragment.TAG);
        ft.commit();
    }


    @Override
    public void onPreExecute() {
        resultField.setText("");
        primecheckbutton.setEnabled(false);
    }

    @Override
    public void onProgressUpdate(double progress) {
        resultField.setText(String.format("%.1f%% completed", progress * 100));
    }

    @Override
    public void onPostExecute(boolean result) {
        resultField.setText(result + "");
        primecheckbutton.setEnabled(true);
    }

    @Override
    public void onCancelled() {
        resultField.setText("Primality test cancelled");
        primecheckbutton.setText("Is it prime?");
    }


}
