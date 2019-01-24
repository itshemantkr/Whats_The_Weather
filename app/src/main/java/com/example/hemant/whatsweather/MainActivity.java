package com.example.hemant.whatsweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView resultTextView;

    public void findWeather(View view) throws UnsupportedEncodingException {
        try{
            InputMethodManager mgr= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
            String encodedCityName=URLEncoder.encode(cityName.getText().toString(),"UTF-8");
            DownloadTask task=new DownloadTask();
            task.execute("https://api.openweathermap.org/data/2.5/weather?q="+ cityName.getText().toString() +"&appid=4bf04b8640983639060b453df808ea41");
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Could not Find Weather", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        resultTextView=findViewById(R.id.resultTextView);
        cityName=findViewById(R.id.cityName);

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url ;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    result += (char) data;
                    data = reader.read();
                }

            }catch (Exception e){
                //Toast.makeText(getApplicationContext(), "Could not Find Weather", Toast.LENGTH_LONG).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                String message="";
                JSONObject jsonObject=new JSONObject(s);

                String weatherInfo=jsonObject.getString("weather");

                JSONArray arr= new JSONArray(weatherInfo);

                for(int i=0;i<arr.length();i++){

                    JSONObject jsonPart=arr.getJSONObject(i);

                    String main="";
                    String description="";
                    main=jsonPart.getString("main");
                    description=jsonPart.getString("description");

                    if(main!=""&&description!=""){
                        message += main+" : "+description+"\r\n";
                    }

                }

                if(message!=""){

                    resultTextView.setText(message);
                }else {
                    Toast.makeText(getApplicationContext(), "Could not Find Weather", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Could not Find Weather", Toast.LENGTH_LONG).show();

            }

        }
    }

}
