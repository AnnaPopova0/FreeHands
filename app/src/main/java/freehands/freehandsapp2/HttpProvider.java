package freehands.freehandsapp2;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import freehands.freehandsapp2.Models.Address;
import freehands.freehandsapp2.Models.Gift;
import freehands.freehandsapp2.Models.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.net.HttpURLConnection.HTTP_BAD_METHOD;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by jagor on 4/8/2017.
 */

public class HttpProvider {
    private static final HttpProvider ourInstance = new HttpProvider();

    public static HttpProvider getInstance() {
        return ourInstance;
    }

    private HttpProvider() {
    }

    public static final String TAG = "MY_LOG";

    public static final String URL = "https://freehands1337.herokuapp.com/";

    public static final String MEDIA_TYPE_HEADER = "application/json; charset=utf-8";

    public String registration(String email, String password) throws Exception {
        Gson gson = new Gson();
        Auth auth = new Auth(email, password);
        String authToken = null;

        String json = gson.toJson(auth);

        MediaType mediaType = MediaType.parse(MEDIA_TYPE_HEADER);

        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(URL + "freehands/registration")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();

        System.out.println("hui");
        Response response = client.newCall(request).execute();
        System.out.println("response code");
        Log.d(TAG, "response body" + response.body().string());
        if (response.code() == HTTP_FORBIDDEN) {
            new Exception("User with these credentials already exists");
        } else if (response.code() == HTTP_OK) {
            Log.d("MY_LOG", "Response code ok");
            authToken = auth.getBasicAuth(email, password);
            Log.d(TAG, "authToken is " + authToken);
        }
        return authToken;

    }

    public Response login(String authToken) throws Exception {


        Request request = new Request.Builder()
                .url(URL + "login")
                .addHeader("Authorization", authToken)
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(request).execute();
        Log.d(TAG, "login: response code is " + response.code());


        return response;
    }

    public User getUserInfo(String authToken) throws Exception {
        String json = null;
        Gson gson = new Gson();


        Request request = new Request.Builder()
                .url(URL + "freehands/fulluserinfo")
                .addHeader("Authorization", authToken)
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(request).execute();

        if (response.code() == HTTP_BAD_REQUEST){
            throw new Exception("Please log in");
        }else if(response.code() == HTTP_OK){
            json = response.body().string();
        }

        User user = gson.fromJson(json, User.class);

        return user;
    }

    public User updateUserInfo(String name, String phone, String authToken) throws Exception {
        String responseJson = null;
        UpdateUserInfo updateUserInfo = new UpdateUserInfo(name, phone);
        String json = new Gson().toJson(updateUserInfo);

        MediaType mediaType = MediaType.parse(MEDIA_TYPE_HEADER);

        RequestBody requestBody = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(URL + "freehands/fulluserinfo")
                .addHeader("Authorization", authToken)
                .put(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(request).execute();

        if(response.code() == HTTP_NOT_FOUND){
            throw new Exception("User is not logged");
        }else if(response.code() == HTTP_OK){
            responseJson = response.body().string();
        }

        User user = new Gson().fromJson(responseJson, User.class);

        return user;

    }

    public void deleteUser(String authToken) throws Exception {
        Request request = new Request.Builder()
                .url(URL + "freehands/deleteuser")
                .addHeader("Authorization", authToken)
                .delete()
                .build();

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(request).execute();

        if(response.code() == HTTP_NOT_FOUND){
            throw new Exception("User is not logged");
        }else if(response.code() == HTTP_OK){

        }
    }

    public void addGift(String authToken){
        Gift gift = new Gift();
    }

    public void addAddress(String authToken, String json) throws Exception {
        Gson gson = new Gson();

        MediaType mediaType = MediaType.parse(MEDIA_TYPE_HEADER);
        RequestBody requestBody = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(URL + "freehands/newaddress")
                .addHeader("Authorization", authToken)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(request).execute();

        if(response.code() == HTTP_NOT_FOUND){
            throw new Exception("Some fields are not filled properly");
        }else if(response.code() == HTTP_FORBIDDEN){
            throw new Exception("PLease log in");
        }else if(response.code() == HTTP_OK){

        }

        String jsonResponse = response.body().string();



    }

    public Address[] getUserAddresses(String authToken) throws Exception {
        String json = null;
        Gson gson = new Gson();
        Request request = new Request.Builder()
                .url(URL + "freehands/getalladdresses")
                .addHeader("Authorization", authToken)
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(request).execute();

        if (response.code() == HTTP_BAD_REQUEST){
            throw new Exception("Please log in");
        }else if(response.code() == HTTP_OK){
            json = response.body().string();
        }

        Address[] addresses = gson.fromJson(json, Address[].class);

        return addresses;
    }

}

