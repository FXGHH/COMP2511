package unsw.sso.unsw.sso.pages;

import javax.crypto.interfaces.PBEKey;

import unsw.sso.Browser;
import unsw.sso.ClientApp;

public interface page {
    // public page selectProvider();
    // public page hoogleLogin();
    // public String linkedOutLogin();
    // public String goBack();
    // abstract public void visit(ClientApp app, Browser b);
    abstract public void interact(ClientApp currentApp, Object using, Browser b);
    abstract public page back();
}
