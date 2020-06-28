import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.CacheStrategy;
import com.safframework.rxcache.domain.Record;
import domain.User;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @FileName: PACKAGE_NAME.TestSaveMemoryOnly
 * @author: Tony Shen
 * @date: 2020-06-23 00:22
 * @version: V1.0 <描述当前版本功能>
 */
public class TestSaveMemoryOnly {

    public static void main(String[] args) {

        RxCache.config(new RxCache.Builder());

        RxCache rxCache = RxCache.getRxCache(); // 使用默认的 FIFOMemoryImpl 作为 memory 的实现

        User u = new User();
        u.name = "tony";
        u.password = "123456";
        rxCache.saveMemory("test",u);

        Observable<Record<User>> observable = rxCache.load2Observable("test", User.class);

        observable.subscribe(new Consumer<Record<User>>() {

            @Override
            public void accept(Record<User> record) throws Exception {

                User user = record.getData();
                System.out.println(user.name);
                System.out.println(user.password);
            }
        });

        Record<User> record1 = rxCache.get("test", User.class, CacheStrategy.PERSISTENCE);
        if (record1 == null) {
            System.out.println("record is null");
        }
    }
}
