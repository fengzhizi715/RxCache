import com.safframework.bytekit.utils.Preconditions;
import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.converter.MoshiConverter;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.persistence.disk.impl.DiskImpl;
import com.safframework.rxcache.reflect.TypeBuilder;
import domain.User;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by tony on 2018/11/6.
 */
public class TestDiskImplWithMoshi {

    public static void main(String[] args) {

        File cacheDirectory = new File("aaa");

        if (!cacheDirectory.exists()) {

            cacheDirectory.mkdir();
        }

        DiskImpl diskImpl = new DiskImpl(cacheDirectory, new MoshiConverter());

        RxCache.config(new RxCache.Builder().persistence(diskImpl));

        RxCache rxCache = RxCache.getRxCache();

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        rxCache.save("test", u);

        Observable<Record<User>> observable = rxCache.load2Observable("test", User.class);

        observable.subscribe(new Consumer<Record<User>>() {

            @Override
            public void accept(Record<User> record) throws Exception {

                User user = record.getData();
                System.out.println(user.name);
                System.out.println(user.password);
            }
        });

        testMap(rxCache);
        testSet(rxCache);
    }

    private static void testMap(RxCache rxCache) {

        Map<String, User> map = new HashMap<>();

        User u1 = new User();
        u1.name = "tonyMap1";
        u1.password = "map1123456";
        map.put("u1", u1);

        User u2 = new User();
        u2.name = "tonyMap12";
        u2.password = "map12345";
        map.put("u2", u2);
        rxCache.save("map", map);

        Type type = TypeBuilder
                .newInstance(Map.class)
                .addTypeParam(String.class)
                .addTypeParam(User.class)
                .build();

        Observable<Record<Map<String, User>>> observable = rxCache.load2Observable("map", type);

        observable.subscribe(new Consumer<Record<Map<String, User>>>() {

            @Override
            public void accept(Record<Map<String, User>> record) throws Exception {

                Map<String, User> recordDataList = record.getData();

                if (Preconditions.isNotBlank(recordDataList)) {

                    User user = recordDataList.get("u1");
                    System.out.println(user.name);
                    System.out.println(user.password);


                    User user2 = recordDataList.get("u2");
                    System.out.println(user2.name);
                    System.out.println(user2.password);
                }
            }
        });
    }

    private static void testSet(RxCache rxCache) {

        Set<User> set = new HashSet<>();

        User u1 = new User();
        u1.name = "tonySet1";
        u1.password = "set1123456";
        set.add(u1);

        User u2 = new User();
        u2.name = "tonySet12";
        u2.password = "set12345";
        set.add(u2);
        rxCache.save("set", set);

        Type type = TypeBuilder
                .newInstance(Set.class)
                .addTypeParam(User.class)
                .build();

        Observable<Record<Set<User>>> observable = rxCache.load2Observable("set", type);

        observable.subscribe(new Consumer<Record<Set<User>>>() {

            @Override
            public void accept(Record<Set<User>> record) throws Exception {

                Set<User> recordDataList = record.getData();

                if (Preconditions.isNotBlank(recordDataList)) {
                    for (User user : recordDataList) {
                        System.out.println(user.name);
                        System.out.println(user.password);
                    }
                }
            }
        });
    }
}
