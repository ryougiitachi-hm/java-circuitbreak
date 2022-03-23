package per.itachi.java.circuitbreak.resilience.config;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfig {

    @Bean
    public void init() {
    }

    @Bean
    public CircuitBreakerConfig defaultCircuitBreakerConfig() {
        // too many parameters for circuit break
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50.0f)
                .waitDurationInOpenState(Duration.ofSeconds(1L))
//                .ringBufferSizeInHalfOpenState(2) // deprecated, replaced by permittedNumberOfCallsInHalfOpenState
//                .ringBufferSizeInClosedState(2) // deprecated, replaced by slidingWindow
                .permittedNumberOfCallsInHalfOpenState(2)
                .slidingWindow(2, 2, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .build();
    }

//    @Bean
    public CircuitBreakerRegistry defaultCircuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }

    @Bean
    public RateLimiterConfig defaultRateLimiterConfig() {
        // thanks to not many parameters for rate limiter.
        return RateLimiterConfig.custom()
//                .drainPermissionsOnResult()
//                .limitForPeriod()
//                .limitRefreshPeriod()
//                .writableStackTraceEnabled()
//                .timeoutDuration()
                .build();
    }

    @Bean
    public RateLimiterRegistry defaultRateLimiterRegistry() {
        return RateLimiterRegistry.custom().build();
    }

    @Bean
    public BulkheadConfig defaultBulkheadConfig() {
        // thanks to not many parameters for bulk head.
        return BulkheadConfig.custom()
//                .fairCallHandlingStrategyEnabled()
//                .maxConcurrentCalls()
//                .maxWaitDuration()
//                .writableStackTraceEnabled()
                .build();
    }

    @Bean
    public BulkheadRegistry defaultBulkheadRegistry() {
        return BulkheadRegistry.ofDefaults();
    }

    @Bean
    public ThreadPoolBulkheadConfig defaultThreadPoolBulkheadConfig() {
        // thanks to not many parameters for thread bulk head.
        return ThreadPoolBulkheadConfig.custom()
                .coreThreadPoolSize(ThreadPoolBulkheadConfig.DEFAULT_CORE_THREAD_POOL_SIZE)
                .maxThreadPoolSize(ThreadPoolBulkheadConfig.DEFAULT_MAX_THREAD_POOL_SIZE)
//                .contextPropagator()
//                .writableStackTraceEnabled()
                .build();
    }
}
