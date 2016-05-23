package at.jku.cis.radar.storage;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenStorage implements Serializable {

    private final Map<String, String> token2Username = new ConcurrentHashMap<>();

    public void storeToken(String token, String username) {
        token2Username.put(token, username);
    }

    public String getUsername(String token) {
        if (!containsToken(token)) {
            return null;
        }
        return token2Username.get(token);
    }

    public boolean containsToken(String token) {
        return token2Username.containsKey(token);
    }
}
