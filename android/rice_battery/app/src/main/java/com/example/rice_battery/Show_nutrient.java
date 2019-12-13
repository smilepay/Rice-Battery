package com.example.rice_battery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class Show_nutrient extends AppCompatActivity {
    String JsonResultString;
    String today_date, today_carbon, today_protein, today_fat, today_nat, today_sugar, today_cole;
    String name,whole;
    float today_carbon_fl,today_protein_fl,today_fat_fl,today_nat_fl,today_sugar_fl,today_cole_fl,today_energy_fl,total_energy_fl;
    float now_carbon_fl,now_protein_fl,now_fat_fl,now_nat_fl,now_sugar_fl,now_cole_fl,now_energy_fl;
    private BluetoothSPP bt;
    int flag = 0;
    String now_carbon,now_protein,now_fat,now_nat,now_sugar,now_cole,now_energy;
    String user_name;
    ImageView img_1,img_2,img_3,img_4,img_5,img_6,img_7;
    String nut,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_test);

        img_1=(ImageView)findViewById(R.id.content_1);
        img_2=(ImageView)findViewById(R.id.content_2);
        img_3=(ImageView)findViewById(R.id.content_3);
        img_4=(ImageView)findViewById(R.id.content_4);
        img_5=(ImageView)findViewById(R.id.content_5);
        img_6=(ImageView)findViewById(R.id.content_6);
        img_7=(ImageView)findViewById(R.id.content_7);
        img_1.setBackgroundResource(R.drawable.one_ss);
        img_2.setBackgroundResource(R.drawable.one_ss);
        img_3.setBackgroundResource(R.drawable.one_ss);
        img_4.setBackgroundResource(R.drawable.one_ss);
        img_5.setBackgroundResource(R.drawable.one_ss);
        img_6.setBackgroundResource(R.drawable.one_ss);
        img_7.setBackgroundResource(R.drawable.one_ss);

        Intent intent = getIntent();
        name = intent.getExtras().getString("food_name"); /*String형*/

        Intent intent2 = getIntent();
        user_name = intent2.getExtras().getString("user_name"); /*String형*/
        whole = intent2.getExtras().getString("whole");

        Show_nutrient.GetData2 task1 = new Show_nutrient.GetData2();
        task1.execute("http://203.245.10.33:8888/rice/show_food_nutrient.php?food_name=" + name, "");

        String total_energy=whole; //이건 디비에서 바로 받아오는거라 계산 x


        //Show_nutrient.InsertData task3 = new Show_nutrient.InsertData();
        //task.execute("http://203.245.10.33:8888/rice/day_eat_insert.php", user_name, Float.toString(carbon_rate), Float.toString(protein_rate), Float.toString(fat_rate), Float.toString(today_nat_fl),Float.toString(sugar_rate),Float.toString(today_cole_fl));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        Toast toast = Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG);

        switch(item.getItemId())
        {
            case R.id.menu1:
                Intent intent = new Intent(getApplicationContext(), Show_foodnutrient.class);
                intent.putExtra("name",name);
                startActivity(intent);
                break;
            case R.id.menu2:
                Intent intent2 = new Intent(getApplicationContext(), Show_character.class);
                intent2.putExtra("name",name);
                intent2.putExtra("user_name",user_name);
                intent2.putExtra("nutrition",nut);
                intent2.putExtra("status",status);
                System.out.println(status);
                System.out.println(nut);
                startActivity(intent2);
                break;
            case R.id.menu3:
                Intent intent3 = new Intent(getApplicationContext(), Recommend_food.class);
                intent3.putExtra("nutrition",nut);
                intent3.putExtra("status",status);
                startActivity(intent3);
                break;
        }

        toast.show();
        return super.onOptionsItemSelected(item);
    }

    private class GetData2 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                JsonResultString = result;
                InitializeQuestionData2();
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

    public void InitializeQuestionData2() {
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
            img_1.setBackgroundResource(R.drawable.three);
            System.out.println("들어옴");
            JSONObject jsonObject = new JSONObject(JsonResultString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            JSONObject item = jsonArray.getJSONObject(0);

            now_energy = item.getString(TAG_CAL);
            now_carbon = item.getString(TAG_CAR);
            now_protein = item.getString(TAG_PRO);
            now_fat = item.getString(TAG_FAT);
            now_sugar = item.getString(TAG_SUGAR);
            now_nat = item.getString(TAG_SOD);
            now_cole = item.getString(TAG_CHO);

            //float total_energy_fl=Float.parseFloat(total_energy);
            float now_carbon_fl=Float.parseFloat(now_carbon);
            float now_protein_fl=Float.parseFloat(now_protein);
            float now_fat_fl=Float.parseFloat(now_fat);
            float now_nat_fl=Float.parseFloat(now_nat);
            float now_sugar_fl=Float.parseFloat(now_sugar);
            float now_cole_fl=Float.parseFloat(now_cole);
            float now_energy_fl=Float.parseFloat(now_energy);

            //탄,단,지 비율 변수
            float total_cpf=0;
            float carbon_rate=0;
            float protein_rate=0;
            float fat_rate=0;
            float sugar_rate=0;

            total_cpf=now_carbon_fl+now_protein_fl+now_fat_fl;
            carbon_rate=now_carbon_fl/total_cpf;
            protein_rate=now_protein_fl/total_cpf;
            fat_rate=now_fat_fl/total_cpf;
            sugar_rate=now_sugar_fl/now_energy_fl;

            float value=0;
            nut="탄수화물";
            status = "부족";
            //1공복, 2부족, 3적정, 4초과,5과초과

            //비율로 계산-탄수화물
            if(carbon_rate>=55&&carbon_rate<=65) {
                img_1.setBackgroundResource(R.drawable.three);
            }
            else if(carbon_rate<55) {
                img_1.setBackgroundResource(R.drawable.two);
                if(value < Math.abs((carbon_rate-55)/carbon_rate) ){
                    value = Math.abs((carbon_rate-55)/carbon_rate);
                    status = "부족";
                    nut = "탄수화물";
                }
            }
            else {
                img_1.setBackgroundResource(R.drawable.four_s);
                if(value < Math.abs((carbon_rate-55)/carbon_rate) ){
                    value = Math.abs((carbon_rate-55)/carbon_rate);
                    status = "과잉";
                    nut = "탄수화물";
                }
            }
            //단백질
            if(protein_rate>=7&&protein_rate<=20) {
                img_2.setBackgroundResource(R.drawable.three);
            }
            else if(protein_rate>20) {
                img_2.setBackgroundResource(R.drawable.four_s);
                if(value < Math.abs((protein_rate-13)/protein_rate) ){
                    value = Math.abs((protein_rate-13)/protein_rate) ;
                    status = "과잉";
                    nut = "단백질";
                }
            }
            else {
                img_2.setBackgroundResource(R.drawable.two);
                if(value < Math.abs((protein_rate-13)/protein_rate) ){
                    value = Math.abs((protein_rate-7)/protein_rate) ;
                    status = "부족";
                    nut = "단백질";
                }
            }
            //지방
            if(fat_rate>=15&&fat_rate<=30)
                img_3.setBackgroundResource(R.drawable.three);
            else if(fat_rate>30) {
                img_3.setBackgroundResource(R.drawable.four_s);
                if(value < Math.abs((fat_rate-23)/fat_rate) ){
                    value = Math.abs((fat_rate-23)/fat_rate);
                    status = "과잉";
                    nut = "지방";
                }
            }
            else {
                img_3.setBackgroundResource(R.drawable.two);
                if(value < Math.abs((fat_rate-23)/fat_rate) ){
                    value = Math.abs((fat_rate-23)/fat_rate);
                    status = "부족";
                    nut = "지방";
                }
            }

            //나트륨 확인
            if(now_nat_fl<2000)
                img_4.setBackgroundResource(R.drawable.three);
            else if(now_nat_fl>2000) {
                img_4.setBackgroundResource(R.drawable.five_s);
                if(value < Math.abs((now_nat_fl-2000)/now_nat_fl) ){
                    value = Math.abs((now_nat_fl-2000)/now_nat_fl);
                    status = "과잉";
                    nut = "나트륨";
                }
            }
            else {
                img_4.setBackgroundResource(R.drawable.two);
                if(value < Math.abs((now_nat_fl-2000)/now_nat_fl) ){
                    value = Math.abs((now_nat_fl-2000)/now_nat_fl);
                    status = "과잉";
                    nut = "나트륨";
                }
            }
            //당류 확인
            if(sugar_rate>=10&&sugar_rate<=20) {
                img_5.setBackgroundResource(R.drawable.four_s);
                if(value < Math.abs((sugar_rate-15)/sugar_rate) ){
                    value = Math.abs((sugar_rate-15)/sugar_rate);
                    status = "과잉";
                    nut = "당류";
                }
            }
            else if(sugar_rate<10) {
                img_5.setBackgroundResource(R.drawable.two);
                if(value <  Math.abs((sugar_rate-15)/sugar_rate) ){
                    value =  Math.abs((sugar_rate-15)/sugar_rate);
                    status = "부족";
                    nut = "당류";
                }
            }
            else {
                img_5.setBackgroundResource(R.drawable.five_s);
                if(value <  Math.abs((sugar_rate-15)/sugar_rate) ){
                    value =  Math.abs((sugar_rate-15)/sugar_rate);
                    status = "과잉";
                    nut = "당류";
                }
            }

            //콜레스트롤 확인
            if(now_cole_fl<=300)
                img_6.setBackgroundResource(R.drawable.three);
            else {
                img_6.setBackgroundResource(R.drawable.four_s);
                if(value < Math.abs((now_cole_fl-300)/now_cole_fl) ){
                    value = Math.abs((now_cole_fl-300)/now_cole_fl);
                    status = "과잉";
                    nut = "콜레스테롤";
                }
            }
            //에너지 확인
            if(total_energy_fl>now_energy_fl) {
                img_7.setBackgroundResource(R.drawable.two);
                if(value < Math.abs((total_energy_fl-now_energy_fl)/total_energy_fl) ){
                    value = Math.abs((total_energy_fl-now_energy_fl)/total_energy_fl);
                    status = "부족";
                    nut = "에너지";
                }
            }
            else if(total_energy_fl==now_energy_fl)
                img_7.setBackgroundResource(R.drawable.three);
            else {
                img_7.setBackgroundResource(R.drawable.four_s);
                if(value < Math.abs((total_energy_fl-now_energy_fl)/total_energy_fl) ){
                    value = Math.abs((total_energy_fl-now_energy_fl)/total_energy_fl);
                    status = "과잉";
                    nut = "에너지";
                }
            }

        } catch (JSONException e) {
        }
    }
    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Show_nutrient.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String)params[0];
            String user_name= (String)params[1];
            String today_carbon = (String)params[2];
            String today_protein = (String)params[3];
            String today_fat = (String)params[4];
            String today_nat = (String)params[5];
            String today_sugar = (String)params[6];
            String today_cole = (String)params[7];

            String postParameters = "&user_name=" +user_name + "&today_carbon=" + today_carbon + "&today_protein=" + today_protein + "&today_fat=" + today_fat  + "&today_nat=" + today_nat +"&today_sugar=" + today_sugar + "&today_cole=" + today_cole;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();
            } catch (Exception e) {
                System.out.println(e.toString());
                return new String("Error: " + e.getMessage());
            }
        }
    }
}