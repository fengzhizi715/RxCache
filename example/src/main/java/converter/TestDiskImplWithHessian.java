package converter;

import com.safframework.rxcache.converter.HessianConverter;

/**
 * Created by tony on 2019-04-17.
 */
public class TestDiskImplWithHessian {

    public static void main(String[] args) {

        new BaseDiskWithConverter(new HessianConverter());
    }
}