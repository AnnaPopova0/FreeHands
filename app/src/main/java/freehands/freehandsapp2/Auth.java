package freehands.freehandsapp2;

import android.util.Base64;

/**
 * Created by jagor on 4/8/2017.
 */

public class Auth {

    private String email;
    private String password;



    public Auth(String email, String password) {
        this.email = email;
        this.password = password;
    }





    public Auth() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBasicAuth(String email, String password){
        String credentials = email + ":" + password;
        String basicAuth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        return basicAuth;
    }
}
