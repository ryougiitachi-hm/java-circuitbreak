package per.itachi.java.circuitbreak.resilience.adapter;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import per.itachi.java.circuitbreak.resilience.dto.user.UserDto;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisAdapter implements RedisPort {

    @Autowired
    private CircuitBreakerConfig defaultCircuitBreakerConfig;

    @Autowired
    private CircuitBreakerRegistry defaultCircuitBreakerRegistry;

    @Override
    public UserDto loadUserInfo(String username) {
        // The difference between CircuitBreaker.of("") and circuitBreakerRegistry.circuitBreaker("")
        // CircuitBreakerRegistry creates CircuitBreaker via invoking CircuitBreaker.of("") eventually.
        // CircuitBreakerRegistry manages all of CircuitBreaker centrally rather than CircuitBreaker.of("").
        CircuitBreaker circuitBreaker = defaultCircuitBreakerRegistry
                .circuitBreaker(getClass().getSimpleName(), defaultCircuitBreakerConfig);
        CheckedFunction0<UserDto> decoratedSupplier = CircuitBreaker
                .decorateCheckedSupplier(circuitBreaker, ()->{
                    log.info("sleep...");
                    // no need to throw exception?
                    TimeUnit.SECONDS.sleep(2L);
                    log.info("wakeup...");
                    return UserDto.builder().build();
                });
        Try<UserDto> result = Try
                .of(decoratedSupplier)
                .recover(RuntimeException.class, throwable->{
                    log.error("Error occured. ", throwable);
                    return UserDto.builder().build();
                });
        UserDto userDto = result.get();
        log.info("The result is {}, result.isSuccess={}. ", result, result.isSuccess());
        log.info("The state of cb is {}. ", circuitBreaker.getState());
        return userDto;
    }

}
