package persistence.converter.disk;

import com.safframework.rxcache.converter.FastJSONConverter;
import persistence.BaseDiskWithConverter;

/**
 * Created by tony on 2018/11/6.
 */
public class TestDiskImplWithFastjson {

    public static void main(String[] args) {

        new BaseDiskWithConverter(new FastJSONConverter());
    }
}
