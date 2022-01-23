package tech.xigam.simpleauth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import emu.grasscutter.auth.DefaultAuthentication;
import emu.grasscutter.plugin.Plugin;
import tech.xigam.simpleauth.auth.SimpleAuthProvider;
import tech.xigam.simpleauth.objects.InvalidateExpiredSessionsTask;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyPair;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public final class SimpleAuth extends Plugin {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static SimpleAuth instance;

    /**
     * Gets the plugin instance.
     * @return The plugin as a singleton.
     */
    public static SimpleAuth getInstance() {
        return instance;
    }
    
    /* Timer for session invalidator task. */
    private final Timer taskTimer = new Timer();
    
    /* Plugin configuration. */
    private Config configuration;
    /* Encryption keys. */
    private KeyPair keyPair;
    
    @Override
    public void onLoad() {
        // Set instance.
        instance = this;
        
        // Create configuration file.
        var configFile = new File(this.getDataFolder(), "config.json");
        if(!configFile.exists()) {
            try {
                if(!configFile.createNewFile())
                    throw new IOException("Failed to create config file.");
                Files.write(configFile.toPath(), gson.toJson(new Config()).getBytes());
            } catch (IOException ignored) {
                this.getLogger().error("Unable to save configuration file.");
            }
        }
        
        try { // Load configuration file.
            this.configuration = gson.fromJson(new FileReader(configFile), Config.class);
        } catch (IOException ignored) {
            this.getLogger().error("Unable to load configuration file.");
            this.configuration = new Config();
        }
        
        // Check if the encryption key pair exists.
        var keys = new File(this.getDataFolder(), "keys");
        if(!keys.exists()) {
            if(!keys.mkdirs())
                this.getLogger().error("Unable to create keys directory.");
            else {
                var keyPair = Cryptography.generateKeyPair();
                if(keyPair == null)
                    this.getLogger().error("Your system does not support SimpleAuth. (RSA encryption)");
                else Cryptography.saveKeyPair(keyPair, keys.getAbsolutePath());
            }
        }
        
        // Load encryption key.
        this.keyPair = Cryptography.loadKeyPair(keys.getAbsolutePath());
        
        // Log load message.
        this.getLogger().info("SimpleAuth has been loaded.");
    }

    @Override
    public void onEnable() {
        // Set the authentication provider.
        this.getHandle().setAuthSystem(new SimpleAuthProvider());
        
        // Schedule the login session invalidator task.
        // Runs every second, but runs first after 30 seconds.
        this.taskTimer.scheduleAtFixedRate(new InvalidateExpiredSessionsTask(), 30000L, 1000L);
        
        // Log enable message.
        this.getLogger().info("SimpleAuth has been enabled.");
    }

    @Override
    public void onDisable() {
        // Set the authentication provider.
        this.getHandle().setAuthSystem(new DefaultAuthentication());
        
        // Unload key pair. (for security)
        this.keyPair = null;
        
        // Log disable message.
        this.getLogger().info("SimpleAuth has been disabled.");
    }

    /**
     * Gets the plugin configuration.
     * @return Configuration instance.
     */
    public Config getConfig() {
        return this.configuration;
    }

    /**
     * Gets the key pair used for encryption.
     * @return KeyPair instance.
     */
    public KeyPair getKeyPair() {
        return this.keyPair;
    }
}
