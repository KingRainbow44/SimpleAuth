package tech.xigam.simpleauth.auth;

import emu.grasscutter.game.Account;
import emu.grasscutter.server.http.objects.LoginResultJson.*;

/**
 * Utilities to help with authentication.
 */
public final class AuthenticationUtil {
    private AuthenticationUtil() {
        // Prevents instantiation.
    }
    
    public static VerifyData generateLoginData(Account account) {
        // Generate a new account data object.
        var accountData = new VerifyAccountData();
        accountData.uid = account.getId();
        accountData.token = account.generateSessionKey();
        accountData.email = account.getEmail();
        
        // Generate a new login data object.
        var data = new VerifyData();
        data.account = accountData;
        
        return data; // Return the final login data.
    }
}
