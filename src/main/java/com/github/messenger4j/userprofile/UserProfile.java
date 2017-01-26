package com.github.messenger4j.userprofile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andrey on 26.01.17.
 */
public class UserProfile {

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("profile_pic")
    private String profilePic;

    private String locale;

    private String timezone;

    private String gender;

    @SerializedName("is_payment_enabled")
    private boolean isPaymentEnabled;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getLocale() {
        return locale;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getGender() {
        return gender;
    }

    public boolean isPaymentEnabled() {
        return isPaymentEnabled;
    }
}
