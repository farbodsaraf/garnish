package com.mobiquityinc.mobit.steps.restclient.auth.preparer;

import com.mobiquityinc.mobit.modules.generic.httpclient.HttpRequestBuilder;
import com.mobiquityinc.mobit.modules.generic.httpclient.request_preparer.HttpRequestPreparer;
import com.mobiquityinc.mobit.steps.restclient.auth.provider.RestClientAuthenticationProvider;
import lombok.NonNull;

import javax.annotation.Nullable;

public final class AuthenticationHttpRequestPreparer implements HttpRequestPreparer {

    @NonNull private final RestClientAuthenticationProvider authenticationProvider;

    @Nullable private String username;

    public AuthenticationHttpRequestPreparer(@NonNull final RestClientAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public void login(@NonNull final String username) {
        this.username = username;
    }

    public void logout() {
        this.username = null;
    }

    @Override
    public void prepareRequest(@NonNull final HttpRequestBuilder requestBuilder) {
        if (this.username == null) {
            return;
        }

        this.authenticationProvider.authenticateRequest(this.username, requestBuilder);
    }
}
