package com.github.messenger4j.common;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

/**
 * @author Andriy Koretskyy
 * @since 0.8.1
 */
public class GsonFactory {

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
                .registerTypeAdapter(Float.class, new FloatSerializer())
                .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
}
