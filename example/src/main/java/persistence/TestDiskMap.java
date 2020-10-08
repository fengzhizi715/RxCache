package persistence;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.persistence.diskmap.DiskMapImpl;
import domain.User;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

import java.io.File;

/**
 * @FileName: persistence.TestDiskMap
 * @author: Tony Shen
 * @date: 2020-07-04 11:18
 * @version: V1.0 <描述当前版本功能>
 */
public class TestDiskMap {

    public static void main(String[] args) {

        File cacheDirectory = new File("aaa/db");

        DiskMapImpl diskMap = new DiskMapImpl(cacheDirectory);

        RxCache.config(new RxCache.Builder().persistence(diskMap));

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
