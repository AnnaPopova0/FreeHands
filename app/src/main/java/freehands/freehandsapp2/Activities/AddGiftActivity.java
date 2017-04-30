package freehands.freehandsapp2.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import freehands.freehandsapp2.GiftCategories.GiftCategories;
import freehands.freehandsapp2.R;

/**
 * Created by jagor on 4/17/2017.
 */

public class AddGiftActivity extends AppCompatActivity {

    public static final int FURNITURE = 0;
    public static final int ELETRONICS = 1;
    public static final int CLOTHING = 2;

    private TextView breadcrumbCategory1, breadcrumbCategory2;
    private ImageView addPhoto;
    private EditText titleInput, addressInput, descriptionInput;
    private Spinner categoriesLvl1InputSpinner, categoriesLvl2InputSpinner;
    private Button postGiftBtn;

    private GiftCategories giftCategories = new GiftCategories();

    private int categoryFromFragment;

    private ArrayAdapter<String> dropdownCat2;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Adding gift");

        Intent intent = getIntent();
        categoryFromFragment = (int) intent.getExtras().get("CATEGORY");

        breadcrumbCategory1 = (TextView) findViewById(R.id.breadcrumb_category1);
        breadcrumbCategory2 = (TextView) findViewById(R.id.breadcrumb_category2);

        addPhoto = (ImageView) findViewById(R.id.add_photo);

        titleInput = (EditText) findViewById(R.id.title_input);
        addressInput = (EditText) findViewById(R.id.address_input);
        descriptionInput = (EditText) findViewById(R.id.description_input);

        categoriesLvl1InputSpinner = (Spinner) findViewById(R.id.categories_lvl_1_input_spinner);
        categoriesLvl2InputSpinner = (Spinner) findViewById(R.id.categories_lvl_2_input_spinner);

        ArrayAdapter<String> dropdownCat1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, giftCategories.giftCategoriesLvl1);
        categoriesLvl1InputSpinner.setAdapter(dropdownCat1);

        switch (categoryFromFragment){
            case FURNITURE:
                categoriesLvl1InputSpinner.setSelection(FURNITURE);
                setSpinnerCategoriesLvl2Adapter(FURNITURE);
                break;
            case ELETRONICS:
                categoriesLvl1InputSpinner.setSelection(ELETRONICS);
                setSpinnerCategoriesLvl2Adapter(ELETRONICS);
                break;
            case CLOTHING:
                categoriesLvl1InputSpinner.setSelection(CLOTHING);
                setSpinnerCategoriesLvl2Adapter(CLOTHING);
                break;
        }

        categoriesLvl1InputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selected = categoriesLvl1InputSpinner.getSelectedItemPosition();
                switch (selected){
                    case FURNITURE:
                        setSpinnerCategoriesLvl2Adapter(FURNITURE);
                        break;
                    case ELETRONICS:
                        setSpinnerCategoriesLvl2Adapter(ELETRONICS);
                        break;
                    case CLOTHING:
                        setSpinnerCategoriesLvl2Adapter(CLOTHING);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






    }

    private void setSpinnerCategoriesLvl2Adapter(int i){
        dropdownCat2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, giftCategories.getCategoriesLvl2(i));
        categoriesLvl2InputSpinner.setAdapter(dropdownCat2);
        breadcrumbCategory1.setText(categoriesLvl1InputSpinner.getSelectedItem().toString());
    }




}
