package unsw.sso.unsw.sso.pages;

import unsw.sso.Browser;
import unsw.sso.ClientApp;

public class homeState implements page{
    private String currentPage = "home";
    private String previousPage = null;
    @Override
    public void interact(ClientApp currentApp, Object using, Browser b) {
    }

    @Override
    public page back() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
