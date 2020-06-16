package converter;

import com.safframework.rxcache.converter.FSTConverter;

/**
 * Created by tony on 2019-04-16.
 */
public class TestDiskWithFST {

    public static void main(String[] args) {

        new BaseDiskWithConverter(new FSTConverter());
    }
}
