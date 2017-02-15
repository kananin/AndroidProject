package com.example.nisha.skiweather;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Created by Nisha on 12/19/2016.
 */
public class WeatherFragment extends Fragment implements View.OnClickListener{


    Spinner spinner_state,spinner_resort,spinner_days;
    ArrayAdapter<String> adapter_days;
    String endPoint = "";
    String response = "";
    String temp = "";
    int days_count = 0;
    int flag = 0;
    String resortName ="",today_date="",today_chance_of_snowfall="",total_SnowFall="";
    String top_max_temp,top_min_temp,mid_max_temp,mid_min_temp,bottom_max_temp,bottom_min_temp;
    String nine_am_fog,nine_am_frost,nine_am_chance_of_snow,nine_am_chance_of_rain,nine_am_visibility,nine_am_humidity,nine_am_chance_of_wind,nine_am_weather;
    String twelve_pm_weather="",three_pm_weather="",six_pm_weather="";
    String[] todays_date; String[] todays_chance_of_snowfall; String [] totals_SnowFall;String[] top_max_temp_array;
    String [] top_min_temp_array; String [] moonRiseArray; String [] moonSetArray; String[] sunRiseArray; String[] sunSetArray;
    String[] nine_am_weather_array; String[] twelve_pm_weather_array; String[] three_pm_weather_array; String[] six_pm_weather_array;
    int NoOfDays = 0;
    boolean resortExists=false;
    Button bt_getWeather;
    TextView detailsTextView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.weather, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Get Weather");

        spinner_state = (Spinner)view.findViewById(R.id.spinner_state);
        spinner_resort =(Spinner)view.findViewById(R.id.spinner_resort);
        spinner_days = (Spinner)view.findViewById(R.id.spinner_days);
        bt_getWeather = (Button)view.findViewById(R.id.get_weather_button);
        detailsTextView = (TextView) view.findViewById(R.id.details_text_view);
        bt_getWeather.setOnClickListener(this);
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = spinner_state.getSelectedItem().toString();

