package com.github.messenger4j.common;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.common.http.MessengerHttpClient;
import com.google.gson.*;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by andrey on 25.01.17.
 */
public abstract class MessengerSendClientAbstract<P, R> {

    private final Class<R> rClass;
    private final Gson gson;
    private final String requestUrl;
    private final MessengerHttpClient httpClient;

    protected MessengerSendClientAbstract(Class<R> rClass, String requestUrl, MessengerHttpClient httpClient) {
        this.rClass = rClass;
        this.gson = GsonFactory.getGson();
        this.requestUrl = requestUrl;
        this.httpClient = httpClient;
    }

    protected R sendPayload(P payload, MessengerHttpClient.Method method) throws MessengerApiException, MessengerIOException {

        try {
            final String jsonBody = this.gson.toJson(payload);
            final MessengerHttpClient.Response response = this.httpClient.execute(this.requestUrl, jsonBody, method);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                return gson.fromJson(response.getBody(), rClass);
            } else {
                throw gson.fromJson(response.getBody(), MessengerApiException.class);
            }
        } catch (IOException e) {
            throw new MessengerIOException(e);
        }
    }
}
