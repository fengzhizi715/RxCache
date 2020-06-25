package persistence;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.persistence.okio.OkioImpl;

import java.io.File;

/**
 * @FileName: persistence.BaseOkioWithConverter
 * @author: Tony Shen
 * @date: 2020-06-25 22:51
 * @version: V1.0 <描述当前版本功能>
 */
public class BaseOkioWithConverter extends BasePersistence {

    public BaseOkioWithConverter(Converter converter) {

        File cacheDirectory = new File("aaa");

        if (!cacheDirectory.exists()) {

            cacheDirectory.mkdir();
        }

        OkioImpl okioImpl = new OkioImpl(cacheDirectory,converter);

        RxCache.config(new RxCache.Builder().persistence(okioImpl));

        RxCache rxCache = RxCache.getRxCache();

        testObject(rxCache);
        testMap(rxCache);
        testList(rxCache);
        testSet(rxCache);
    }
}
