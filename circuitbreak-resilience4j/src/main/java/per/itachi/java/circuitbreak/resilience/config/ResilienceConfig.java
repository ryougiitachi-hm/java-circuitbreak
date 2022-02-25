package per.itachi.java.circuitbreak.resilience.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfig {

    @Bean
    public void init() {
        // this method will execute normally.
    }

    @Bean
    public CircuitBreakerConfig defaultCircuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50.0f)
                .waitDurationInOpenState(Duration.ofSeconds(1L))
//                .ringBufferSizeInHalfOpenState(2) // deprecated, replaced by permittedNumberOfCallsInHalfOpenState
//                .ringBufferSizeInClosedState(2) // deprecated, replaced by slidingWindow
                .permittedNumberOfCallsInHalfOpenState(2)
//                .slidingWindow(2, 2, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .build();
    }

    @Bean
    public CircuitBreakerRegistry defaultCircuitBreakerRegistry() {
        return CircuitBreakerRegistry.custom().build();
    }
}
