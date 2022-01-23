package tech.xigam.simpleauth.objects;

import emu.grasscutter.game.Account;
import lombok.Data;
import tech.xigam.simpleauth.account.SimpleAuthAccount;

import java.time.OffsetDateTime;

/**
 * Temporary object for holding login session data.
 */
@Data(staticConstructor = "of")
public final class Session {
    private Account account;
    private SimpleAuthAccount authData;
    private OffsetDateTime expirationTime;
    private String token;

    /**
     * Checks if this session has expired.
     * @return true if expired, false otherwise
     */
    public boolean hasExpired() {
        return OffsetDateTime.now().isAfter(this.expirationTime);
    }
}
