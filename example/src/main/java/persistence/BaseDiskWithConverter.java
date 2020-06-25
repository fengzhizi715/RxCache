package persistence;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.persistence.disk.impl.DiskImpl;

import java.io.File;

/**
 * @FileName: converter.TestDiskWithConverters
 * @author: Tony Shen
 * @date: 2020-06-16 11:16
 * @version: V1.0 <描述当前版本功能>
 */
public class BaseDiskWithConverter extends BasePersistence{

    public BaseDiskWithConverter(Converter converter) {

        File cacheDirectory = new File("aaa");

        if (!cacheDirectory.exists()) {

            cacheDirectory.mkdir();
        }

        DiskImpl diskImpl = new DiskImpl(cacheDirectory,converter);

        RxCache.config(new RxCache.Builder().persistence(diskImpl));

        RxCache rxCache = RxCache.getRxCache();

        testObject(rxCache);
        testMap(rxCache);
        testList(rxCache);
        testSet(rxCache);
    }
}