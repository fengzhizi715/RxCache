package persistence.converter;

import com.safframework.rxcache.converter.KryoConverter;
import persistence.BaseDiskWithConverter;

/**
 * Created by tony on 2019-04-17.
 */
public class TestDiskImplWithKryo {

    public static void main(String[] args) {

        new BaseDiskWithConverter(new KryoConverter());
    }
}
