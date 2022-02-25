package per.itachi.java.circuitbreak.resilience.adapter;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import per.itachi.java.circuitbreak.resilience.dto.user.UserDto;

public class RedisAdapter implements RedisPort {

    @Autowired
    private CircuitBreakerRegistry registry;


    @Override
    public UserDto getUserInfo(String username) {
        CircuitBreaker circuitBreaker = registry.circuitBreaker(getClass().getSimpleName());
//        circuitBreaker.onError();
        return null;
    }
}
