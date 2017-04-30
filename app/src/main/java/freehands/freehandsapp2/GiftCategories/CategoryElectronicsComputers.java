package freehands.freehandsapp2.GiftCategories;

/**
 * Created by jagor on 4/23/2017.
 */

public class CategoryElectronicsComputers {

    private String categoryName = "Electronics & Computers";

    private String tvVideo = "TV & Video";
    private String homeAudioTheater = "Home Audio & Theater";
    private String cameraPhotoVideo = "Camera, Photo & Video";
    private String cellPhonesAccessories = "Cell Phones & Accessories";
    private String computersTablets = "Computers & Tablets";

    public String[] electronicsArr = {tvVideo, homeAudioTheater, cameraPhotoVideo, cellPhonesAccessories, computersTablets};

    public String getTvVideo() {
        return tvVideo;
    }

    public String getHomeAudioTheater() {
        return homeAudioTheater;
    }

    public String getCameraPhotoVideo() {
        return cameraPhotoVideo;
    }

    public String getCellPhonesAccessories() {
        return cellPhonesAccessories;
    }

    public String getComputersTablets() {
        return computersTablets;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
