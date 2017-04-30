package freehands.freehandsapp2.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import freehands.freehandsapp2.Fragments.PersonAdressesFragment;
import freehands.freehandsapp2.Fragments.StartCategoriesForGiftFragment;
import freehands.freehandsapp2.HttpProvider;
import freehands.freehandsapp2.R;
import freehands.freehandsapp2.StringValues;
import freehands.freehandsapp2.Models.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, StringValues {

    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private FrameLayout fragmentFrame;

    private TextView userFirstName, userEmailTxt;

    public boolean startGiftCategoriesfragment = false;

    public boolean returnfromPersonAddressesFragment = false;
    private User user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Gifts list");


        fragmentFrame = (FrameLayout) findViewById(R.id.fragment_frame);



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGiftCategoriesfragment = true;
                fragmentFrame.setVisibility(View.VISIBLE);
                StartCategoriesForGiftFragment fragment = new StartCategoriesForGiftFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_frame, fragment, "START_CGIFT_CATEGORIES");
                transaction.commit();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        new GetUserInfoAsyncTask().execute();


    }

    @Override
    public void onBackPressed() {
        if (startGiftCategoriesfragment) {
            StartCategoriesForGiftFragment fragment = (StartCategoriesForGiftFragment) getSupportFragmentManager().findFragmentByTag("START_CGIFT_CATEGORIES");
            if (fragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(fragment);
                transaction.commit();
            }
            fragmentFrame.setVisibility(View.INVISIBLE);
            startGiftCategoriesfragment = false;
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        userFirstName = (TextView) findViewById(R.id.user_first_name);
        userEmailTxt = (TextView) findViewById(R.id.user_email_txt);
        SharedPreferences sp = getSharedPreferences(PERSON_INFO, MODE_PRIVATE);
        String name = sp.getString(PERSON_NAME, null);
        String email = sp.getString(PERSON_EMAIL, null);
        if (name == null) {
            userFirstName.setText("Your name");
        } else {
            userFirstName.setText(name);
        }


        userEmailTxt.setText(email);




        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            Log.d(TAG, "onNavigationItemSelected: starting new Activity");


        } else if (id == R.id.nav_my_wishes) {

        } else if (id == R.id.nav_dialogs) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_addresses) {
            fragmentFrame.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            PersonAdressesFragment personAdressesFragment = new PersonAdressesFragment();
            transaction.replace(R.id.fragment_frame, personAdressesFragment);
            transaction.commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {


            showLoginActivityFinish(user);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLoginActivityFinish(User user) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        if (user != null) {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
        } else if (user == null) {
            Toast.makeText(this, LOGIN_ERROR_DUE_REG, Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sPref = getSharedPreferences(AUTH, MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.clear();
        editor.commit();

        startActivity(intent);
        finish();
    }


//    @Override
//    public void infoUpdated() {
//        new GetUserInfoAsyncTask().execute();
//    }


    public class GetUserInfoAsyncTask extends AsyncTask<Void, Void, User> {
        String authToken = null;

        @Override
        protected void onPreExecute() {
            SharedPreferences sharedPreferences = getSharedPreferences(AUTH, MODE_PRIVATE);
            authToken = sharedPreferences.getString(AUTH_TOKEN, null);

        }

        @Override
        protected User doInBackground(Void... params) {
            User user1 = new User();
            try {
             user1 = HttpProvider.getInstance().getUserInfo(authToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return user1;
        }


        @Override
        protected void onPostExecute(User user) {

            if (user == null) {
                showLoginActivityFinish(user);
            } else {

                String name = user.getFirstName();
                String email = user.getEmail();
                String phone = user.getPhone();
                List<String> addresses = user.getAddresses();
                Log.d("MY_LOG", "addresses " + addresses);



                SharedPreferences sharedPreferences = getSharedPreferences(PERSON_INFO, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PERSON_NAME, name);
                editor.putString(PERSON_EMAIL, email);
                editor.putString(PERSON_PHONE, phone);
                editor.commit();
            }
        }


    }
}