                int idSpinner = getResources().getIdentifier(name,"array",getActivity().getPackageName());
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout,list);
                // Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + "selected", Toast.LENGTH_LONG).show();
                //TextView spinnerText = (TextView) findViewById(R.id.spinner_state).getChildAt(0);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(idSpinner));
                //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.spinner_layout);
                spinner_resort.setAdapter(adapter);
                //adapter_days = ArrayAdapter.createFromResource(MainActivity.this,R.array.Days,android.R.layout.simple_spinner_dropdown_item);
                //adapter_days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //spinner_state.setAdapter(adapter);
                resortName  = spinner_resort.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Method Name : readData
     * Functionality : This method reads the data provided by the user
     * Returns : Name of the Resort
     */
    private String readData(View view) {
        //Initialize variables

        String ResortName = "";
        String Days ="";

        Days = spinner_days.getSelectedItem().toString();
        if(Days.equals("Today")){
            NoOfDays = 1;
            flag = 0;
        }
        else if(Days.equals("Tomorrow")){

            flag = 1;
            NoOfDays = 1;
        }
        else if(Days.equals("Weekend")){
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            System.out.println("Day is : " + day);
            if(day == 7){
                NoOfDays = 2;
                flag = 0;
            }
            else if(day == 1){
                NoOfDays = 1;
                flag = 6;
            }
            else{
                flag = 7 - day - 1;
                NoOfDays = 2;
            }
            // System.out.println("Day: " + day);


        }
        else if(Days.equals("1 day")){
            NoOfDays = 1;
            flag =0;

        }
        else if(Days.equals("2 days")){
            NoOfDays = 2;
            flag =0;
        }
        else if(Days.equals("3 days")){
            NoOfDays = 3;
            flag =0;
        }
        else if(Days.equals("4 days")){
            NoOfDays = 4;
            flag =0;
        }
        else if(Days.equals("5 days")){
            NoOfDays = 5;
            flag =0;
        }
        else if(Days.equals("6 days")){
            NoOfDays = 6;
            flag =0;
        }
        else if(Days.equals("7 days")){
            NoOfDays = 7;
            flag =0;
        }
        //Read the name of the Resort provided by the user

        ResortName = spinner_resort.getSelectedItem().toString();
        todays_date = new String[NoOfDays];
        todays_chance_of_snowfall = new String[NoOfDays];
        totals_SnowFall = new String[NoOfDays];
        top_max_temp_array = new String[NoOfDays];
        top_min_temp_array = new String[NoOfDays];
        moonRiseArray = new String[NoOfDays];
        moonSetArray = new String[NoOfDays];
        sunRiseArray = new String[NoOfDays];
        sunSetArray = new String[NoOfDays];
        nine_am_weather_array = new String[NoOfDays];
        twelve_pm_weather_array = new String[NoOfDays];
        three_pm_weather_array = new String[NoOfDays];
        six_pm_weather_array = new String[NoOfDays];
        // Lets the user know the info entered by them
       // TextView detailsTextView = (TextView) rootView.findViewById(R.id.details_text_view);
        detailsTextView.setText("Resort Name you entered is : " + ResortName +
                " and No of days are :" + NoOfDays + "Days = " + Days);
        return ResortName;

    }

    /**
     * Method Name : getSkiWeather
     * @param view Functionality : Constructs the API query and passes it to the Async Task
     *             to execute it
     */
    public void getSkiWeather(View view) {
        // variable declaration
        String outputFormat = "&format=json";
        // getting the name of the resort
        resortName = readData(view);
        // constructing the API query encoding any spaces in the resort name
        endPoint = "http://api.worldweatheronline.com/premium/v1/ski.ashx?key=your_key&q=";
        try {
            endPoint = endPoint + URLEncoder.encode(resortName, "UTF-8") + outputFormat;
        } catch (Exception e) {
        }
        //Printing the API query for testing
        System.out.println(endPoint);
        // String displayText = "";
        //Calling AsyncTask to hit the API to get data
        new ConnectApi(view);
        new ConnectApi().execute();

        // TextView detailsTextView = (TextView) findViewById(R.id.details_text_view);
        // detailsTextView.setText("Resort Name you entered is : " + displayText);

        // new ConnectApi().execute(endPoint);
    }


    /**
     * Class Name : ConnectApi
     * Function : Hits the API using the API Query and fetches the data
     */
    public class ConnectApi extends AsyncTask<String, Void, String> {
        View v;
        public ConnectApi(){}
       public ConnectApi(View view){

           v = view;
       }

        String DisplayInfo = "";
        JSONObject astronomyObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
            try {
                url = new URL(endPoint.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                urlConnection.getContent();
                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

                while ((temp = bReader.readLine()) != null) {
                    response += temp;
                }

                bReader.close();
                inStream.close();
                urlConnection.disconnect();
                ///System.out.println("Response " + response);
                object = (JSONObject) new JSONTokener(response).nextValue();
                System.out.println(object);

                JSONObject childObject = object.getJSONObject("data");
                System.out.println(childObject);
                if(childObject.has("error")){
                    JSONArray errorMessage = childObject.getJSONArray("error");
                    System.out.println(errorMessage);
                    resortExists = false;
                    response="";
                }
                else {
                    JSONArray requestJSONArray = childObject.getJSONArray("request");
                    //System.out.println(requestJSONArray);

                    JSONArray weatherJSONArray = childObject.getJSONArray("weather");
                    //System.out.println(weatherJSONArray);
                    if(NoOfDays > 7)
                        NoOfDays = 7;

                    System.out.println(" flag - " + flag + " and NoOfDays - " + NoOfDays);
                    int j = flag;
                    for(int i = 0; i< NoOfDays; i++){
                        JSONObject todaysJSONObject = weatherJSONArray.getJSONObject(j);
                        todays_chance_of_snowfall[i] = todaysJSONObject.getString("chanceofsnow");
                        todays_date[i] = todaysJSONObject.getString("date");
                        System.out.println("Date - " + todays_date[i]);
                        totals_SnowFall[i] = todaysJSONObject.getString("totalSnowfall_cm");
                        JSONArray top = todaysJSONObject.getJSONArray("top");
                        JSONObject topObject = top.getJSONObject(0);
                        top_max_temp_array[i] =topObject.getString("maxtempC");
                        top_min_temp_array[i] = topObject.getString("mintempC");
                        JSONArray astronomy = todaysJSONObject.getJSONArray("astronomy");
                        astronomyObject = astronomy.getJSONObject(0);
                        moonRiseArray[i] = astronomyObject.getString("moonrise");
                        moonSetArray[i] = astronomyObject.getString("moonset");
                        sunRiseArray[i] = astronomyObject.getString("sunrise");
                        sunSetArray[i] = astronomyObject.getString("sunset");

                        JSONArray hourly = todaysJSONObject.getJSONArray("hourly");
                        JSONObject nineam = hourly.getJSONObject(3);
                        JSONObject twelvepm = hourly.getJSONObject(4);
                        JSONObject threepm = hourly.getJSONObject(5);
                        JSONObject sixpm = hourly.getJSONObject(6);
                        System.out.println("Nine am - " + nineam);
                        nine_am_weather_array[i] = "Chance of Snow :" + nineam.getString("chanceofsnow") + "\r\n";
                        nine_am_weather_array[i] = nine_am_weather_array[i] + "Chance of Rain :" + nineam.getString("chanceofrain") + "\r\n";
                        nine_am_weather_array[i] = nine_am_weather_array[i] + "Chance of Wind :" + nineam.getString("chanceofwindy") + "\r\n";
                        nine_am_weather_array[i] = nine_am_weather_array[i] + "Visibility :" + nineam.getString("visibility") + "\r\n";
                        nine_am_weather_array[i] = nine_am_weather_array[i] + "Chance of Fog :" + nineam.getString("chanceoffog") + "\r\n";
                        nine_am_weather_array[i] = nine_am_weather_array[i] + "Chance of Frost :" + nineam.getString("chanceoffrost") + "\r\n";
                        nine_am_weather_array[i] = nine_am_weather_array[i] + "Chance of humidity :" + nineam.getString("humidity") + "\r\n";
                        System.out.println("Helloooooooooo");
                        System.out.println("Hello " + nine_am_weather_array[i]);
                        twelve_pm_weather_array[i] = "Chance of Snow :" + twelvepm.getString("chanceofsnow") + "\r\n";
                        twelve_pm_weather_array[i] = twelve_pm_weather_array[i] + "Chance of Rain :" + twelvepm.getString("chanceofrain") + "\r\n";
                        twelve_pm_weather_array[i] = twelve_pm_weather_array[i] + "Chance of Wind :" + twelvepm.getString("chanceofwindy") + "\r\n";
                        twelve_pm_weather_array[i] = twelve_pm_weather_array[i] + "Visibility :" + twelvepm.getString("visibility") + "\r\n";
                        twelve_pm_weather_array[i] = twelve_pm_weather_array[i] + "Chance of Fog :" + twelvepm.getString("chanceoffog") + "\r\n";
                        twelve_pm_weather_array[i] = twelve_pm_weather_array[i] + "Chance of Frost :" + twelvepm.getString("chanceoffrost") + "\r\n";
                        twelve_pm_weather_array[i] = twelve_pm_weather_array[i] + "Chance of humidity :" + twelvepm.getString("humidity") + "\r\n";
                        three_pm_weather_array[i] = "Chance of Snow :" + threepm.getString("chanceofsnow") + "\r\n";
                        three_pm_weather_array[i] = three_pm_weather_array[i] + "Chance of Rain :" + threepm.getString("chanceofrain") + "\r\n";
                        three_pm_weather_array[i] = three_pm_weather_array[i] + "Chance of Wind :" + threepm.getString("chanceofwindy") + "\r\n";
                        three_pm_weather_array[i] = three_pm_weather_array[i] + "Visibility :" + threepm.getString("visibility") + "\r\n";
                        three_pm_weather_array[i] = three_pm_weather_array[i] + "Chance of Fog :" + threepm.getString("chanceoffog") + "\r\n";
                        three_pm_weather_array[i] = three_pm_weather_array[i] + "Chance of Frost :" + threepm.getString("chanceoffrost") + "\r\n";
                        three_pm_weather_array[i] = three_pm_weather_array[i] + "Chance of humidity :" + threepm.getString("humidity") + "\r\n";
                        six_pm_weather_array[i] = "Chance of Snow :" + sixpm.getString("chanceofsnow") + "\r\n";
                        six_pm_weather_array[i] = six_pm_weather_array[i] + "Chance of Rain :" + sixpm.getString("chanceofrain") + "\r\n";
                        six_pm_weather_array[i] = six_pm_weather_array[i] + "Chance of Wind :" + sixpm.getString("chanceofwindy") + "\r\n";
                        six_pm_weather_array[i] = six_pm_weather_array[i] + "Visibility :" + sixpm.getString("visibility") + "\r\n";
                        six_pm_weather_array[i] = six_pm_weather_array[i] + "Chance of Fog :" + sixpm.getString("chanceoffog") + "\r\n";
                        six_pm_weather_array[i] = six_pm_weather_array[i] + "Chance of Frost :" + sixpm.getString("chanceoffrost") + "\r\n";
                        six_pm_weather_array[i] = six_pm_weather_array[i] + "Chance of humidity :" + sixpm.getString("humidity") + "\r\n";

                        j++;

                    }
                    resortExists = true;
                    response="";

                }

                return response;
                //System.out.println(object.);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inStream != null) {
                    try {
// this will close the bReader as well
                        inStream.close();
                    } catch (IOException ignored) {
                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }


                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("In Post execution...");
            Fragment fragment;
             fragment = new DisplayWeather();

            Bundle extras = new Bundle();
            if(resortExists) {
                extras.putString("ResortName", resortName);
                extras.putString("TodaysDate", today_date);
                extras.putString("ChanceOfSnow", today_chance_of_snowfall);
                extras.putString("TotalSnowFall", total_SnowFall);
                extras.putString("Top_Max_Temp", top_max_temp);
                extras.putString("Top_Min_Temp", top_min_temp);
                extras.putStringArray("dateArray",todays_date);
                extras.putStringArray("TotalSnowFallArray",totals_SnowFall);
                extras.putStringArray("ChanceOfSnowArray",todays_chance_of_snowfall);
                extras.putStringArray("TopMaxTempArray",top_max_temp_array);
                extras.putStringArray("TopMinTempArray",top_min_temp_array);
                extras.putInt("NoOfDays",NoOfDays);
                extras.putStringArray("MoonRiseArray",moonRiseArray);
                extras.putStringArray("MoonSetArray",moonSetArray);
                extras.putStringArray("SunRiseArray",sunRiseArray);
                extras.putStringArray("SunSetArray",sunSetArray);
                extras.putStringArray("NineAmWeatherArray",nine_am_weather_array);
                extras.putStringArray("TwelvePmWeatherArray",twelve_pm_weather_array);
                extras.putStringArray("ThreePmWeatherArray",three_pm_weather_array);
                extras.putStringArray("SixPmWeatherArray",six_pm_weather_array);
                resortExists = false;

                fragment.setArguments(extras);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
            else{
                //TextView detailsTextView = (TextView) v.findViewById(R.id.details_text_view);
                detailsTextView.setText("Resort Name you entered is : " + resortName +
                       " and no such resort exists");

            }


        }

    }
    @Override
    public void onClick(View v) {
        // variable declaration
        String outputFormat = "&format=json";
        // getting the name of the resort
        resortName = readData(v);
        // constructing the API query encoding any spaces in the resort name
        endPoint = "http://api.worldweatheronline.com/premium/v1/ski.ashx?key=cc7d024713a94ba18b9165115162711&q=";
        try {
            endPoint = endPoint + URLEncoder.encode(resortName, "UTF-8") + outputFormat;
        } catch (Exception e) {
        }
        //Printing the API query for testing
        System.out.println(endPoint);
        // String displayText = "";
        //Calling AsyncTask to hit the API to get data
        new ConnectApi().execute();


    }
}
