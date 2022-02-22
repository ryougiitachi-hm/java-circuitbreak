package per.itachi.java.circuitbreak.resilience.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfig {

    @Bean
    public void init() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50.0f)
                .waitDurationInOpenState(Duration.ofSeconds(1L))
//                .ringBufferSizeInHalfOpenState(2) // deprecated, replaced by permittedNumberOfCallsInHalfOpenState
//                .ringBufferSizeInClosedState(2) // deprecated, replaced by slidingWindow
                .permittedNumberOfCallsInHalfOpenState(2)
//                .slidingWindow(2, 2, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .build();
    }
}
