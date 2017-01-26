package com.github.messenger4j.setup;

import java.util.Objects;

/**
 * @author Max Grabenhorst
 * @since 0.6.0
 */
public final class SetupResponse {

    private String result;

    public String getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetupResponse that = (SetupResponse) o;
        return Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result);
    }

    @Override
    public String toString() {
        return "SetupResponse{" +
                "result = '" + result + '\'' +
                '}';
    }
}