package com.example.primenumberchecker;

/**
 * Created by Gonzalo on 28/11/2017.
 */

public interface TaskListener {
    void onPreExecute();
    void onProgressUpdate(double progress);
    void onPostExecute(boolean result);
    void onCancelled();
}

