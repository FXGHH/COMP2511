package unsw.sso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.sso.providers.Hoogle;
import unsw.sso.providers.LinkedOut;
import unsw.sso.unsw.sso.provider;

public class ClientApp {

    private Map<String, String> usersExist = new HashMap<>();
    private List<String> providers = new ArrayList<>();
    private final String name;

    public ClientApp(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // TODO: you'll probably want to change a lot of this class
    public void registerProvider(Object o) {
        provider p = (provider)o;
        p.registerProvider(providers);
    }

    public boolean userCheck(String email) {
        return usersExist.containsKey(email);
    }

    public boolean hasHoogle() {
        return providers.contains("Hoogle");
    }

    public boolean hasLinkedOut() {
        return providers.contains("LinkedOut");
    }

    public void registerUser(Token token) {
        // only hoogle is supported right now!  So we presume hoogle on user
        usersExist.put(token.getUserEmail(), token.getProviderName());
    }

    public boolean hasUserForProvider(String email, Object providerObj) {
        // if (provider instanceof LinkedOut) {
        //     return true;
        // }
        
        // return provider instanceof Hoogle && this.hasHoogle() && this.usersExist.getOrDefault(email, false);
        provider p = (provider)providerObj;
        return usersExist.get(email).equals(p.getProviderName());
        
    }

    public boolean hasHoogleUser(String email) {
        return usersExist.get(email).equals("Hoogle");
    }

}
