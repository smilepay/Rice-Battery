package com.example.rice_battery;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Show_foodnutrient extends AppCompatActivity {
    String JsonResultString;
    String food_name, serving_size, calories, carbs, proteins, fats, sugars, sodium, cholesterol, saturated_fat, trans_fat;
    TextView names,sizes,cals,cars,pros,fat,sugs,sods,chos,sats,transs;
    String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_nutrient_check);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name"); /*String형*/

        names= findViewById(R.id.name);
        cals= findViewById(R.id.calories);
        cars= findViewById(R.id.carbs);
        pros= findViewById(R.id.pro);
        fat= findViewById(R.id.fats);
        sugs= findViewById(R.id.sugars);
        sods= findViewById(R.id.sod);
        chos= findViewById(R.id.cho);

        Toast.makeText(getApplicationContext(),name,
                Toast.LENGTH_SHORT).show();
        Show_foodnutrient.GetData task = new Show_foodnutrient.GetData();
        task.execute("http://203.245.10.33:8888/rice/show_food_nutrient.php?food_name=" + name, "");
    }

    class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                JsonResultString = result;
                InitializeQuestionData();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {

                return null;
            }
        }
    }

    public void InitializeQuestionData() {
        String TAG_JSON = "webnautes";
        String TAG_NAME = "food_name";
        String TAG_CAL = "calories";
        String TAG_CAR = "carbs";
        String TAG_PRO = "proteins";
        String TAG_FAT = "fats";
        String TAG_SUGAR = "sugars";
        String TAG_SOD = "sodium";
        String TAG_CHO = "cholesterol";

        try {
            JSONObject jsonObject = new JSONObject(JsonResultString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            JSONObject item = jsonArray.getJSONObject(0);
            food_name = item.getString(TAG_NAME);
            calories = item.getString(TAG_CAL);;
            carbs = item.getString(TAG_CAR);
            proteins = item.getString(TAG_PRO);
            fats = item.getString(TAG_FAT);
            sugars = item.getString(TAG_SUGAR);
            sodium = item.getString(TAG_SOD);
            cholesterol = item.getString(TAG_CHO);

            names.setText(food_name);
            cals.setText(calories);
            cars.setText(carbs);
            pros.setText(proteins);
            fat.setText(fats);
            sugs.setText(sugars);
            sods.setText(sodium);
            chos.setText(cholesterol);

        } catch (JSONException e) {
        }
    }
}
