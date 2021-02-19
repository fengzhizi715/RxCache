package persistence;

import com.safframework.bytekit.utils.Preconditions;
import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.reflect.TypeBuilder;
import domain.User;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

import java.lang.reflect.Type;
import java.util.*;

import static com.safframework.rxcache.rxjava.rxjava3.RxCache_ExtensionKt.load2Observable;

/**
 * @FileName: persistence.BasePersistence
 * @author: Tony Shen
 * @date: 2020-06-25 23:03
 * @version: V1.0 <描述当前版本功能>
 */
public class BasePersistence {

    public static void testObject(RxCache rxCache) {

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
    }

    public static void testMap(RxCache rxCache){

        Map<String,User> map = new HashMap<>();

        User u1 = new User();
        u1.name = "tonyMap1";
        u1.password = "map1123456";
        map.put("u1",u1);

        User u2 = new User();
        u2.name = "tonyMap12";
        u2.password = "map12345";
        map.put("u2",u2);
        rxCache.save("map",map);
        Type type = TypeBuilder
                .newInstance(HashMap.class)
                .addTypeParam(String.class)
                .addTypeParam(User.class)
                .build();

        Observable<Record<Map<String,User>>> observable = load2Observable(rxCache,"map", type);

        observable.subscribe(new Consumer<Record<Map<String,User>>>() {

            @Override
            public void accept(Record<Map<String,User>> record) throws Exception {

                Map<String,User> recordDataList = record.getData();

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

    public static void testList(RxCache rxCache) {

        List<User> list = new ArrayList<>();

        User u1 = new User();
        u1.name = "tonyList1";
        u1.password = "list1123456";
        list.add(u1);

        User u2 = new User();
        u2.name = "tonyList12";
        u2.password = "list12345";
        list.add(u2);
        rxCache.save("list", list);

        Type type = TypeBuilder
                .newInstance(List.class)
                .addTypeParam(User.class)
                .build();

        Observable<Record<List<User>>> observable = load2Observable(rxCache,"list", type);

        observable.subscribe(new Consumer<Record<List<User>>>() {

            @Override
            public void accept(Record<List<User>> record) throws Exception {

                List<User> recordDataList = record.getData();

                if (Preconditions.isNotBlank(recordDataList)) {
                    for (User user : recordDataList) {
                        System.out.println(user.name);
                        System.out.println(user.password);
                    }
                }
            }
        });
    }

    public static void testSet(RxCache rxCache) {

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

        Observable<Record<Set<User>>> observable = load2Observable(rxCache, "set", type);

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
