package com.github.messenger4j.test.integration.userprofile;

import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.common.http.MessengerHttpClient;
import com.github.messenger4j.common.http.MessengerHttpClient.Method;
import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.setup.CallToAction;
import com.github.messenger4j.setup.MessengerSetupClient;
import com.github.messenger4j.setup.SetupResponse;
import com.github.messenger4j.userprofile.UserProfile;
import com.github.messenger4j.userprofile.UserProfileClient;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;

import static com.github.messenger4j.common.http.MessengerHttpClient.Method.DELETE;
import static com.github.messenger4j.common.http.MessengerHttpClient.Method.GET;
import static com.github.messenger4j.common.http.MessengerHttpClient.Method.POST;
import static com.github.messenger4j.setup.Type.POSTBACK;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Andriy Koretskyy
 * @since 0.7.2
 */
public class UserProfileClientTest {

    private static final String PAGE_ACCESS_TOKEN = "PAGE_ACCESS_TOKEN";

    private UserProfileClient userProfileClient;
    private MessengerHttpClient mockHttpClient = mock(MessengerHttpClient.class);

    private final MessengerHttpClient.Response fakeResponse = new MessengerHttpClient.Response(200,
            "{\"result\": \"Successfully added new_thread's CTAs\"}");

    @Before
    public void beforeEach() throws Exception {
        when(mockHttpClient.execute(anyString(), ArgumentMatchers.<String>isNull(), ArgumentMatchers.any(Method.class))).
                thenReturn(fakeResponse);
        userProfileClient = MessengerPlatform.newUserProfileClientBuilder(PAGE_ACCESS_TOKEN)
                .httpClient(mockHttpClient)
                .build();
    }

    @Test
    public void shouldGetUserProfile() throws Exception {
        //given
        final String userId = "Some_id";

        //when
        userProfileClient.getProfile(userId);

        //then
        verify(mockHttpClient).execute(endsWith(PAGE_ACCESS_TOKEN), ArgumentMatchers.<String>isNull(), eq(GET));
    }

    @Test
    public void shouldHandleSuccessResponse() throws Exception {
        //given
        final MessengerHttpClient.Response successfulResponse = new MessengerHttpClient.Response(200,
                "{\"first_name\": \"Peter\"," +
                        "\"last_name\": \"Chang\"," +
                        "\"profile_pic\": \"https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpf1/v/t1.0-1/p200x200/13055603_10105219398495383_8237637584159975445_n.jpg?oh=1d241d4b6d4dac50eaf9bb73288ea192&oe=57AF5C03&__gda__=1470213755_ab17c8c8e3a0a447fed3f272fa2179ce\"," +
                        "\"locale\": \"en_US\"," +
                        "\"timezone\": -7," +
                        "\"gender\": \"male\"" +
                        "}    ");
        when(mockHttpClient.execute(anyString(), ArgumentMatchers.<String>isNull(), ArgumentMatchers.any(Method.class))).thenReturn(successfulResponse);

        //when
        final UserProfile userProfile = userProfileClient.getProfile("some_id");

        //then
        assertThat(userProfile, is(notNullValue()));
        assertThat(userProfile.getFirstName(), is(equalTo("Peter")));
    }

    @Test
    public void shouldHandleErrorResponse() throws Exception {
        //given
        final MessengerHttpClient.Response errorResponse = new MessengerHttpClient.Response(401, "{\n" +
                "  \"error\": {\n" +
                "    \"message\": \"Invalid OAuth access token.\",\n" +
                "    \"type\": \"OAuthException\",\n" +
                "    \"code\": 190,\n" +
                "    \"fbtrace_id\": \"BLBz/WZt8dN\"\n" +
                "  }\n" +
                "}");
        when(mockHttpClient.execute(anyString(), ArgumentMatchers.<String>isNull(), ArgumentMatchers.any(Method.class))).thenReturn(errorResponse);

        //when
        MessengerApiException messengerApiException = null;
        try {
            userProfileClient.getProfile("some_id");
        } catch (MessengerApiException e) {
            messengerApiException = e;
        }

        //then
        assertThat(messengerApiException, is(notNullValue()));
        assertThat(messengerApiException.getMessage(), is(equalTo("Invalid OAuth access token.")));
        assertThat(messengerApiException.getType(), is(equalTo("OAuthException")));
        assertThat(messengerApiException.getCode(), is(equalTo(190)));
        assertThat(messengerApiException.getFbTraceId(), is(equalTo("BLBz/WZt8dN")));
    }
}