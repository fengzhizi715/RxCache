import com.safframework.rxcache.RxCache;
import domain.User;

import java.util.concurrent.TimeUnit;

/**
 * Created by tony on 2019-02-15.
 */
public class TestTimeUnit {

    public static void main(String[] args) {

        RxCache.config(new RxCache.Builder());

        RxCache rxCache = RxCache.getRxCache();

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        rxCache.save("test",u,5, TimeUnit.SECONDS);

        try {
            Thread.sleep(2300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ttl="+rxCache.ttl("test",User.class));
    }
}
