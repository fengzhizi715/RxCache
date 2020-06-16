package converter;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.converter.FastJSONConverter;
import com.safframework.rxcache.persistence.disk.impl.DiskImpl;

import java.io.File;

/**
 * Created by tony on 2018/11/6.
 */
public class TestDiskImplWithFastjson extends BaseDiskWithConverter {

    public static void main(String[] args) {

        File cacheDirectory = new File("aaa");

        if (!cacheDirectory.exists()) {

            cacheDirectory.mkdir();
        }

        DiskImpl diskImpl = new DiskImpl(cacheDirectory,new FastJSONConverter());

        RxCache.config(new RxCache.Builder().persistence(diskImpl));

        RxCache rxCache = RxCache.getRxCache();

        testObject(rxCache);
        testMap(rxCache);
        testList(rxCache);
        testSet(rxCache);
    }

}
