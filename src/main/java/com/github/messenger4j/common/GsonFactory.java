package com.github.messenger4j.common;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by andrey on 26.01.17.
 */
public class GsonFactory {

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Float.class, new FloatSerializer())
                .registerTypeAdapter(MessengerApiException.class, new MessengerApiExceptionDeserializer())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
}
