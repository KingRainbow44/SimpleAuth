package tech.xigam.simpleauth.auth;

import emu.grasscutter.auth.AuthenticationSystem.AuthenticationRequest;
import emu.grasscutter.auth.Authenticator;
import emu.grasscutter.server.http.objects.LoginResultJson;
import tech.xigam.simpleauth.account.AccountProvider;
import tech.xigam.simpleauth.objects.absolute.ResponseCodes;

/**
 * Handles authentication via the game's login screen.
 */
public final class LoginScreenAuthenticator implements Authenticator<LoginResultJson> {
    @Override
    public LoginResultJson authenticate(AuthenticationRequest input) {
        // Get request data.
        var request = input.getPasswordRequest();
        assert request != null; // Should never be null.
        
        // Get the input username.
        // The username should be an OTP (6-digit token).
        var username = request.account;
        // Create response object.
        var response = new LoginResultJson();
        
        // Get the login session by token.
        var session = AccountProvider.findSessionByToken(username);
        // Check if the session is valid.
        if (session == null) {
            // Invalid session.
            response.message = "Could not find session with token: " + username;
            response.retcode = ResponseCodes.INVALID_ACCOUNT;
        } else {
            // Valid session.
            response.message = "OK";
            response.data = AuthenticationUtil.generateLoginData(session.getAccount());
            
            // Invalidate the session.
            AccountProvider.invalidateSession(session);
        }
        
        return response;
    }
}
