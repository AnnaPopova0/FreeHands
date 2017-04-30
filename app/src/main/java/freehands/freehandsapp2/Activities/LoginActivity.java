package freehands.freehandsapp2.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import freehands.freehandsapp2.Auth;
import freehands.freehandsapp2.Fragments.RegistrationFragment;
import freehands.freehandsapp2.HttpProvider;
import freehands.freehandsapp2.R;
import freehands.freehandsapp2.StringValues;
import okhttp3.Response;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

public class LoginActivity extends AppCompatActivity implements RegistrationFragment.RegistrationListener, StringValues {


    //UI references
    private AutoCompleteTextView emailInput;
    private EditText passwordInput;
    private Button signInBtn;
    private TextView registerLink;
    private FrameLayout frameLogIn, fragmentFrame;
    private ProgressBar progressLogIn;
    private View loginFormLayot;
    String email, password;
    String baseToken;
    String loginToken;



    private boolean isRegistration = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Log in");



        SharedPreferences sharedPreferences = getSharedPreferences(AUTH, MODE_PRIVATE);
        baseToken = sharedPreferences.getString(AUTH_TOKEN, null);
        Log.d("MY_LOG", "From LoginActivity - authToken is " + baseToken);
        System.out.println("MY_LOG" + " // restart authToken is " + baseToken);
        if (baseToken != null) {
            startPersonActivity();
        }


        loginFormLayot = findViewById(R.id.login_form_layout);
        fragmentFrame = (FrameLayout) findViewById(R.id.fragment_frame);
        progressLogIn = (ProgressBar) findViewById(R.id.progress_login);
        frameLogIn = (FrameLayout) findViewById(R.id.frame_log_in);

        emailInput = (AutoCompleteTextView) findViewById(R.id.email_input);
        passwordInput = (EditText) findViewById(R.id.password_input);

        registerLink = (TextView) findViewById(R.id.register_link);

        signInBtn = (Button) findViewById(R.id.sign_in_btn);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptLogin();
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegistrationFragment();
            }
        });


    }

    private void attemptLogin() {
        emailInput.setError(null);
        passwordInput.setError(null);

        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        //checking email field
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("This field required!");
            emailInput.requestFocus();
        } else if (!isEmailValid(email)) {
            emailInput.setError("This email address is invalid");
            emailInput.requestFocus();
        }

        //checking password field
        else if (TextUtils.isEmpty(password)) {
            passwordInput.setError("This field is required");
            passwordInput.requestFocus();
        } else if (!isPasswordValid(password)) {
            passwordInput.setError("Password is too short");
            passwordInput.requestFocus();
        } else {
            new LoginAsyncTask().execute();
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    private void startPersonActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        finish();
    }

    private void showRegistrationFragment() {

        isRegistration = true;
        RegistrationFragment fragment = new RegistrationFragment();
        fragment.setCallbackListener(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_frame, fragment, "REG_FRAG");
        transaction.commit();
        loginFormLayot.setVisibility(View.GONE);
        fragmentFrame.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle("Registration");
    }

    @Override
    public void onBackPressed() {
        if (isRegistration) {
            RegistrationFragment fragment = (RegistrationFragment) getSupportFragmentManager().findFragmentByTag("REG_FRAG");
            if (fragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(fragment);
                transaction.commit();
            }
            fragmentFrame.setVisibility(View.GONE);
            loginFormLayot.setVisibility(View.VISIBLE);
            isRegistration = false;
            getSupportActionBar().setTitle("Login");
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void registrationSuccess(String authToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("AUTH", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_TOKEN, authToken);
        editor.commit();
        Toast.makeText(this, "Successfull registration", Toast.LENGTH_SHORT).show();
        startPersonActivity();
    }



    //inner asynctask class

    private class LoginAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            email = emailInput.getText().toString();
            password = passwordInput.getText().toString();

            progressLogIn.setVisibility(View.VISIBLE);
            frameLogIn.setVisibility(View.VISIBLE);

            emailInput.setEnabled(false);
            passwordInput.setEnabled(false);
            signInBtn.setEnabled(false);
        }

        @Override
        protected String doInBackground(Void... params) {

            String result = LOGGING_RESULT;

            boolean isLogged = false;
            Response response = null;
            loginToken = new Auth().getBasicAuth(email, password);


            try {
                response = HttpProvider.getInstance().login(loginToken);

                if (response.code() == HTTP_BAD_REQUEST) {
                    result = "Server error occured, please try again later";
                }  else if (response.code() == HTTP_INTERNAL_ERROR) {
                    result = "User is not registered";
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "Connection problem";
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            progressLogIn.setVisibility(View.INVISIBLE);
            frameLogIn.setVisibility(View.INVISIBLE);

            if (s == LOGGING_RESULT) {
                SharedPreferences sharedPreferences = getSharedPreferences(AUTH, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(AUTH_TOKEN, loginToken);
                editor.commit();
                Toast.makeText(LoginActivity.this, LOGGING_RESULT, Toast.LENGTH_SHORT).show();
                startPersonActivity();
            }else{
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                emailInput.setEnabled(true);
                passwordInput.setEnabled(true);
                signInBtn.setEnabled(true);
            }
        }

    }
}
