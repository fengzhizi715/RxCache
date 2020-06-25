package memory;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.memory.impl.FIFOMemoryImpl;

/**
 * Created by tony on 2019-02-08.
 */
public class TestCacheStatistics {

    public static void main(String[] args) {

        RxCache.config(new RxCache.Builder().memory(new FIFOMemoryImpl(10)));

        RxCache rxCache = RxCache.getRxCache();

        for (int i=0;i<10;i++) {

            rxCache.save("test"+i,i);
        }

        rxCache.save("test10",10);

        rxCache.info();
    }
}
