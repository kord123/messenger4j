package com.github.messenger4j.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * Created by andrey on 26.01.17.
 */
public class FloatSerializer implements JsonSerializer<Float> {
    @Override
    public JsonElement serialize(Float floatValue, Type type, JsonSerializationContext jsonSerializationContext) {
        if (floatValue.isNaN() || floatValue.isInfinite()) {
            return null;
        }
        return new JsonPrimitive(new BigDecimal(floatValue).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
}
