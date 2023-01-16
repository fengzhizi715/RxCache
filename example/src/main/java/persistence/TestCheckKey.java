package persistence;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.persistence.disk.impl.DiskImpl;

import java.io.File;

/**
 * @FileName: persistence.TestCheckKey
 * @author: Tony Shen
 * @date: 2023/1/16 12:35 PM
 * @version: V1.0 <描述当前版本功能>
 */
public class TestCheckKey {

    public static void main(String[] args) {

        File cacheDirectory = new File("aaa");

        if (!cacheDirectory.exists()) {

            cacheDirectory.mkdir();
        }

        DiskImpl diskImpl = new DiskImpl(cacheDirectory);

        RxCache.config(new RxCache.Builder().persistence(diskImpl));

        RxCache rxCache = RxCache.getRxCache();
        Boolean result  = rxCache.checkKey("test");
        System.out.println(result);

        if (!result) {
            rxCache.remove("test");
        }
    }
}
