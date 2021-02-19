import com.safframework.bytekit.utils.Preconditions;
import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import domain.User;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

import java.util.ArrayList;
import java.util.List;

import static com.safframework.rxcache.rxjava.rxjava3.RxCache_ExtensionKt.load2Observable;

/**
 * Created by tony on 2019-02-03.
 */
public class TestList {

    public static void main(String[] args) {

        RxCache.config(new RxCache.Builder());

        RxCache rxCache = RxCache.getRxCache();

        List<User> list = new ArrayList<>();

        User u1 = new User();
        u1.name = "tony1";
        u1.password = "123456";
        list.add(u1);

        User u2 = new User();
        u2.name = "tony2";
        u2.password = "123456";
        list.add(u2);
        rxCache.save("test",list);

        Observable<Record<List<User>>> observable = load2Observable(rxCache, "test", User.class);

        observable.subscribe(new Consumer<Record<List<User>>>() {

            @Override
            public void accept(Record<List<User>> record) throws Exception {

                List<User> recordDataList = record.getData();

                if (Preconditions.isNotBlank(recordDataList)) {

                    User user = recordDataList.get(0);
                    System.out.println(user.name);
                    System.out.println(user.password);
                }
            }
        });
    }
}
