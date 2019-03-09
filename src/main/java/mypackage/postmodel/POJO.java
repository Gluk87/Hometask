
package mypackage.postmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POJO {

    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("dob")
    @Expose
    private Dob dob;
    @SerializedName("id")
    @Expose
    private Id id;
    @SerializedName("nat")
    @Expose
    private String nat;

    public String getGender() {
        return gender;
    }

    public Name getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Dob getDob() {
        return dob;
    }

    public Id getId() {
        return id;
    }

    public String getNat() {
        return nat;
    }

}
