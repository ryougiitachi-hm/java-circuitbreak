package per.itachi.java.circuitbreak.resilience.circuitbreak;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class DefaultCircuitBreakTest {

    @Test
    public void testCircuitBreak() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50.0f) // 50 by default
//                .automaticTransitionFromOpenToHalfOpenEnabled() // false by default
//                .enableAutomaticTransitionFromOpenToHalfOpen() // false by default
//                .currentTimestampFunction()
//                .waitDurationInOpenState() // 60s by default
//                .waitIntervalFunctionInOpenState() //
//                .maxWaitDurationInHalfOpenState() // n/a by default
//                .permittedNumberOfCallsInHalfOpenState() // 10 by default
                .slowCallRateThreshold(50.0f) // 100 by default
                .slowCallDurationThreshold(Duration.ofMillis(10L)) // 60s by default
//                .minimumNumberOfCalls() // 100 by default
//                .slidingWindowType()
//                .slidingWindowSize()
                .slidingWindow(2, 2, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
//                .ignoreException(throwable->true)
//                .ignoreExceptions() // [] by default
//                .recordResult(object->false) //
//                .recordException(throwable->true)
//                .recordExceptions() // [] by default
//                .writableStackTraceEnabled() // true by default
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.custom().build();
        registry.addConfiguration(getClass().getSimpleName(), config);
        CircuitBreaker circuitBreaker = registry
                .circuitBreaker(getClass().getSimpleName(), getClass().getSimpleName());
        CheckedFunction0<String> checkedFunction0 = CircuitBreaker
                .decorateCheckedSupplier(circuitBreaker, ()->{
                    TimeUnit.MILLISECONDS.sleep(50L);
                    return UUID.randomUUID().toString();
                });
        // accumulate many request
        int count = 10;
        for (int i = 0; i < count; ++i) {
            Try<String> result = Try.of(checkedFunction0)
                    .recover(throwable -> {
                        log.info("Error occurred, downgraded. ");
                        return null;
                    });
            log.info("The try is {}, circuitBreaker.state is {}, circuitBreaker.metrics is {}.",
                    result.get(), circuitBreaker.getState(), circuitBreaker.getMetrics());
        }
    }
}
