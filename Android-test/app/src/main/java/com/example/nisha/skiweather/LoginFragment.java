
package com.example.nisha.skiweather;
        import android.app.ProgressDialog;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.widget.Toolbar;
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


public class LoginFragment extends Fragment implements View.OnClickListener {

// enter your port number
    // 10.0.2.2 - local host for simulator
    private static final String REGISTER_URL = "http://10.0.2.2:port_number/registration/login.php";

    Button bt_login;
    EditText et_username;
    EditText et_password;
    UserLocalStore ul;
   // EditText et_email;
  // Toolbar toolbar ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //changing R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.login, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Login");
        ul = new UserLocalStore(getActivity());
        bt_login = (Button)view.findViewById(R.id.bt_login);
        et_username = (EditText)view.findViewById(R.id.et_username);
        et_password = (EditText)view.findViewById(R.id.et_password);
       // toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        bt_login.setOnClickListener(this);
       // setHasOptionsMenu(false);
    }


    @Override
    public void onClick(View v) {
        if(v == bt_login){
            loginUser();
            User user = new User(et_username.getText().toString().trim().toLowerCase(),"",et_username.getText().toString().trim().toLowerCase());
        }
    }

    private void loginUser(){

        String username = et_username.getText().toString().trim().toLowerCase();
        String email = "";
        String password = et_username.getText().toString().trim().toLowerCase();
        login(username,password,email);
    }

    private void login(String username, String password, String email) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            LoginUserClass ruc = new LoginUserClass();
            int flag = 0;
            Fragment fragment;
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
                if(flag==1){

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
                System.out.println("result " + result);

                if(result.equals("successfully logedin ")){

                ul.setUserLoggedIn(true);

                    flag =1;

                }
                else {
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