import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;

/**
 * Created by tony on 2018/10/5.
 */
public class TestWithExpireTime {

    public static void main(String[] args) {

        RxCache.config(new RxCache.Builder());

        RxCache rxCache = RxCache.getRxCache();

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        rxCache.save("test",u,2000);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Record<User> record = rxCache.get("test", User.class);

        if (record==null) {
            System.out.println("record is null");
        }
    }
}
