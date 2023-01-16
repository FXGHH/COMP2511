package unsw.sso.unsw.sso.pages;

import unsw.sso.Browser;
import unsw.sso.ClientApp;
import unsw.sso.providers.Hoogle;
import unsw.sso.providers.LinkedOut;

public class SelProviderState implements page{
    private String currentPage = "Select a Provider";
    private String previousPage = null;
    // Browser browser;

    // @Override
    // public void visit(ClientApp app, Browser b) {
        
    //     this.currentPage = b.currPageStatus(app);
    //     // this.currentPage = "Select a Provider";
    //     this.currentApp = app;
    // }

    @Override
    public void interact(ClientApp currentApp, Object using, Browser b) {
        if (using instanceof Hoogle && currentApp.hasHoogle()) {
            b.setPageState(b.gethLogPage());
        } else if (using instanceof LinkedOut && currentApp.hasLinkedOut()) {
            b.setPageState(b.getLinLogPage());
        }
    }

    @Override
    public page back() {
        // TODO Auto-generated method stub
        return null;
    }

    
    
}
