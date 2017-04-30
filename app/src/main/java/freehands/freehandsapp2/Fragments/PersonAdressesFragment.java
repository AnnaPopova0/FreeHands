package freehands.freehandsapp2.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import freehands.freehandsapp2.Activities.AddAddressActivity;
import freehands.freehandsapp2.HttpProvider;
import freehands.freehandsapp2.Models.Address;
import freehands.freehandsapp2.R;
import freehands.freehandsapp2.StringValues;


public class PersonAdressesFragment extends Fragment implements StringValues {


    private FloatingActionButton addAddressBtn;
    private String authToken;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_adresses, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AUTH, Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString(AUTH_TOKEN, null);

        addAddressBtn = (FloatingActionButton) view.findViewById(R.id.add_address_btn);


        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddAddressActivity();
            }
        });

        new GetPersonAddresses().execute();

        return view;
    }


    private void startAddAddressActivity() {

        Intent intent = new Intent(getActivity(), AddAddressActivity.class);
        startActivity(intent);
    }


    private class GetPersonAddresses extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Address[] addresses = HttpProvider.getInstance().getUserAddresses(authToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
