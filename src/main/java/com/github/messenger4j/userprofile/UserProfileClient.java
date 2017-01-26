package com.github.messenger4j.userprofile;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.setup.CallToAction;
import com.github.messenger4j.setup.SetupResponse;

import java.util.List;

/**
 * Created by andrey on 23.01.17.
 */
public interface UserProfileClient {

    UserProfile getProfile(String userId) throws MessengerApiException, MessengerIOException;
}
