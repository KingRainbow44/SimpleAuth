package tech.xigam.simpleauth.account;

import com.mongodb.client.MongoClients;
import dev.morphia.*;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.query.experimental.filters.Filters;
import tech.xigam.simpleauth.objects.Session;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static emu.grasscutter.Configuration.DATABASE;

/**
 * A provider for SimpleAuth accounts.
 */
public final class AccountProvider {
    private static final Datastore pluginDatastore;
    private static final Map<String, Session> sessions =
            new ConcurrentHashMap<>();
    
    static {
        // Create a Morphia mapper instance & datastore for SimpleAuth.

        // Set mapper options.
        MapperOptions mapperOptions = MapperOptions.builder()
                .storeEmpties(true).storeNulls(false).build();
        
        // Create data store.
        pluginDatastore = Morphia.createDatastore(
                MongoClients.create(DATABASE.server.connectionUri),
                "simpleauth", mapperOptions
        );
        
        // Map classes.
        pluginDatastore.getMapper().map(SimpleAuthAccount.class);
    }
    
    private AccountProvider() {
        // Prevents instantiation.
    }

    /**
     * Saves the specified account to the database.
     * @param account The account to save.
     */
    public static void saveAccount(SimpleAuthAccount account) {
        pluginDatastore.save(account);
    }

    /**
     * Gets an account by its ID.
     * The provided ID should be the same as the one used in the Grasscutter account.
     * @param accountId The ID of the account to get.
     * @return The account with the specified ID, or null if it doesn't exist.
     */
    @Nullable public static SimpleAuthAccount getAccount(String accountId) {
        return pluginDatastore.find(SimpleAuthAccount.class).filter(Filters.eq("_id", accountId)).first();
    }

    /**
     * Gets a login session using a token.
     * @param token The token to get the session for.
     * @return The session with the specified token, or null if it doesn't exist.
     */
    @Nullable public static Session findSessionByToken(String token) {
        return sessions.getOrDefault(token, null);
    }

    /**
     * Gets all valid login sessions.
     * @return A list of sessions.
     */
    public static List<Session> getSessions() {
        return new ArrayList<>(sessions.values());
    }

    /**
     * Invalidates a session.
     * @param session The session to invalidate.
     */
    public static void invalidateSession(Session session) {
        sessions.remove(session.getToken());
    }
}