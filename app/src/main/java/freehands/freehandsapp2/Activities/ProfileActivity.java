package freehands.freehandsapp2.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import freehands.freehandsapp2.HttpProvider;
import freehands.freehandsapp2.R;
import freehands.freehandsapp2.StringValues;
import freehands.freehandsapp2.Models.User;

/**
 * Created by jagor on 4/16/2017.
 */

public class ProfileActivity extends AppCompatActivity implements StringValues {

    private FrameLayout updateInfoFrame;
    private ProgressBar updateInfoProgressBar;
    private ImageView personAvatar;
    private EditText personNameInput, personPhoneInput;
    private Button saveBtn, deleteProfileBtn;

    private String personNameSP, personPhoneSP;
    private String authToken;

    String nameUpdate, phoneUpdate;

    private boolean isDelete = false;

//    private InfoUpdatedListener listener;

//    public void setInfoUpdatedListener(InfoUpdatedListener listener){
//        this.listener = listener;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: created");
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //gettin personName and personPhone from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences(PERSON_INFO, MODE_PRIVATE);
        personNameSP = sharedPref.getString(PERSON_NAME, null);
        personPhoneSP = sharedPref.getString(PERSON_PHONE, null);

        //getting authToken
        SharedPreferences sharedPreferencesAuthToken = getSharedPreferences(AUTH, MODE_PRIVATE);
        authToken = sharedPreferencesAuthToken.getString(AUTH_TOKEN, null);

        //hide keyboard onCreate
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        updateInfoFrame = (FrameLayout) findViewById(R.id.update_info_frame);
        updateInfoProgressBar = (ProgressBar) findViewById(R.id.update_info_progress_bar);
        personAvatar = (ImageView) findViewById(R.id.person_avatar);

        personNameInput = (EditText) findViewById(R.id.person_name_input);
        personNameInput.setText(personNameSP);

        personPhoneInput = (EditText) findViewById(R.id.person_phone_input);
        personPhoneInput.setText(personPhoneSP);

        saveBtn = (Button) findViewById(R.id.save_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePersonInfo();
            }
        });

        deleteProfileBtn = (Button) findViewById(R.id.delete_profile_btn);

        deleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfile();
            }
        });

    }

    private void deleteProfile() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Deleting current profile");
        alert.setMessage("Are you sure you want to delete your profile?");

        alert.setPositiveButton("Delete profile", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isDelete = true;
                new UpdateUserInfoAsyncTask().execute();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {

            return super.onOptionsItemSelected(item);
        }
    }

    private void updatePersonInfo() {

        new UpdateUserInfoAsyncTask().execute();

    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
//        startActivity(intent);


        super.onBackPressed();
    }

    private class UpdateUserInfoAsyncTask extends AsyncTask<Void, Void, User> {

        @Override
        protected void onPreExecute() {
            nameUpdate = personNameInput.getText().toString();
            phoneUpdate = personPhoneInput.getText().toString();

            personNameInput.setEnabled(false);
            personPhoneInput.setEnabled(false);
            saveBtn.setEnabled(false);
            updateInfoFrame.setVisibility(View.VISIBLE);
            updateInfoProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected User doInBackground(Void... params) {

            User user = null;
            if (isDelete != true) {
                try {
                    user = HttpProvider.getInstance().updateUserInfo(nameUpdate, phoneUpdate, authToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return user;
            }else{
                try {
                    HttpProvider.getInstance().deleteUser(authToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                super.onPostExecute(user);
                String newPhone = user.getPhone();
                String newName = user.getFirstName();
                SharedPreferences sharedPreferences = getSharedPreferences(PERSON_INFO, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PERSON_NAME, newName);
                editor.putString(PERSON_PHONE, newPhone);
                editor.commit();

                personNameInput.setEnabled(true);
                personPhoneInput.setEnabled(true);
                saveBtn.setEnabled(true);
                updateInfoFrame.setVisibility(View.INVISIBLE);
                updateInfoProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                isDelete = false;

//            listener.infoUpdated();
            }else{
                SharedPreferences sharedPrefPersonInfo = getSharedPreferences(PERSON_INFO, MODE_PRIVATE);
                SharedPreferences.Editor editorPersonInfo = sharedPrefPersonInfo.edit();
                editorPersonInfo.clear();
                editorPersonInfo.commit();

                SharedPreferences sharedPrefAuth = getSharedPreferences(AUTH, MODE_PRIVATE);
                SharedPreferences.Editor editorAuth = sharedPrefAuth.edit();
                editorAuth.clear();
                editorAuth.commit();

                Toast.makeText(ProfileActivity.this, "Your profile was deleted", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                isDelete = false;
                startActivity(intent);
            }
        }
    }

//    public interface InfoUpdatedListener{
//        void infoUpdated();
//    }


}
