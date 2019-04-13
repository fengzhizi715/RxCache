import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.memory.offheap.DirectBufferMemoryImpl;
import domain.User;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by tony on 2018-12-22.
 */
public class TestOffHeap {

    public static void main(String[] args) {

        RxCache.config(new RxCache.Builder().memory(new DirectBufferMemoryImpl(3)));

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

        Observable<Record<User>> observable = rxCache.load2Observable("test1", User.class);

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
