package tech.xigam.simpleauth;

import javax.crypto.*;
import java.io.IOException;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;

/**
 * Handles methods related to cryptography.
 */
@SuppressWarnings("UnusedReturnValue")
public final class Cryptography {
    private Cryptography() {
        // Prevents instantiation.
    }

    /**
     * Generates an RSA key pair.
     * @return The generated key pair, or null if the generator cannot be found.
     */
    public static KeyPair generateKeyPair() {
        try {
            // Get the RSA key pair generator.
            var generator = KeyPairGenerator.getInstance("RSA");
            
            generator.initialize(4096); // Set the key size to 4096 bits.
            return generator.generateKeyPair(); // Generate the key pair.
        } catch (NoSuchAlgorithmException ignored) {
            SimpleAuth.getInstance().getLogger().error("Unable to find RSA key pair generator."); return null;
        }
    }

    /**
     * Saves the contents of a key pair to a private and public key file.
     * @param keyPair The key pair to save.
     * @param path The path to save the key to.
     * @return True if the key pair was saved successfully, false otherwise.
     */
    public static boolean saveKeyPair(KeyPair keyPair, String path) {
        // Fetch the actual values of the keys from the key pair.
        var privateKey = keyPair.getPrivate().getEncoded();
        var publicKey = keyPair.getPublic().getEncoded();
        
        try {
            // Write keys to the files.
            Files.write(Paths.get(path, "private-key.pem"), privateKey);
            Files.write(Paths.get(path, "public-key.pem"), publicKey);
            
            return true;
        } catch (IOException exception) {
            SimpleAuth.getInstance().getLogger().error("Unable to save private key."); return false;
        }
    }

    /**
     * Loads a key pair from a private and public key file.
     * @param path The path to load the key from.
     * @return The loaded key pair, or null if the key pair cannot be loaded.
     */
    public static KeyPair loadKeyPair(String path) {
        try {
            // Read the keys from the files.
            var privateKey = Files.readAllBytes(Paths.get(path, "private-key.pem"));
            var publicKey = Files.readAllBytes(Paths.get(path, "public-key.pem"));
            
            // Create the key pair from the keys.
            var keyFactory = KeyFactory.getInstance("RSA");
            var privateKeySpec = new PKCS8EncodedKeySpec(privateKey);
            var publicKeySpec = new X509EncodedKeySpec(publicKey);
            
            return new KeyPair(keyFactory.generatePublic(publicKeySpec), keyFactory.generatePrivate(privateKeySpec));
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException exception) {
            SimpleAuth.getInstance().getLogger().error("Unable to load private key."); return null;
        }
    }

    /**
     * Encrypts the specified bytes using a public key.
     * @param data The data to encrypt.
     * @param key The public key to encrypt with.
     * @return The encrypted data, or the original data if encryption failed.
     */
    public static byte[] encrypt(byte[] data, PublicKey key) {
        try {
            // Encrypt the data.
            var cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException exception) {
            SimpleAuth.getInstance().getLogger().error("Unable to encrypt data."); return data;
        }
    }

    /**
     * Decrypts the specified bytes using a private key.
     * @param data The data to decrypt.
     * @param key The private key to decrypt with.
     * @return The decrypted data, or the original data if decryption failed.
     */
    public static byte[] decrypt(byte[] data, PrivateKey key) {
        try {
            // Decrypt the data.
            var cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException exception) {
            SimpleAuth.getInstance().getLogger().error("Unable to decrypt data."); return data;
        }
    }
}
