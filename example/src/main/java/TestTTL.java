import com.safframework.rxcache.RxCache;
import domain.User;

/**
 * Created by tony on 2018/11/26.
 */
public class TestTTL {

    public static void main(String[] args) {

        RxCache.config(new RxCache.Builder());

        RxCache rxCache = RxCache.getRxCache();

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        rxCache.save("test",u,2500);

        try {
            Thread.sleep(2300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ttl="+rxCache.ttl("test",User.class));
    }
}
