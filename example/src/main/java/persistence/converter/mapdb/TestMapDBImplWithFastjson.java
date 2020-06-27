package persistence.converter.mapdb;

import com.safframework.rxcache.converter.FastJSONConverter;
import persistence.BaseMapDBWithConverter;

/**
 * @FileName: persistence.converter.mapdb.TestMapDBImplWithFastjson
 * @author: Tony Shen
 * @date: 2020-06-25 23:22
 * @version: V1.0 <描述当前版本功能>
 */
public class TestMapDBImplWithFastjson {

    public static void main(String[] args) {

        new BaseMapDBWithConverter(new FastJSONConverter());
    }
}
