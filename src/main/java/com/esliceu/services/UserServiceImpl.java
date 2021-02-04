package com.esliceu.services;

import com.esliceu.entities.User;
import com.esliceu.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    public User getUserById(Long userid){
        Optional<User> u = userRepo.findById(userid);
        return u.orElse(null);
    }

    public Long checkIfUserIsLogged(String email, String auth) {
        User user = userRepo.findByEmailAndAuth(email, auth);
        if(user != null) {
            return user.getUserid();
        }
        return null;
    }

    public User getUserByEmailAndAuthAndPassword(String email, String auth, String password) {
        return userRepo.findByEmailAndAuthAndPassword(email, auth, password);
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void updateNativeUser(String username, String email, String password, Long userid) throws NoSuchAlgorithmException {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setAuth("NATIVE");
        u.setPassword(encryptPassword(password));
        u.setUserid(userid);
        userRepo.save(u);
    }

    public void updateOAuthUser(String username, Long userid) {
        User u = new User();
        u.setUsername(username);
        u.setUserid(userid);
        userRepo.save(u);
    }

    public Long nativeRegister(String username, String email, String auth, String password1) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setAuth(auth);
        u.setPassword(password1);
        return userRepo.save(u).getUserid();
    }

    public Long oAuth2Register(String email, String auth) {
        User u = new User();
        u.setEmail(email);
        u.setAuth(auth);

        String username = email.split("@")[0];
        int counter = 2;
        while(userRepo.findByUsername(username) != null){
            username = email.split("@")[0] + counter;
            counter++;
        }

        u.setUsername(username);

        return userRepo.save(u).getUserid();
    }

    @Override
    public short checkRegisterCredentials(String username, String email, String password1, String password2) {
        // Return codes:
        // 0: okay.
        // 1: invalid username.
        // 2: invalid email.
        // 3: invalid password.
        // 4: passwords does not match.
        // 5: passwords matches username.

        // Length >= 3; Valid characters: a-z, A-Z, 0-9, points, dashes and underscores.
        String userPattern = "^[a-zA-Z0-9._-]{3,}$";
        boolean userValid = username.matches(userPattern);

        // no whitespace allowed in the entire string and at least eight chars
        String passPattern = "(?=\\S+$).{8,}";
        boolean passValid = password1.matches(passPattern);

        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        boolean emailValid = email.matches(emailPattern);

        if(!userValid) return 1;
        if(!emailValid) return 2;
        if(!passValid) return 3;
        if(!password1.equals(password2)) return 4;
        if(password1.equals(username)) return 5;

        return 0;
    }

    private String encryptPassword(String pass) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        int iterations = 100000;
        int keyLength = 64 * 8;
        char[] passwordChars = pass.toCharArray();

        return hashPassword(passwordChars, salt, iterations, keyLength);
    }

    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = 100000;
        byte[] salt = fromHex(parts[0]);
        byte[] hash = fromHex(parts[1]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static String hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength ) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return toHex(salt) + ":" + toHex(hash);
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
