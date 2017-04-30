package freehands.freehandsapp2.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import freehands.freehandsapp2.Activities.AddGiftActivity;
import freehands.freehandsapp2.Activities.MainActivity;
import freehands.freehandsapp2.R;


public class StartCategoriesForGiftFragment extends Fragment {

    private Button firstCategoryLvlOne, secondCategoryLvlOne, thirdCategoryLvlOne;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_categories_for_gift, container, false);

        firstCategoryLvlOne = (Button) view.findViewById(R.id.furniture_lvl_1);
        secondCategoryLvlOne = (Button) view.findViewById(R.id.electronics_computers_lvl_1);
        thirdCategoryLvlOne = (Button) view.findViewById(R.id.clothing_shoes_jewelry_lvl_1);

        firstCategoryLvlOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGiftAddActivityWithCategory(AddGiftActivity.FURNITURE);
            }
        });

        secondCategoryLvlOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGiftAddActivityWithCategory(AddGiftActivity.ELETRONICS);
            }
        });

        thirdCategoryLvlOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGiftAddActivityWithCategory(AddGiftActivity.CLOTHING);
            }
        });


        return view;
    }

    private void startGiftAddActivityWithCategory(int category){
        Intent intent = new Intent(getActivity(), AddGiftActivity.class);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }


}
