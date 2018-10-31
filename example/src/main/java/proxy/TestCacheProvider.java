package proxy;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.proxy.CacheProvider;
import domain.User;
import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;

/**
 * Created by tony on 2018/10/31.
 */
public class TestCacheProvider {

    public static void main(String[] args) {


        RxCache.config(new RxCache.Builder());

        RxCache rxCache = RxCache.getRxCache();

        CacheProvider cacheProvider = new CacheProvider.Builder().rxCache(rxCache).build();

        Provider provider = cacheProvider.create(Provider.class);

        User u = new User();
        u.name = "tony";
        u.password = "123456";

        provider.putData(u); // 将u存入缓存中

        Record<User> record = provider.getData(User.class); // 从缓存中获取key="user"的数据

        if (record!=null) {

            System.out.println(record.getData().name);
        }

        provider.removeUser(); // 从缓存中删除key="user"的数据

        record = provider.getData(User.class);

        if (record==null) {

            System.out.println("record is null");
        }

        User u2 = new User();
        u2.name = "tony2";
        u2.password = "000000";
        rxCache.save("test",u2);

        Maybe<Record<User>> maybe = provider.getMaybe(User.class); // 从缓存中获取key="test"的数据，返回的类型为Maybe
        maybe.subscribe(new Consumer<Record<User>>() {
            @Override
            public void accept(Record<User> userRecord) throws Exception {

                User user = userRecord.getData();
                if (user!=null) {

                    System.out.println(user.name);
                    System.out.println(user.password);
                }
            }
        });
    }
}
