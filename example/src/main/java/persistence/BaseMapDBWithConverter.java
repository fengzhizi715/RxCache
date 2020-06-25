package persistence;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.persistence.mapdb.MapDBImpl;

import java.io.File;

/**
 * @FileName: persistence.BaseMapDBWithConverter
 * @author: Tony Shen
 * @date: 2020-06-25 23:20
 * @version: V1.0 <描述当前版本功能>
 */
public class BaseMapDBWithConverter extends BasePersistence {

    public BaseMapDBWithConverter(Converter converter) {

        File cacheDirectory = new File("aaa/db");
        MapDBImpl mapdbImpl = new MapDBImpl(cacheDirectory,converter);

        RxCache.config(new RxCache.Builder().persistence(mapdbImpl));

        RxCache rxCache = RxCache.getRxCache();

        testObject(rxCache);
        testMap(rxCache);
        testList(rxCache);
        testSet(rxCache);
    }
}
