package unsw.sso.providers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import unsw.sso.Token;
import unsw.sso.unsw.sso.provider;

public class LinkedOut implements provider{
    private Map<String, String> userMappings = new HashMap<>();

    public void addUser(String email, String password) {
        userMappings.put(email, password);
    }

    public Token generateFormSubmission(String email, String password) {
        if (Objects.equals(userMappings.get(email), password) || password.length() == 0) {
            return new Token(UUID.randomUUID().toString(), email, getClass().getSimpleName());
        } else {
            return new Token(null, email, getClass().getSimpleName());
        }
    }

    @Override
    public void registerProvider(List<String> providers) {
        if (!providers.contains("LinkedOut")) {
            providers.add("LinkedOut");
        }
    }

    @Override
    public String getProviderName() {
        return "LinkedOut";
    }

}
