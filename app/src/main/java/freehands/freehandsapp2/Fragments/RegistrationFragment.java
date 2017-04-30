package freehands.freehandsapp2.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import freehands.freehandsapp2.HttpProvider;
import freehands.freehandsapp2.R;

/**
 * Created by jagor on 4/8/2017.
 */

public class RegistrationFragment extends Fragment {

    private EditText inputEmailReg, inputPasswordReg;
    private Button registerBtn;
    private RegistrationListener listener;
    private FrameLayout frameRegistration;
    private ProgressBar progressRegistration;

    String email = null;
    String password = null;

    public void setCallbackListener(RegistrationListener listener) {
        this.listener = listener;
    }

    public RegistrationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registration_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmailReg = (EditText) view.findViewById(R.id.input_email_registration);
        inputPasswordReg = (EditText) view.findViewById(R.id.input_password_registration);
        frameRegistration = (FrameLayout) view.findViewById(R.id.frame_registration);
        progressRegistration = (ProgressBar) view.findViewById(R.id.progress_registration);

        registerBtn = (Button) view.findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();

            }
        });
    }

    private void attemptRegister() {

        inputEmailReg.setError(null);
        inputPasswordReg.setError(null);

        email = inputEmailReg.getText().toString();
        password = inputPasswordReg.getText().toString();

        //checking email field
        if (TextUtils.isEmpty(email)) {
            inputEmailReg.setError("This field required!");
            inputEmailReg.requestFocus();
        } else if (!isEmailValid(email)) {
            inputEmailReg.setError("This email address is invalid");
            inputEmailReg.requestFocus();
        }

        //checking password field
        else if (TextUtils.isEmpty(password)) {
            inputPasswordReg.setError("This field is required");
            inputPasswordReg.requestFocus();
        } else if (!isPasswordValid(password)) {
            inputPasswordReg.setError("Password is too short");
            inputPasswordReg.requestFocus();
        } else {
            new RegistrationAsyncTask().execute();
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }


    //Listener inner interface
    public interface RegistrationListener {
        void registrationSuccess(String authToken);
    }


    //inner class RegistrationAsyncTask
    public class RegistrationAsyncTask extends AsyncTask<Void, Void, String> {
//        String email, password;

        public RegistrationAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
//            email = inputEmailReg.getText().toString();
//            password = inputPasswordReg.getText().toString();

            frameRegistration.setVisibility(View.VISIBLE);
            progressRegistration.setVisibility(View.VISIBLE);

            inputEmailReg.setEnabled(false);
            inputPasswordReg.setEnabled(false);
            registerBtn.setEnabled(false);


        }

        @Override
        protected String doInBackground(Void... params) {
            String authToken = null;


            try {
                authToken = HttpProvider.getInstance().registration(email, password);
                Log.d("MY_LOG", "from doInBackg - auth Token is " + authToken.toString());

            } catch (Exception e) {

                e.printStackTrace();
            }

            return authToken;
        }

        @Override
        protected void onPostExecute(String s) {

            progressRegistration.setVisibility(View.INVISIBLE);
            frameRegistration.setVisibility(View.INVISIBLE);

            Log.d("MY_TAG", "onPostExecute: s is " + s);

            if (s != null) {
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("AUTH_TOKEN", s);
//                editor.commit();
                listener.registrationSuccess(s);
            } else {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        }
    }
}

