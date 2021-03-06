package com.github.messenger4j.test.integration.setup;

import static com.github.messenger4j.common.MessengerHttpClient.HttpMethod.DELETE;
import static com.github.messenger4j.common.MessengerHttpClient.HttpMethod.POST;
import static com.github.messenger4j.setup.CallToActionType.POSTBACK;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.common.MessengerHttpClient;
import com.github.messenger4j.common.MessengerHttpClient.HttpMethod;
import com.github.messenger4j.common.MessengerHttpClient.HttpResponse;
import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.setup.CallToAction;
import com.github.messenger4j.setup.MessengerSetupClient;
import com.github.messenger4j.setup.SetupResponse;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Andriy Koretskyy
 * @since 0.8.0
 */
public class MessengerSetupClientTest {

    private static final String PAGE_ACCESS_TOKEN = "PAGE_ACCESS_TOKEN";

    private MessengerSetupClient messengerSetupClient;
    private MessengerHttpClient mockHttpClient = mock(MessengerHttpClient.class);

    private final HttpResponse fakeResponse = new HttpResponse(200,
            "{\"result\": \"Successfully added new_thread's CTAs\"}");

    @Before
    public void beforeEach() throws Exception {
        when(mockHttpClient.execute(any(HttpMethod.class), anyString(), anyString())).thenReturn(fakeResponse);
        messengerSetupClient = MessengerPlatform.newSetupClientBuilder(PAGE_ACCESS_TOKEN)
                .httpClient(mockHttpClient)
                .build();
    }

    @Test
    public void shouldSetupStartButton() throws Exception {
        //given
        final String payload = "Button pressed";

        //when
        messengerSetupClient.setupStartButton(payload);

        //then
        final String expectedJsonBody =
                "{\"setting_type\":\"call_to_actions\",\"thread_state\":\"new_thread\",\"call_to_actions\":[" +
                        "{\"payload\":\"Button pressed\"}]}";
        verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), eq(expectedJsonBody));
    }

    @Test
    public void shouldRemoveStartButton() throws Exception {
        //when
        messengerSetupClient.removeStartButton();

        //then
        final String expectedJsonBody =
                "{\"setting_type\":\"call_to_actions\",\"thread_state\":\"new_thread\"}";
        verify(mockHttpClient).execute(eq(DELETE), endsWith(PAGE_ACCESS_TOKEN), eq(expectedJsonBody));
    }

    @Test
    public void shouldSetupWelcomeMessage() throws Exception {
        //given
        final String greeting = "Hi, we welcome you at our page";

        //when
        messengerSetupClient.setupWelcomeMessage(greeting);

        //then
        final String expectedJsonBody =
                "{\"setting_type\":\"greeting\",\"greeting\":{\"text\":\"Hi, we welcome you at our page\"}}";
        verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), eq(expectedJsonBody));
    }

    @Test
    public void shouldResetWelcomeMessage() throws Exception {
        //when
        messengerSetupClient.resetWelcomeMessage();

        //then
        final String expectedJsonBody = "{\"setting_type\":\"greeting\"}";
        verify(mockHttpClient).execute(eq(DELETE), endsWith(PAGE_ACCESS_TOKEN), eq(expectedJsonBody));
    }

    @Test
    public void shouldSetupPersistentMenu() throws Exception {
        //given
        final CallToAction menuItem = CallToAction.newBuilder()
                .title("Leave feedback")
                .type(POSTBACK)
                .payload("any")
                .build();

        //when
        messengerSetupClient.setupPersistentMenu(singletonList(menuItem));

        //then
        final String expectedJsonBody =
                "{\"setting_type\":\"call_to_actions\",\"thread_state\":\"existing_thread\",\"call_to_actions\":" +
                        "[{\"type\":\"postback\",\"title\":\"Leave feedback\",\"payload\":\"any\"}]}";
        verify(mockHttpClient).execute(eq(POST), endsWith(PAGE_ACCESS_TOKEN), eq(expectedJsonBody));
    }

    @Test
    public void shouldRemovePersistentMenu() throws Exception {
        //when
        messengerSetupClient.removePersistentMenu();

        //then
        final String expectedJsonBody =
                "{\"setting_type\":\"call_to_actions\",\"thread_state\":\"existing_thread\"}";
        verify(mockHttpClient).execute(eq(DELETE), endsWith(PAGE_ACCESS_TOKEN), eq(expectedJsonBody));
    }

    @Test
    public void shouldHandleSuccessResponse() throws Exception {
        //given
        final HttpResponse successfulResponse = new HttpResponse(200,
                "{\"result\": \"Successfully added new_thread's CTAs\"}");
        when(mockHttpClient.execute(any(HttpMethod.class), anyString(), anyString())).thenReturn(successfulResponse);

        //when
        final SetupResponse setupResponse = messengerSetupClient.setupStartButton("button pressed");

        //then
        assertThat(setupResponse, is(notNullValue()));
        assertThat(setupResponse.getResult(), is(equalTo("Successfully added new_thread's CTAs")));
    }

    @Test
    public void shouldHandleErrorResponse() throws Exception {
        //given
        final HttpResponse errorResponse = new HttpResponse(401, "{\n" +
                "  \"error\": {\n" +
                "    \"message\": \"Invalid OAuth access token.\",\n" +
                "    \"type\": \"OAuthException\",\n" +
                "    \"code\": 190,\n" +
                "    \"fbtrace_id\": \"BLBz/WZt8dN\"\n" +
                "  }\n" +
                "}");
        when(mockHttpClient.execute(any(HttpMethod.class), anyString(), anyString())).thenReturn(errorResponse);

        //when
        MessengerApiException messengerApiException = null;
        try {
            messengerSetupClient.setupStartButton("text");
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