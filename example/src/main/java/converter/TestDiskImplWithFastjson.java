package converter;

import com.safframework.rxcache.converter.FastJSONConverter;

/**
 * Created by tony on 2018/11/6.
 */
public class TestDiskImplWithFastjson {

    public static void main(String[] args) {

        new BaseDiskWithConverter(new FastJSONConverter());
    }
}
