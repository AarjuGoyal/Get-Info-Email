package com.example.aarjugoyal.feed_web;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    static
    {
        Log.d("DEBUG","in main activity");
    }

    public final static String EMAILNAME = "email_name";
    ProgressBar progressBar;  //= (ProgressBar) findViewById(R.id.progressBar)
    TextView responseView; // = (TextView) findViewById(R.id.responseView)
    EditText emailText;
    String email;




    /*class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView responseView = (TextView) findViewById(R.id.responseView);
        EditText emailText = (EditText) findViewById(R.id.emailText);
        String email = emailText.getText().toString();

        //Log.i("INFO","found email id as: ");
        private Exception exception;


        protected void onPreExecute() {
            Log.d("DEBUG","Function :: onPreExecute--->");
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here
            Log.d("DEBUG","Function :: doInBackground--->");
            Log.d("DEBUG","found email id as: " + email);
            String API_KEY = "f1a2864dce15ebe2";
            String API_URL = "curl -H \"X-FullContact-APIKey: f1a2864dce15ebe2\" https://api.fullcontact.com/v2/person.json?";
            try {
                URL url = new URL(API_URL + "email=" + email + "&apiKey=" + API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            Log.d("DEBUG","Function :: onPostExecute--->");
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);

            responseView.setText(response);
        }
    }
*/

    public void getDetails(View view)
    {

        Log.i("INFO","Function ::" + "getDetails");
        emailText = (EditText) findViewById(R.id.emailText);
        email = emailText.getText().toString();
        Log.i("INFO","getDetails :: value of email ::" + email);
        Intent intent = new Intent(this, DisplayContent.class);
        intent.putExtra(EMAILNAME,email);
        startActivity(intent);
    }
}
