package com.github.messenger4j.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * @author Max Grabenhorst
 * @author Andriy Koretskyy
 * @since 0.8.1
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
