package com.github.messenger4j.userprofile;

import com.github.messenger4j.common.GsonFactory;
import com.github.messenger4j.common.http.MessengerHttpClient;
import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.DefaultMessengerHttpClient;
import com.github.messenger4j.setup.MessengerSetupClientImpl;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.github.messenger4j.common.http.MessengerHttpClient.Method.GET;

/**
 * Created by andrey on 26.01.17.
 */
public class UserProfileClientImpl implements UserProfileClient {

    private static final String FB_GRAPH_API_URL =
            "https://graph.facebook.com/v2.8/%s" +
                    "?fields=first_name,last_name,profile_pic,locale,timezone,gender" +
                    "&access_token=%s";

    private final Logger logger = LoggerFactory.getLogger(MessengerSetupClientImpl.class);

    private final Gson gson;
    private final MessengerHttpClient httpClient;
    private final String pageAccessToken;


    public UserProfileClientImpl(UserProfileClientBuilder builder) {
        this.gson = GsonFactory.getGson();
        this.pageAccessToken = builder.pageAccessToken;
        this.httpClient = builder.httpClient == null ? new DefaultMessengerHttpClient() : builder.httpClient;

        logger.debug("{} initialized successfully.", MessengerSetupClientImpl.class.getSimpleName());
    }

    @Override
    public UserProfile getProfile(String userId) throws MessengerApiException, MessengerIOException {
        return sendGet(userId);
    }

    protected UserProfile sendGet(String userid) throws MessengerApiException, MessengerIOException {

        try {
            String requestUrl = String.format(FB_GRAPH_API_URL, userid, pageAccessToken);
            final MessengerHttpClient.Response response = this.httpClient.execute(requestUrl, null, GET);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                return gson.fromJson(response.getBody(), UserProfile.class);
            } else {
                throw gson.fromJson(response.getBody(), MessengerApiException.class);
            }
        } catch (IOException e) {
            throw new MessengerIOException(e);
        }
    }
}
