package persistence;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.memory.impl.FIFOMemoryImpl;
import com.safframework.rxcache.persistence.disk.impl.DiskImpl;

import java.io.File;

/**
 * @FileName: persistence.TestCacheStatistics
 * @author: Tony Shen
 * @date: 2020-07-11 23:33
 * @version: V1.0 <描述当前版本功能>
 */
public class TestCacheStatistics {

    public static void main(String[] args) {

        File cacheDirectory = new File("aaa");

        if (!cacheDirectory.exists()) {

            cacheDirectory.mkdir();
        }

        DiskImpl diskImpl = new DiskImpl(cacheDirectory);

        RxCache.config(new RxCache.Builder().memory(new FIFOMemoryImpl(10)).persistence(diskImpl));

        RxCache rxCache = RxCache.getRxCache();

        for (int i=0;i<10;i++) {

            rxCache.save("test"+i,i);
        }

        rxCache.save("test10",10); // 保存第十一个数据，在内存缓存中移除第一个数据

        rxCache.info();
    }
}
