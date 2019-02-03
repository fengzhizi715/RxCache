import com.safframework.bytekit.utils.Preconditions;
import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.persistence.disk.impl.DiskImpl;
import com.safframework.rxcache.reflect.TypeBuilder;
import domain.User;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2019-02-03.
 */
public class TestTypeBuilder {

    public static void main(String[] args) {

        File cacheDirectory = new File("aaa");

        if (!cacheDirectory.exists()) {

            cacheDirectory.mkdir();
        }

        DiskImpl diskImpl = new DiskImpl(cacheDirectory);

        RxCache.config(new RxCache.Builder().persistence(diskImpl));

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

        Type type = TypeBuilder.newInstance(List.class)
                .addTypeParam(User.class)
                .build();

        Observable<Record<List<User>>> observable = rxCache.load2Observable("test", type);

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
