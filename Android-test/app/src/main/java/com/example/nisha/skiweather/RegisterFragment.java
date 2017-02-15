package com.example.nisha.skiweather;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Nisha on 12/19/2016.
 */


public class RegisterFragment extends Fragment implements View.OnClickListener {


    private static final String REGISTER_URL = "http://10.0.2.2:port_number/registration/register.php";

    Button bt_register;
    EditText et_username;
    EditText et_password;
    EditText et_email;
    UserLocalStore ul;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.activity_register, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Register");

        bt_register = (Button)view.findViewById(R.id.bt_register);
        et_username = (EditText)view.findViewById(R.id.et_username_reg);
        et_email =(EditText)view.findViewById(R.id.et_email);
        et_password = (EditText)view.findViewById(R.id.et_password_reg);
        ul = new UserLocalStore(getActivity());
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
            Fragment fragment;
            int flag =0;




            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity().getApplicationContext(),s, Toast.LENGTH_LONG).show();
                if(flag == 1){
                    fragment = new WeatherFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                     ft.commit();

                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("username",params[0]);
                data.put("password",params[1]);
                data.put("email",params[2]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);
                if(result.equals("successfully registered ")){
                    ul.setUserLoggedIn(true);
                    flag =1;

                }
                else
                {
                    ul.setUserLoggedIn(false);
                    flag=0;
                }



                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(username,password,email);
    }

}