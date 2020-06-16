package converter;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.converter.HessianConverter;
import com.safframework.rxcache.persistence.disk.impl.DiskImpl;

import java.io.File;

/**
 * Created by tony on 2019-04-17.
 */
public class TestDiskImplWithHessian extends BaseDiskWithConverter {

    public static void main(String[] args) {

        File cacheDirectory = new File("aaa");

        if (!cacheDirectory.exists()) {

            cacheDirectory.mkdir();
        }

        DiskImpl diskImpl = new DiskImpl(cacheDirectory, new HessianConverter());

        RxCache.config(new RxCache.Builder().persistence(diskImpl));

        RxCache rxCache = RxCache.getRxCache();

        testObject(rxCache);
        testMap(rxCache);
        testList(rxCache);
        testSet(rxCache);
    }
}
