package com.example.aarjugoyal.feed_web;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class DisplayContent extends FragmentActivity implements DownloadCallback{

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_content);

        Intent intent = getIntent();
        String email_name = intent.getStringExtra(MainActivity.EMAILNAME);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(email_name);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_content);
        layout.addView(textView);


        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.google.com");

    }


    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;

    @Override
    public void updateFromDownload(String result) {
        // Update your UI here based on result of download.
    }

    @Override
    public void updateFromDownload(Object result) {

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:

                break;
            case Progress.CONNECT_SUCCESS:

                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:

                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:

                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:

                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

}

    /**
     * Implementation of headless Fragment that runs an AsyncTask to fetch data from the network.
     */
    public class NetworkFragment extends Fragment {
        public static final String TAG = "NetworkFragment";

        private static final String URL_KEY = "UrlKey";

        private DownloadCallback mCallback;
        private DownloadTask mDownloadTask;
        private String mUrlString;

        /**
         * Static initializer for NetworkFragment that sets the URL of the host it will be downloading
         * from.
         */
        public static NetworkFragment getInstance(FragmentManager fragmentManager, String url) {
            NetworkFragment networkFragment = new NetworkFragment();
            Bundle args = new Bundle();
            args.putString(URL_KEY, url);
            networkFragment.setArguments(args);
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
            return networkFragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mUrlString = getArguments().getString(URL_KEY);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            // Host Activity will handle callbacks from task.
            mCallback = (DownloadCallback) context;
        }

        @Override
        public void onDetach() {
            super.onDetach();
            // Clear reference to host Activity to avoid memory leak.
            mCallback = null;
        }

        @Override
        public void onDestroy() {
            // Cancel task when Fragment is destroyed.
            cancelDownload();
            super.onDestroy();
        }

        /**
         * Start non-blocking execution of DownloadTask.
         */
        public void startDownload() {
            cancelDownload();
            mDownloadTask = new DownloadTask();
            mDownloadTask.execute(mUrlString);
        }

        /**
         * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
         */
        public void cancelDownload() {
            if (mDownloadTask != null) {
                mDownloadTask.cancel(true);
            }
        }


        private class DownloadTask extends AsyncTask<String, Void, DownloadTask.Result> {

            private DownloadCallback<String> mCallback;

            DownloadTask(DownloadCallback<String> callback) {
                setCallback(callback);
            }

            void setCallback(DownloadCallback<String> callback) {
                mCallback = callback;
            }

            /**
             * Wrapper class that serves as a union of a result value and an exception. When the download
             * task has completed, either the result value or exception can be a non-null value.
             * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
             */
            private static class Result {
                public String mResultValue;
                public Exception mException;
                public Result(String resultValue) {
                    mResultValue = resultValue;
                }
                public Result(Exception exception) {
                    mException = exception;
                }
            }

            /**
             * Cancel background network operation if we do not have network connectivity.
             */
            @Override
            protected void onPreExecute() {
                if (mCallback != null) {
                    NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
                    if (networkInfo == null || !networkInfo.isConnected() ||
                            (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                    && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                        // If no connectivity, cancel task and update Callback with null data.
                        mCallback.updateFromDownload(null);
                        cancel(true);
                    }
                }
            }

            /**
             * Defines work to perform on the background thread.
             */
            @Override
            protected DownloadTask.Result doInBackground(String... urls) {
                Result result = null;
                if (!isCancelled() && urls != null && urls.length > 0) {
                    String urlString = urls[0];
                    try {
                        URL url = new URL(urlString);
                        String resultString = downloadUrl(url);
                        if (resultString != null) {
                            result = new Result(resultString);
                        } else {
                            throw new IOException("No response received.");
                        }
                    } catch(Exception e) {
                        result = new Result(e);
                    }
                }
                return result;
            }

            /**
             * Updates the DownloadCallback with the result.
             */
            @Override
            protected void onPostExecute(Result result) {
                if (result != null && mCallback != null) {
                    if (result.mException != null) {
                        mCallback.updateFromDownload(result.mException.getMessage());
                    } else if (result.mResultValue != null) {
                        mCallback.updateFromDownload(result.mResultValue);
                    }
                    mCallback.finishDownloading();
                }
            }

            /**
             * Override to add special behavior for cancelled AsyncTask.
             */
            @Override
            protected void onCancelled(Result result) {
            }
            ...
        }


    }



