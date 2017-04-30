package freehands.freehandsapp2;

/**
 * Created by jagor on 4/16/2017.
 */

public class UpdateUserInfo {

    private String firstName;
    private String phone;

    public UpdateUserInfo(String firstName, String phone) {
        this.firstName = firstName;
        this.phone = phone;
    }

    public UpdateUserInfo() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
