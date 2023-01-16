package unsw.sso;

import java.util.ArrayList;
import java.util.List;

import unsw.sso.providers.Hoogle;
import unsw.sso.providers.LinkedOut;
import unsw.sso.unsw.sso.provider;
import unsw.sso.unsw.sso.pages.SelProviderState;
import unsw.sso.unsw.sso.pages.homeState;
import unsw.sso.unsw.sso.pages.hoogleLoginState;
import unsw.sso.unsw.sso.pages.linkOutLoginState;
import unsw.sso.unsw.sso.pages.page;

public class Browser {
    private Token currentToken = null;
    private String currentPage = null;
    private String previousPage = null;

    private page curPage = new SelProviderState();
    private page hLogPage = new hoogleLoginState();
    private page linLogPage = new linkOutLoginState();
    private page homePage = new homeState();

    private ClientApp currentApp = null;
    private List<Token> cache = new ArrayList<>();

    public void visit(ClientApp app) {
        currentToken = null;
        
        this.previousPage = null;
        this.currentPage = currPageStatus(app);
        this.currentApp = app;
    }

    public String getCurrentPageName() {
        return this.currentPage;
    }

    public void setPageState(page p) {
        curPage = p;
    }

    public Token a() {
        return currentToken;
    }
    public String currPageStatus(ClientApp app) {
        
        if (cache.size() != 0 && checkCacheUser(app)) {
            return "Home";
        } 
        return "Select a Provider";
    }

    public void clearCache() {
        cache.clear();
    }

    public boolean checkCacheUser(ClientApp app) {
        for(Token t : cache) {
            if (app.userCheck(t.getUserEmail())) {
                return true;
            }
        }
        return false;
    }

    public void interact(Object using) {
        if (using == null) {
            this.currentPage = this.previousPage;
            return;
        }

        curPage.interact(currentApp, using, this);

        switch (currentPage) {
            case "Select a Provider": {
                // if the currentApp doesn't have hoogle
                // then it has no providers, which just will prevent
                // transition.
                if (using instanceof Hoogle && currentApp.hasHoogle()) {
                    this.previousPage = currentPage;
                    this.currentPage = "Hoogle Login";
                } else if (using instanceof LinkedOut && currentApp.hasLinkedOut()) {
                    this.previousPage = currentPage;
                    this.currentPage = "LinkedOut Login";
                    // do nothing...
                }
                break;
            }
            case "Hoogle Login": {
                if (using instanceof Token) {
                    Token token = (Token) using;
                    if (token.getAccessToken() != null) {

                        cache.add(token);

                        this.previousPage = currentPage;
                        this.currentPage = "Home";
    
                        this.currentToken = token;
                        this.currentApp.registerUser((Token)token);
                    } else {
                        // If accessToken is null, then the user is not authenticated
                        // Go back to select providers page
                        this.currentPage = "Select a Provider";
                    }
                } else {
                    // do nothing...
                }

                break;
            }
            case "LinkedOut Login": {
                if (using instanceof Token) {
                    Token token = (Token) using;
                    if (token.getAccessToken() != null) {
                        this.previousPage = currentPage;
                        this.currentPage = "Home";
    
                        this.currentToken = token;
                        this.currentApp.registerUser((Token)token);
                    } else {
                        // If accessToken is null, then the user is not authenticated
                        // Go back to select providers page
                        this.currentPage = "Select a Provider";
                    }
                } else {
                    // do nothing...
                }

                break;
            }
            case "Home": {
                // no need to do anything
                break;
            }
        }
    }

    public page getCurPage() {
        return curPage;
    }

    public void setCurPage(page curPage) {
        this.curPage = curPage;
    }

    public page gethLogPage() {
        return hLogPage;
    }

    public void sethLogPage(page hLogPage) {
        this.hLogPage = hLogPage;
    }

    public page getLinLogPage() {
        return linLogPage;
    }

    public void setLinLogPage(page linLogPage) {
        this.linLogPage = linLogPage;
    }

    

    public page getHomePage() {
        return homePage;
    }

    public void setHomePage(page homePage) {
        this.homePage = homePage;
    }

    public void cacheAdd(Token t) {
        cache.add(t);
    }

    public void setCurToken(Token t) {
        currentToken = t;
    }

    public ClientApp getCurApp() {
        return currentApp;
    }
    
}


