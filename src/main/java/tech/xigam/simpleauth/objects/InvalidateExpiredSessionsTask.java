package tech.xigam.simpleauth.objects;

import tech.xigam.simpleauth.account.AccountProvider;

import java.util.TimerTask;

/**
 * Invalidates expired login sessions.
 */
public final class InvalidateExpiredSessionsTask extends TimerTask {
    @Override public void run() {
        var loginSessions = AccountProvider.getSessions(); // Get all login sessions.
        if(loginSessions.size() <= 0) return; // Check if there are any sessions.
        
        for(var loginSession : loginSessions) // Get all login sessions.
            if(loginSession.hasExpired()) // Check if the session has expired.
                AccountProvider.invalidateSession(loginSession); // Invalidate the session.
    }
}
