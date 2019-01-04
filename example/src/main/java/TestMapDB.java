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

        RxCache.config(new RxCache.Builder().memory(new MapDBImpl(5)));

        RxCache rxCache = RxCache.getRxCache();

        for (int i=0;i<6;i++) {

            User u = new User();
            u.name = "tony"+i;
            u.password = "123456";
            rxCache.save("test"+i,u);
        }

        System.out.println(rxCache.getAllKeys().size());
        Observable<Record<User>> observable = rxCache.load2Observable("test1", User.class);

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
