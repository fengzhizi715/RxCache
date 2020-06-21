package converter;

import com.safframework.rxcache.converter.ProtobufConverter;

/**
 * @FileName: converter.TestDiskImplWithProtobuf
 * @author: Tony Shen
 * @date: 2020-06-21 12:54
 * @version: V1.0 <描述当前版本功能>
 */
public class TestDiskImplWithProtobuf {

    public static void main(String[] args) {

        new BaseDiskWithConverter(new ProtobufConverter());
    }
}
