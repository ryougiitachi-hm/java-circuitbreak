package per.itachi.java.circuitbreak.resilience.adapter;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import per.itachi.java.circuitbreak.resilience.dto.user.UserDto;

@Primary
@Slf4j
@Component
public class RedisMockAdapter implements RedisPort {

    @Autowired
    private CircuitBreakerConfig circuitBreakerConfig;

    /**
     * spring el is avaiable for fallbackMethod, but not expected, better to not use it.
     * */
//    @CircuitBreaker(name = "redis-mock", fallbackMethod = "#{redisFallbackAdapter.loadUserInfo(#username)}")
    @CircuitBreaker(name = "redis-mock", fallbackMethod = "loadUserInfoFallback")
    @Override
    public UserDto loadUserInfo(String username) {
        log.info("Start loading user info for username {}.", username);
        try {
            TimeUnit.SECONDS.sleep(1L);
            throw new RuntimeException();
//            return UserDto.builder().username(UUID.randomUUID().toString()).build();
        }
        catch (InterruptedException e) {
            log.error("Thread {} was interrupted. ", Thread.currentThread());
            return UserDto.builder().username(null).build();
        }
    }

    private UserDto loadUserInfoFallback(String username, Throwable throwable) {
        log.error("Downgraded, throwable={}. ", throwable);
        return UserDto.builder().username("error username").build();
    }


}
