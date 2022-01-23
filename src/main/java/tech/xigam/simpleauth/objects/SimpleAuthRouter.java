package tech.xigam.simpleauth.objects;

import emu.grasscutter.server.http.Router;
import express.Express;
import express.http.Request;
import express.http.Response;
import io.javalin.Javalin;

@SuppressWarnings("JavadocReference")
public final class SimpleAuthRouter implements Router {
    @Override public void applyRoutes(Express express, Javalin handle) {
        express.get("/token", SimpleAuthRouter::token);
    }

    /**
     * Generate a user's token from a username & password combo.
     * @param username The username of the account.
     * @param password The password of the account.
     */
    private static void token(Request request, Response response) {
        var username = request.query("username");
        var password = request.query("password");
    }
}
