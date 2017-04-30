package freehands.freehandsapp2.GiftCategories;

import java.util.ArrayList;

/**
 * Created by jagor on 4/23/2017.
 */

public class GiftCategories {

    private CategoryFurniture categoryFurniture = new CategoryFurniture();
    private CategoryElectronicsComputers categoryElectronicsComputers = new CategoryElectronicsComputers();
    private CategoryClothingShoesJewelry categoryClothingShoesJewelry = new CategoryClothingShoesJewelry();

    public String[] giftCategoriesLvl1 = {categoryFurniture.getCategoryName(), categoryElectronicsComputers.getCategoryName(), categoryClothingShoesJewelry.getCategoryName()};
    public String[][] giftCategoriesLvl2 = new String[][]{categoryFurniture.furnitureArr, categoryElectronicsComputers.electronicsArr, categoryClothingShoesJewelry.clothingArr};

    public String[] getCategoriesLvl2(int position){
        String[] arrayList = giftCategoriesLvl2[position];
        return arrayList;
    }
}
