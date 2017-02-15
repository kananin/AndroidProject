package com.example.nisha.skiweather;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private static final String REGISTER_URL = "http://skiweather.esy.es/UserRegistration/register.php";


    Button bt_register;
    EditText et_username;
    EditText et_password;
    EditText et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bt_register = (Button)findViewById(R.id.bt_register);
        et_username = (EditText)findViewById(R.id.et_username_reg);
        et_email =(EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password_reg);

        bt_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == bt_register){
            registerUser();
        }
    }

    private void registerUser(){

        String username = et_username.getText().toString().trim().toLowerCase();
        String email = et_email.getText().toString().trim().toLowerCase();
        String password = et_password.getText().toString().trim().toLowerCase();
        register(username,password,email);
    }

    private void register(String username, String password, String email) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("username",params[0]);
                data.put("password",params[1]);
                data.put("email",params[2]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(username,password,email);
    }

}
