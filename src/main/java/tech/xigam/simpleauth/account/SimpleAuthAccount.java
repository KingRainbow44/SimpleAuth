package tech.xigam.simpleauth.account;

import dev.morphia.annotations.*;
import lombok.*;
import lombok.experimental.*;

import javax.annotation.Nullable;

@Accessors(chain = true)
@Entity(value = "simpleauth", useDiscriminator = false)
public final class SimpleAuthAccount {
    @Id @Getter @Setter
    private String id;

    @Getter @Setter
    @AlsoLoad("password")
    private String password;

    /**
     * Saves the account, and it's information to the database.
     */
    public SimpleAuthAccount save() {
        AccountProvider.saveAccount(this); return this;
    }

    /**
     * GEts the account by its ID.
     * @param id The ID of the account.
     * @return The account.
     */
    @Nullable
    public static SimpleAuthAccount find(String id) {
        return AccountProvider.getAccount(id);
    }

    /**
     * Creates a SimpleAuth account, then saves it to the database.
     * @param accountId The ID of the account. This should match a Grasscutter account.
     * @param password The password of the account. This should an RSA-encrypted hash of the password.
     * @return The newly created account.
     */
    public static SimpleAuthAccount create(String accountId, String password) {
        return new SimpleAuthAccount()
                .setId(accountId)
                .setPassword(password)
                .save();
    }
}