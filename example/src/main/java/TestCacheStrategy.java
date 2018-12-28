import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.CacheStrategy;
import com.safframework.rxcache.domain.Record;
import domain.User;

/**
 * Created by tony on 2018-12-28.
 */
public class TestCacheStrategy {

    public static void main(String[] args) {

        RxCache.config(new RxCache.Builder());

        RxCache rxCache = RxCache.getRxCache();

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        rxCache.save("test",u);

        Record<User> record1 = rxCache.get("test", User.class, CacheStrategy.PERSISTENCE);
        if (record1 == null) {
            System.out.println("record1 is null");
        }

        Record<User> record2 = rxCache.get("test", User.class, CacheStrategy.ALL);
        if (record2 != null) {
            System.out.println(record2.getData().name);
            System.out.println(record2.getData().password);
        }
    }
}
