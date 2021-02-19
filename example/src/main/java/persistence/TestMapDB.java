package persistence;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;

import com.safframework.rxcache.persistence.mapdb.MapDBImpl;
import domain.User;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

import java.io.File;

import static com.safframework.rxcache.rxjava.rxjava3.RxCache_ExtensionKt.load2Observable;

/**
 * Created by tony on 2019-10-05.
 */
public class TestMapDB {

    public static void main(String[] args) {

        File file = new File("aaa/db");
        MapDBImpl mapdbImpl = new MapDBImpl(file);
        RxCache.config(new RxCache.Builder().persistence(mapdbImpl));

        RxCache rxCache = RxCache.getRxCache();

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        rxCache.save("test",u);

        Observable<Record<User>> observable = load2Observable(rxCache, "test", User.class);

        observable.subscribe(new Consumer<Record<User>>() {
            @Override
            public void accept(Record<User> record) throws Exception {

                User user = record.getData();
                System.out.println(user.name);
                System.out.println(user.password);
            }
        });

        mapdbImpl.close();
    }
}
