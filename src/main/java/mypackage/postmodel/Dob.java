
package mypackage.postmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Dob {

    @SerializedName("date")
    @Expose
    private Date date;
    @SerializedName("age")
    @Expose
    private Integer age;

    public Date getDate() {
        return date;
    }

    public Integer getAge() {
        return age;
    }

}
