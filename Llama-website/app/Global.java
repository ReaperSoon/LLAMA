import com.avaje.ebean.Ebean;
import models.Product;
import models.User;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;

import java.security.Security;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Global extends GlobalSettings {

    private final static Logger.ALogger LOGGER = Logger.of(Global.class);

    private AtomicBoolean isStarted = new AtomicBoolean(false);

    @SuppressWarnings("unchecked")
    @Override
    public void onStart(Application app) {
        if (isStarted.compareAndSet(false, true)) {
            try {
                final Map<String, List<Object>> data = (Map<String, List<Object>>) Yaml.load("init.yml");
                if (Ebean.find(User.class).findRowCount() == 0)
                    Ebean.save(data.get("users"));

                if (Ebean.find(Product.class).findRowCount() == 0)
                    Ebean.save(data.get("products"));


            } catch (Exception ex) {
                LOGGER.error("Error while starting application. Cause: " + ex, ex);
            }
        }
    }

}
