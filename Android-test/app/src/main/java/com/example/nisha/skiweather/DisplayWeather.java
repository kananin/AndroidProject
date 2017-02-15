

package com.example.nisha.skiweather;

/**
 * Created by Nisha on 12/19/2016.
 */

        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AppCompatActivity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;

        import android.widget.ExpandableListView;
        import android.widget.TextView;

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
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

public class DisplayWeather extends Fragment {
    String endPoint = "";
    String response = "";
    String temp = "";

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String moonrise,moonset,sunrise,sunset,date1="",resortName="",chanceOfSnow="",totalSnowFall = "",topMaxTemp="",topMinTemp="";
    String nineAmWeather="",twelvePmWeather="",threePmWeather="",sixPmWeather="";
    String[] dateArray; String[] chanceOfSnowArray; String[] totalSnowFallArray;
    String[] topMaxTempArray; String[] topMinTempArray;
    String[] moonRiseArray; String[] moonSetArray; String[] sunriseArray; String[] sunsetArray;
    String[] nineAmWeatherArray; String[] twelvePmWeatherArray; String[] threePmWeatherArray; String[] sixPmWeatherArray;
    int noOfDays = 0;
    TextView resortNameTextView;
    TextView todaysDateTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.activity_display, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Display Weather");
        resortNameTextView = (TextView)view.findViewById(R.id.textView_ResortName);
        todaysDateTextView = (TextView)view.findViewById(R.id.textView_Date);
        getData();

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


    }


    /**
     * This method reads the data provided by the user
     */
    public void getData()
    {
        noOfDays = getArguments().getInt("NoOfDays");
        chanceOfSnowArray = new String[noOfDays];
        totalSnowFallArray = new String[noOfDays];
        dateArray = new String[noOfDays];

        chanceOfSnowArray = getArguments().getStringArray("ChanceOfSnowArray");
        totalSnowFallArray = getArguments().getStringArray("TotalSnowFallArray");
        dateArray = getArguments().getStringArray("dateArray");
        topMaxTempArray = getArguments().getStringArray("TopMaxTempArray");
        topMinTempArray= getArguments().getStringArray("TopMinTempArray");
        moonRiseArray = getArguments().getStringArray("MoonRiseArray");
        moonSetArray = getArguments().getStringArray("MoonSetArray");
        sunriseArray = getArguments().getStringArray("SunRiseArray");
        sunsetArray = getArguments().getStringArray("SunSetArray");
        nineAmWeatherArray = getArguments().getStringArray("NineAmWeatherArray");
        twelvePmWeatherArray = getArguments().getStringArray("TwelvePmWeatherArray");
        threePmWeatherArray = getArguments().getStringArray("ThreePmWeatherArray");
        sixPmWeatherArray = getArguments().getStringArray("SixPmWeatherArray");
        resortName = getArguments().getString("ResortName");
        date1 = getArguments().getString("TodaysDate");
        chanceOfSnow = "Chance Of Snow :" + getArguments().getString("ChanceOfSnow");
        totalSnowFall ="Total Snow Fall :" + getArguments().getString("TotalSnowFall");
        topMaxTemp = getArguments().getString("Top_Max_Temp");
        topMinTemp = getArguments().getString("Top_Min_Temp");



       // System.out.println("Hello from activity 2");

        JSONObject astronomyObject;
        resortNameTextView.setText(resortName);

        todaysDateTextView.setText(date1);


    }



    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        for(int i = 0; i< noOfDays; i++) {
            // listDataHeader.add("Date is :" + dateArray[i]);
            listDataHeader.add("Weather on " + dateArray[i]);
            listDataHeader.add("Astronomy");
            listDataHeader.add("Hourly");

        }





        ArrayList<String>[] astronomy = new ArrayList[noOfDays];
        for(int i = 0; i< noOfDays; i++)
        {
            astronomy[i] = new ArrayList<>();
            astronomy[i].add("Moonrise :" + moonRiseArray[i]);
            astronomy[i].add("Moonset :" + moonSetArray[i]);
            astronomy[i].add("Sunrise :" + sunriseArray[i]);
            astronomy[i].add("Sunset :" + sunsetArray[i]);
        }

        //List<String> weatherToday = new ArrayList<String>();
        ArrayList<String>[] weatherToday = new ArrayList[noOfDays];
        for(int i = 0; i< noOfDays ; i++) {
            weatherToday[i] = new ArrayList<>();
            weatherToday[i].add(" Chance of Snow : " + chanceOfSnowArray[i]);
            weatherToday[i].add(" Total snow fall : " + totalSnowFallArray[i] + " cm");
            weatherToday[i].add("Top max temperature : " +topMaxTempArray[i] + " deg C");
            weatherToday[i].add("Top min temperature : " +topMinTempArray[i] + " deg C");
        }

        ArrayList<String>[] hourly = new ArrayList[noOfDays];
        //List<String> hourly = new ArrayList<String>();
        for(int i = 0; i< noOfDays; i++) {
            hourly[i] = new ArrayList<>();
            hourly[i].add("9 am");
            hourly[i].add(nineAmWeatherArray[i]);
            hourly[i].add("12 pm");
            hourly[i].add(twelvePmWeatherArray[i]);
            hourly[i].add("3 pm");
            hourly[i].add(threePmWeatherArray[i]);
            hourly[i].add("6 pm");
            hourly[i].add(sixPmWeatherArray[i]);
        }
        int j = 0;
        for(int i = 0; i < (noOfDays); i++) {


            listDataChild.put(listDataHeader.get(j), weatherToday[i]);
            j++;
            listDataChild.put(listDataHeader.get(j), astronomy[i]);
            j++;
            listDataChild.put(listDataHeader.get(j), hourly[i]);
            j++;
            // }
        }
        // Header, Child data


    }


}

