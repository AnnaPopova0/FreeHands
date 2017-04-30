package freehands.freehandsapp2.GiftCategories;

import freehands.freehandsapp2.StringValues;

/**
 * Created by jagor on 4/23/2017.
 */

public class CategoryClothingShoesJewelry {



    private String categoryName = "Clothing, Shoes & Jewelery";

    private String women = "Women";
    private String men = "Men";
    private String kids = "Kids";

    public String[] clothingArr = {women, men, kids};

    public String getWomen() {
        return women;
    }

    public String getMen() {
        return men;
    }

    public String getKids() {
        return kids;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
