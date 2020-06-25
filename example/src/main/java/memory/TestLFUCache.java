package memory;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.memory.impl.LFUMemoryImpl;

/**
 * Created by tony on 2018/10/22.
 */
public class TestLFUCache {

    public static void main(String args[]) {

        RxCache.config(new RxCache.Builder().memory(new LFUMemoryImpl(10)));

        RxCache rxCache = RxCache.getRxCache();
        rxCache.save("test1",1);
        rxCache.save("test2",2);
        rxCache.save("test3",3);
        rxCache.save("test4",4);
        rxCache.save("test5",5);

        System.out.println(rxCache.get("test1",Integer.class).getData());

        rxCache.save("test6",6);

        System.out.println(rxCache.get("test2",Integer.class).getData());
        System.out.println(rxCache.get("test3",Integer.class).getData());
        System.out.println(rxCache.get("test4",Integer.class).getData());

        rxCache.save("test7",7);
        rxCache.save("test8",8);

        System.out.println(rxCache.get("test2",Integer.class).getData());
        System.out.println(rxCache.get("test3",Integer.class).getData());
        System.out.println(rxCache.get("test4",Integer.class).getData());

        rxCache.save("test9",9);
        rxCache.save("test10",10);
        rxCache.save("test11",11);
        rxCache.save("test12",12);

        System.out.println(rxCache.get("test2",Integer.class).getData());
        System.out.println(rxCache.get("test3",Integer.class).getData());
        System.out.println(rxCache.get("test4",Integer.class).getData());

        rxCache.save("test13",13);
        rxCache.save("test14",14);

        System.out.println(rxCache.get("test1",Integer.class).getData());

        rxCache.clear();

        rxCache.save("test1",1);
        rxCache.save("test2",2);
        rxCache.save("test3",3);
        rxCache.save("test4",4);
        rxCache.save("test5",5);

        System.out.println(rxCache.get("test1",Integer.class).getData());
    }
}
