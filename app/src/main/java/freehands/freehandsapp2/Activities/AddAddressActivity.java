package freehands.freehandsapp2.Activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import freehands.freehandsapp2.HttpProvider;
import freehands.freehandsapp2.Models.Address;
import freehands.freehandsapp2.Models.User;
import freehands.freehandsapp2.R;

import static freehands.freehandsapp2.StringValues.AUTH;
import static freehands.freehandsapp2.StringValues.AUTH_TOKEN;

public class AddAddressActivity extends AppCompatActivity {

    private EditText countryInput, cityInput, streetInput;
    private Button saveAddressBtn;
    private FrameLayout addAddressFrame;
    private ProgressBar addAddressProgress;

    private String authToken;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        setTitle("Adding new address");

        SharedPreferences sharedPreferences = getSharedPreferences(AUTH, MODE_PRIVATE);
        authToken = sharedPreferences.getString(AUTH_TOKEN, null);

        countryInput = (EditText) findViewById(R.id.country_input);
        cityInput = (EditText) findViewById(R.id.city_input);
        streetInput = (EditText) findViewById(R.id.street_input);
        saveAddressBtn = (Button) findViewById(R.id.save_address_btn);

        saveAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country = countryInput.getText().toString();
                String city = cityInput.getText().toString();
                String street = streetInput.getText().toString();

                Address address = new Address(country, city, street);

                json = new Gson().toJson(address);

                new AddAddressAsyncTask().execute();
            }
        });



    }

    private class AddAddressAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            addAddressFrame = (FrameLayout) findViewById(R.id.add_address_frame);
            addAddressProgress = (ProgressBar) findViewById(R.id.add_address_progress);

            countryInput.setEnabled(false);
            cityInput.setEnabled(false);
            streetInput.setEnabled(false);
            saveAddressBtn.setEnabled(false);

            addAddressProgress.setVisibility(View.VISIBLE);
            addAddressFrame.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpProvider.getInstance().addAddress(authToken, json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            countryInput.setEnabled(true);
            cityInput.setEnabled(true);
            streetInput.setEnabled(true);
            saveAddressBtn.setEnabled(true);
            Toast.makeText(AddAddressActivity.this, "New address has been added", Toast.LENGTH_SHORT).show();

            addAddressProgress.setVisibility(View.INVISIBLE);
            addAddressFrame.setVisibility(View.INVISIBLE);
            onBackPressed();
        }
    }

}
