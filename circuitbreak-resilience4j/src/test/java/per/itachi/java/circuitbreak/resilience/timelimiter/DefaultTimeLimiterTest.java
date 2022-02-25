package per.itachi.java.circuitbreak.resilience.timelimiter;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.vavr.control.Try;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class DefaultTimeLimiterTest {

//    @Test
    public void testTimeLimiter() {
        // thanks to not many parameters for time limiter
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .cancelRunningFuture(true) // true by default
                .timeoutDuration(Duration.ofMillis(10L)) // 1s by default
                .build();
        TimeLimiterRegistry timeLimiterRegistry = TimeLimiterRegistry.ofDefaults();
        TimeLimiter timeLimiter = timeLimiterRegistry
                .timeLimiter(getClass().getSimpleName(), timeLimiterConfig);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Supplier<Future<String>> supplier = () ->
                executorService.submit(()-> {
                    TimeUnit.SECONDS.sleep(2L);
                    return UUID.randomUUID().toString();
                });
        Callable<String> callableTimeLimiter = TimeLimiter.decorateFutureSupplier(timeLimiter, supplier);
        Try.of(callableTimeLimiter::call)
                .onFailure(throwable -> log.error("Error occurred. ", throwable));
    }

    @Test
    public void testTimeLimiterWithCircuitBreak() {
        // thanks to not many parameters for time limiter
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .cancelRunningFuture(true) // true by default
                .timeoutDuration(Duration.ofMillis(10L)) // 1s by default
                .build();
        TimeLimiterRegistry timeLimiterRegistry = TimeLimiterRegistry.ofDefaults();
        TimeLimiter timeLimiter = timeLimiterRegistry
                .timeLimiter(getClass().getSimpleName(), timeLimiterConfig);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Supplier<Future<String>> supplier = () ->
                executorService.submit(()-> {
                    TimeUnit.SECONDS.sleep(2L);
                    return UUID.randomUUID().toString();
                });
        Callable<String> callableTimeLimiter = TimeLimiter.decorateFutureSupplier(timeLimiter, supplier);
        Callable<String> callableCircuitBreak = CircuitBreaker
                .decorateCallable(CircuitBreaker.ofDefaults(getClass().getSimpleName()), callableTimeLimiter);
        Try.of(callableCircuitBreak::call)
                .onFailure(throwable -> {
                    log.error("Error occurred. ", throwable);
                });
    }
}
