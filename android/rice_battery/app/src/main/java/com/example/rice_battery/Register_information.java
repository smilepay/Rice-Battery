package com.example.rice_battery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Register_information extends AppCompatActivity {

    EditText nickname,age,gender,height,weight;
    Button but;
    private Spinner spinner2;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    String active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_information);

        nickname =  findViewById(R.id.name_edt);
        age = findViewById(R.id.age_edt);
        gender = findViewById(R.id.sex_edt);
        height =  findViewById(R.id.height_edt);
        weight = findViewById(R.id.weight_edt);

        arrayList = new ArrayList<>();
        arrayList.add("활동 적음");
        arrayList.add("약간 활동적");
        arrayList.add("활동적");
        arrayList.add("매우 활동적");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner2.setAdapter(arrayAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                active = arrayList.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),"활동정도를 선택해주세요",
                        Toast.LENGTH_SHORT).show();
            }
        });

        but = findViewById(R.id.complete_btn);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nick_name = String.valueOf(nickname.getText());
                String user_age = String.valueOf(age.getText());
                String user_gender = String.valueOf(gender.getText());
                String user_height = String.valueOf(height.getText());
                String user_weight = String.valueOf(weight.getText());

                double basic=0,work=0,digest=0,whole=0,nat=0,sugar=0,cole=0;
                nat = 2000;
                cole = 300;
                if ((user_gender.equals("여")) && Integer.parseInt(user_age) <=18){
                    basic = 189-17.6*Integer.parseInt(user_age)+625*Integer.parseInt(user_height)+7.9*Integer.parseInt(user_weight);
                }
                else if ((user_gender.equals("남")) && Integer.parseInt(user_age) <=18){
                    basic = 68-43.3*Integer.parseInt(user_age)+712*Integer.parseInt(user_height)+19.2*Integer.parseInt(user_weight);
                }
                else if (user_gender.equals("여") && Integer.parseInt(user_age) >18){
                    basic = 255-2.35*Integer.parseInt(user_age)+361.6*Integer.parseInt(user_height)+9.39*Integer.parseInt(user_weight);
                }
                else if (user_gender.equals("남") && Integer.parseInt(user_age) >18){
                    basic = 204-4*Integer.parseInt(user_age)+450.5*Integer.parseInt(user_height)+11.69*Integer.parseInt(user_weight);
                }

                if(active.equals("활동 적음")) {
                    work = basic * 0.3;
                }
                else if(active.equals("약간 활동적")){
                    work = basic * 0.55;
                }
                else if(active.equals("활동적")) {
                    work = basic * 0.7;
                }
                else if(active.equals("매우 활동적")){
                    work = basic * 0.9;
                }
                digest = ((basic+work)/0.9)*0.1;
                whole = basic+work+digest;
                sugar = whole * 0.1;

               // InsertData task = new InsertData();
                //task.execute("http://203.245.10.33:8888/rice/userinfo_insert.php", nick_name, user_age, user_gender, user_height, user_weight,Double.toString(basic),Double.toString(work),Double.toString(digest),Double.toString(whole),Double.toString(nat),Double.toString(sugar),Double.toString(cole));
                Intent intent = new Intent(getApplicationContext(), Food_input.class);
                intent.putExtra("user_name",String.valueOf(nickname.getText()));
                intent.putExtra("whole",String.valueOf(whole));
                startActivity(intent);
            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Register_information.this,
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
            String nickname= (String)params[1];
            String age = (String)params[2];
            String gender = (String)params[3];
            String height = (String)params[4];
            String weight = (String)params[5];
            String basic = (String)params[6];
            String work = (String)params[7];
            String digest = (String)params[8];
            String whole = (String)params[9];
            String nat = (String)params[10];
            String sugar = (String)params[11];
            String cole = (String)params[12];

            String postParameters = "&user_name=" +nickname + "&user_age=" + age + "&user_gender=" + gender + "&user_height=" + height  + "&user_weight=" + weight +"&user_active=" + active + "&user_basic=" + basic + "&user_work=" + work + "&user_digest=" + digest + "&user_whole=" + whole + "&user_nat=" + nat + "&user_sugar=" + sugar + "&user_cole=" + cole;

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