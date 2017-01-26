package com.github.messenger4j.userprofile;

import com.github.messenger4j.internal.PreConditions;
import com.github.messenger4j.common.http.MessengerHttpClient;

/**
 * @author Max Grabenhorst
 * @since 0.6.0
 */
public final class UserProfileClientBuilder {

    final String pageAccessToken;
    MessengerHttpClient httpClient;

    public UserProfileClientBuilder(String pageAccessToken) {
        PreConditions.notNullOrBlank(pageAccessToken, "pageAccessToken");
        this.pageAccessToken = pageAccessToken;
    }

    public UserProfileClientBuilder httpClient(MessengerHttpClient messengerHttpClient) {
        this.httpClient = messengerHttpClient;
        return this;
    }

    public UserProfileClient build() {
        return new UserProfileClientImpl(this);
    }
}