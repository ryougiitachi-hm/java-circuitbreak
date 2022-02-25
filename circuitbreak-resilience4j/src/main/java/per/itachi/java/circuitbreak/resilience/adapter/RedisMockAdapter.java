package per.itachi.java.circuitbreak.resilience.adapter;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import per.itachi.java.circuitbreak.resilience.dto.user.UserDto;

@Component
public class RedisMockAdapter implements RedisPort {

    @Autowired
    private CircuitBreakerConfig circuitBreakerConfig;

    @Override
    public UserDto getUserInfo(String username) {
        return null;
    }
}

