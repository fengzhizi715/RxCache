package memory;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.memory.OHCImpl;
import domain.User;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

import static com.safframework.rxcache.rxjava.rxjava3.RxCache_ExtensionKt.load2Observable;

/**
 * @FileName: memory.TestOHC
 * @author: Tony Shen
 * @date: 2021/10/21 4:39 下午
 * @version: V1.0 <描述当前版本功能>
 */
public class TestOHC {

    public static void main(String[] args) {

        RxCache.config(new RxCache.Builder().memory(new OHCImpl(1024*1024)));

        RxCache rxCache = RxCache.getRxCache();

        User u1 = new User();
        u1.name = "tony1";
        u1.password = "123456";
        rxCache.save("test1",u1);

        User u2 = new User();
        u2.name = "tony2";
        u2.password = "123456";
        rxCache.save("test2",u2);

        User u3 = new User();
        u3.name = "tony3";
        u3.password = "123456";
        rxCache.save("test3",u3);

        User u4 = new User();
        u4.name = "tony4";
        u4.password = "123456";
        rxCache.save("test4",u4);

        Observable<Record<User>> observable = load2Observable(rxCache, "test1", User.class);

        if (observable!=null) {

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
}
