package com.example.nisha.skiweather;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    UserLocalStore ul;
    //MenuItem login,logout,getWeather,register;
   Menu menu;
    private Menu activityMenu;
    private MenuItem curMenuItem;
    Toolbar toolbar;
    MenuItem login ;
    MenuItem logout;
    MenuItem register;
    MenuItem getWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setTitle("Menu 1");
        setSupportActionBar(toolbar);
        ul = new UserLocalStore(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        if(ul.getUserLoggedIn())
            displaySelectedScreen(R.id.nav_get_weather);
        else
        displaySelectedScreen(R.id.nav_login);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        // toolbar = (Toolbar) findViewById(R.id.toolbar);

        menu.clear();
        MenuInflater inflater = getMenuInflater();
       if (ul.getUserLoggedIn())
           inflater.inflate(R.menu.logged_in, menu);
        else
           inflater.inflate(R.menu.activity_main_drawer, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (ul.getUserLoggedIn()) {
          // menu.clear();
            getMenuInflater().inflate(R.menu.logged_in, menu);
       }
        else {
          // menu.clear();
            getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        }



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //this.invalidateOptionsMenu();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //this.invalidateOptionsMenu();
        return true;
    }


    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_login:

                fragment = new LoginFragment();
                //this.invalidateOptionsMenu();
                System.out.println("in login" + ul.getUserLoggedIn());
                break;
            case R.id.nav_register:
               System.out.println("inRegister" + ul.getUserLoggedIn());
               // this.invalidateOptionsMenu();
                fragment = new RegisterFragment();
                break;
            case R.id.nav_get_weather:
               System.out.println("inweather" +ul.getUserLoggedIn());
                //this.invalidateOptionsMenu();
                fragment = new WeatherFragment();
                break;
            case R.id.nav_logout:
                ul.setUserLoggedIn(false);
                System.out.println("in logout" + ul.getUserLoggedIn());
                ul.clearUserData();
                fragment = new LoginFragment();
              //  this.invalidateOptionsMenu();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
