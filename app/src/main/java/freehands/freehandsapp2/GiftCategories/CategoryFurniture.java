package freehands.freehandsapp2.GiftCategories;

import freehands.freehandsapp2.StringValues;

/**
 * Created by jagor on 4/23/2017.
 */

public class CategoryFurniture {

    private String categoryName = "Furniture";

    public String getCategoryName() {
        return categoryName;
    }

    private String bedroomFurniture = "Bedroom furniture";
    private String livingRoomFurniture = "Living room furniture";
    private String kitchenDiningRoomFurniture = "Kitchen dining room furniture";
    private String homeOfficeFurniture = "Home office furniture";
    private String kidsFurniture = "Kids furniture";

    public String[] furnitureArr = {bedroomFurniture, livingRoomFurniture, kitchenDiningRoomFurniture, homeOfficeFurniture, kidsFurniture};

    public String getBedroomFurniture() {
        return bedroomFurniture;
    }

    public String getLivingRoomFurniture() {
        return livingRoomFurniture;
    }

    public String getKitchenDiningRoomFurniture() {
        return kitchenDiningRoomFurniture;
    }

    public String getHomeOfficeFurniture() {
        return homeOfficeFurniture;
    }

    public String getKidsFurniture() {
        return kidsFurniture;
    }
}
