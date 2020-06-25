package persistence.converter;

import com.safframework.rxcache.converter.FastJSONConverter;
import persistence.BaseOkioWithConverter;

/**
 * @FileName: persistence.converter.TestOkioImplWithFastjson
 * @author: Tony Shen
 * @date: 2020-06-25 22:58
 * @version: V1.0 <描述当前版本功能>
 */
public class TestOkioImplWithFastjson {

    public static void main(String[] args) {

        new BaseOkioWithConverter(new FastJSONConverter());
    }
}
