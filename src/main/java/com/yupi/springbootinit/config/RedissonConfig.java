package com.yupi.springbootinit.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

//  private String password;
    private  Integer database;
    private Integer port;
    private String host;

    @Bean
    public RedissonClient RedissonConfig() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+ host + ":"+port)
                .setDatabase(database);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}

