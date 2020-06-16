package converter;

import com.safframework.rxcache.converter.KryoConverter;

/**
 * Created by tony on 2019-04-17.
 */
public class TestDiskWithKryo {

    public static void main(String[] args) {

        new BaseDiskWithConverter(new KryoConverter());
    }
}
