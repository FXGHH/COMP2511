package unsw.sso.unsw.sso;

import java.util.List;

public interface provider {
   public void registerProvider(List<String> providers);
   public String getProviderName();
}
