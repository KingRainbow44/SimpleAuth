package tech.xigam.simpleauth.auth;

import emu.grasscutter.auth.*;
import emu.grasscutter.auth.DefaultAuthenticators.*;
import emu.grasscutter.database.DatabaseHelper;
import emu.grasscutter.game.Account;
import emu.grasscutter.server.http.objects.*;
import tech.xigam.simpleauth.Cryptography;
import tech.xigam.simpleauth.SimpleAuth;
import tech.xigam.simpleauth.account.SimpleAuthAccount;

import java.nio.charset.StandardCharsets;

/**
 * The authentication system handler for SimpleAuth.
 */
public final class SimpleAuthProvider implements AuthenticationSystem {
    private final Authenticator<LoginResultJson> passwordAuthenticator = new LoginScreenAuthenticator();
    private final Authenticator<LoginResultJson> tokenAuthenticator = new TokenAuthenticator();
    private final Authenticator<ComboTokenResJson> sessionKeyAuthenticator = new SessionKeyAuthenticator();
    private final ExternalAuthenticator externalAuthenticator = new SimpleExternalAuthenticator();

    /**
     * Create an account.
     * @param username The provided username. 
     * @param password The provided password. (SHA-256'ed)
     */
    @Override public void createAccount(String username, String password) {
        // Create/get the/a Grasscutter account from the database.
        var grasscutterAccount = DatabaseHelper.createAccount(username);
        if(grasscutterAccount == null) // The account already exists, fetch it by the username.
            grasscutterAccount = DatabaseHelper.getAccountByName(username);
        
        // Create the SimpleAuth account.
        SimpleAuthAccount.create(grasscutterAccount.getId(), new String(Cryptography.encrypt(
                password.getBytes(StandardCharsets.UTF_8), SimpleAuth.getInstance().getKeyPair().getPublic()))
        );
    }

    /**
     * Reset an account's password.
     * @param username The username of the account to reset.
     */
    @Override public void resetPassword(String username) {
        
    }

    /**
     * Internal-external user identification.
     * @param details A unique, one-time token to verify the user.
     * @return The account from the details provided.
     */
    @Override public Account verifyUser(String details) {
        return null;
    }

    @Override
    public Authenticator<LoginResultJson> getPasswordAuthenticator() {
        return this.passwordAuthenticator;
    }

    @Override
    public Authenticator<LoginResultJson> getTokenAuthenticator() {
        return this.tokenAuthenticator;
    }

    @Override
    public Authenticator<ComboTokenResJson> getSessionKeyAuthenticator() {
        return this.sessionKeyAuthenticator;
    }

    @Override
    public ExternalAuthenticator getExternalAuthenticator() {
        return this.externalAuthenticator;
    }

    @Override
    public OAuthAuthenticator getOAuthAuthenticator() {
        return null;
    }
}
