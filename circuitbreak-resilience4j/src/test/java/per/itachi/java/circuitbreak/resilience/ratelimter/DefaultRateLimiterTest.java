package per.itachi.java.circuitbreak.resilience.ratelimter;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class DefaultRateLimiterTest {

    @Test
    public void testRateLimiter() {
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.ofDefaults();
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
//                .writableStackTraceEnabled(true)
                .limitRefreshPeriod(Duration.ofMillis(1000L)) // 500ns by default
                .limitForPeriod(2) // 50 by default, convenient for testing.
                .timeoutDuration(Duration.ofMillis(1000L)) // 5s by default
//                .drainPermissionsOnResult(either-> false)
                .build();
        RateLimiter rateLimiter = rateLimiterRegistry
                .rateLimiter(getClass().getSimpleName(), rateLimiterConfig);
        rateLimiter.getEventPublisher()
                .onSuccess(event -> {
                    // can be considered as metric statistics.
                    log.info("The event has succeeded, type={}. ", event.getEventType());
                })
                .onFailure(event -> {
                    // can be considered as metric statistics.
                    log.error("Failure {}. ", event);
                });
        CheckedRunnable checkedRunnable = RateLimiter.decorateCheckedRunnable(rateLimiter, ()->{
            log.info("executed.");
        });
//        int countOfCalls = 10;
//        for (int i = 0; i < countOfCalls; ++i) {
//            Try.run(checkedRunnable).onFailure(throwable -> {
//                log.error("Error occurred. ", throwable);
//            });
//        }
        Try.run(checkedRunnable)
                .andThenTry(checkedRunnable)
                .andThenTry(checkedRunnable)
                .andThenTry(checkedRunnable)
                .andThenTry(checkedRunnable)
                .andThenTry(checkedRunnable)
                .onFailure(throwable -> {
                    log.error("Error occurred. ", throwable);
                });
    }

}
