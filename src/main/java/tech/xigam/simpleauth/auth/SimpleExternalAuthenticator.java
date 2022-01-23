package tech.xigam.simpleauth.auth;

import emu.grasscutter.auth.AuthenticationSystem.AuthenticationRequest;
import emu.grasscutter.auth.ExternalAuthenticator;
import emu.grasscutter.auth.OAuthAuthenticator;

public final class SimpleExternalAuthenticator implements ExternalAuthenticator {
    @Override
    public void handleLogin(AuthenticationRequest request) {
        
    }

    @Override
    public void handleAccountCreation(AuthenticationRequest request) {

    }

    @Override
    public void handlePasswordReset(AuthenticationRequest request) {

    }

    public static class OAuth implements OAuthAuthenticator {
        @Override
        public void handleLogin(AuthenticationRequest authenticationRequest) {
            
        }

        @Override
        public void handleRedirection(AuthenticationRequest authenticationRequest, ClientType clientType) {

        }

        @Override
        public void handleTokenProcess(AuthenticationRequest authenticationRequest) {

        }
    }
}
