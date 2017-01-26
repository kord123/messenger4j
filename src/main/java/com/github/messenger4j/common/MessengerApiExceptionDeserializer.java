package com.github.messenger4j.common;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by andrey on 26.01.17.
 */
public class MessengerApiExceptionDeserializer implements JsonDeserializer<MessengerApiException>{
    @Override
    public MessengerApiException deserialize(JsonElement jsonElement,
                                             Type type,
                                             JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return MessengerApiException.fromJson(jsonElement.getAsJsonObject());
    }
}
