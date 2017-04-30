package freehands.freehandsapp2.Models;

import java.util.List;

/**
 * Created by jagor on 4/11/2017.
 */

public class User {

    private String firstName;
    private String email;
    private String[] adresses;
    private String phone;
    private String className;
    private List<String> addresses;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getAdresses() {
        return adresses;
    }

    public void setAdresses(String[] adresses) {
        this.adresses = adresses;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}
