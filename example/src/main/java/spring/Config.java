package spring;

import com.safframework.rxcache.RxCache;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * Created by tony on 2018/10/5.
 */
@Configurable
public class Config {

    @Bean
    public RxCache.Builder rxCacheBuilder(){
        return new RxCache.Builder();
    }

    @Bean
    public RxCache rxCache() {

        RxCache.config(rxCacheBuilder());

        return RxCache.getRxCache();
    }
}
