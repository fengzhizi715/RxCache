package spring;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import domain.User;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.safframework.rxcache.rxjava.rxjava3.RxCache_ExtensionKt.load2Observable;


/**
 * Created by tony on 2018/10/5.
 */
public class TestWithSpring {

    public static void main(String[] args) {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);

        RxCache rxCache = (RxCache) ctx.getBean("rxCache");

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
}
