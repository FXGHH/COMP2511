package unsw.sso.unsw.sso.pages;

import unsw.sso.Browser;
import unsw.sso.ClientApp;
import unsw.sso.Token;

public class linkOutLoginState implements page{
    private String currentPage = "Hoogle Login";
    private String previousPage = null;
    // @Override
    // public page visit(ClientApp app) {
    //     // TODO Auto-generated method stub
    //     return null;
    // }

    @Override
    public page back() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void interact(ClientApp currentApp, Object using, Browser b) {
        if (using instanceof Token) {
            Token token = (Token) using;
            if (token.getAccessToken() != null) {
                this.previousPage = currentPage;
                this.currentPage = "Home";

                b.setCurToken(token);
                b.setPageState(b.getHomePage());
                b.getCurApp().registerUser((Token)token);
            } else {
                // If accessToken is null, then the user is not authenticated
                // Go back to select providers page
                this.currentPage = "Select a Provider";
            }
        } 
    }
    
}
