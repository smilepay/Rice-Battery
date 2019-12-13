package com.example.rice_battery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

    public class Recommend_food extends AppCompatActivity {
        Button dish1, dish2;
        String JsonResultString;
        String status_index = "2";
        String menu1, menu2;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recommendation);
            Intent intent2 = getIntent();
            String nutrition = intent2.getExtras().getString("nutrition");
            String status = intent2.getExtras().getString("status");

            if (nutrition.equals("탄수화물") && status.equals("과잉"))
                status_index = "1";
            else if (nutrition.equals("단백질") && status.equals("과잉"))
                status_index = "2";
            else if (nutrition.equals("지방") && status.equals("과잉"))
                status_index = "3";
            else if (nutrition.equals("에너지") && status.equals("과잉"))
                status_index = "4";
            else if (nutrition.equals("나트륨") && status.equals("과잉"))
                status_index = "5";
            else if (nutrition.equals("당류") && status.equals("과잉"))
                status_index = "6";
            else if (nutrition.equals("콜레스트롤") && status.equals("과잉"))
                status_index = "7";
            else if (nutrition.equals("탄수화물") && status.equals("부족"))
                status_index = "8";
            else if (nutrition.equals("단백질") && status.equals("부족"))
                status_index = "9";
            else if (nutrition.equals("지방") && status.equals("부족"))
                status_index = "10";


            dish1 = (Button) findViewById(R.id.menu);
            dish2 = (Button) findViewById(R.id.menu2);
            dish1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), Store_find.class);
                    startActivity(intent);
                }
            });
            dish2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent2=new Intent(getApplicationContext(), Question.class);
                    startActivity(intent2);

                }
            });
            ImageView img_1 = (ImageView) findViewById(R.id.imageView);
            ImageView img_2 = (ImageView) findViewById(R.id.imageView2);

            if (status_index.equals("1")) {
                img_1.setBackgroundResource(R.drawable.one_one);
                img_2.setBackgroundResource(R.drawable.one_two);
            } else if (status_index.equals("2")) {
                img_1.setBackgroundResource(R.drawable.two_one);
                img_2.setBackgroundResource(R.drawable.two_two);
            } else if (status_index.equals("3")) {
                img_1.setBackgroundResource(R.drawable.three_one);
                img_2.setBackgroundResource(R.drawable.three_two);
            } else if (status_index.equals("4")) {
                img_1.setBackgroundResource(R.drawable.four_one);
                img_2.setBackgroundResource(R.drawable.four_two);
            } else if (status_index.equals("5")) {
                img_1.setBackgroundResource(R.drawable.five_one);
                img_2.setBackgroundResource(R.drawable.five_two);
            } else if (status_index.equals("6")) {
                img_1.setBackgroundResource(R.drawable.six_one);
                img_2.setBackgroundResource(R.drawable.six_two);
            } else if (status_index.equals("7")) {
                img_1.setBackgroundResource(R.drawable.seven_one);
                img_2.setBackgroundResource(R.drawable.two_one);
            } else if (status_index.equals("8")) {
                img_1.setBackgroundResource(R.drawable.eight_one);
                img_2.setBackgroundResource(R.drawable.eight_two);
            } else if (status_index.equals("9")) {
                img_1.setBackgroundResource(R.drawable.one_two);
                img_2.setBackgroundResource(R.drawable.ten);
            } else if (status_index.equals("10")) {
                img_1.setBackgroundResource(R.drawable.nine_one);
                img_2.setBackgroundResource(R.drawable.nine_two);
            }


            Recommend_food.GetData task = new Recommend_food.GetData();
            task.execute("http://203.245.10.33:8888/rice/show_status.php?status_index="
                    + status_index, "");


        }

        private class GetData extends AsyncTask<String, Void, String> {

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
            System.out.println("d여기리이ㅣㅇ리");
            String TAG_JSON = "webnautes";
            String TAG_first = "first_dish";
            String TAG_second = "second_name";

            try {
                JSONObject jsonObject = new JSONObject(JsonResultString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                JSONObject item = jsonArray.getJSONObject(0);
                menu1 = item.getString(TAG_first);
                menu2 = item.getString(TAG_second);
                System.out.println("menu11111" + menu1);

                dish1.setText(menu1);
                dish2.setText(menu2);


            } catch (JSONException e) {

            }
        }
    }
