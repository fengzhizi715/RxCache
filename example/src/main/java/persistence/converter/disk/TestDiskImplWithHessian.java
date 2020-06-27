package persistence.converter.disk;

import com.safframework.rxcache.converter.HessianConverter;
import persistence.BaseDiskWithConverter;

/**
 * Created by tony on 2019-04-17.
 */
public class TestDiskImplWithHessian {

    public static void main(String[] args) {

        new BaseDiskWithConverter(new HessianConverter());
    }
}
