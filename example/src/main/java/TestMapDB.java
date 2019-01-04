import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.memory.MapDBImpl;
import domain.User;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by tony on 2019-01-04.
 */
public class TestMapDB {

    public static void main(String[] args) {

        RxCache.config(new RxCache.Builder().memory(new MapDBImpl(100)));

        RxCache rxCache = RxCache.getRxCache();

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        rxCache.save("test",u);

        Observable<Record<User>> observable = rxCache.load2Observable("test", User.class);

        observable.subscribe(new Consumer<Record<User>>() {
            @Override
            public void accept(Record<User> record) throws Exception {

                User user = record.getData();
                System.out.println(user.name);
                System.out.println(user.password);
            }
        });
    }
}
