package per.itachi.java.circuitbreak.resilience.retry;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class DefaultRetryTest {

//    @Test
    public void testRetry() {
        RetryConfig retryConfig = RetryConfig.<String>custom()
                .maxAttempts(3)
                .failAfterMaxAttempts(true)
                .waitDuration(Duration.ofMillis(1000L))
//                .retryExceptions()
//                .ignoreExceptions()
//                .retryOnException()
//                .retryOnResult(result->{})
//                .intervalFunction(()->{})
//                .intervalBiFunction(()->{})
//                .writableStackTraceEnabled()
                .build();
        RetryRegistry retryRegistry = RetryRegistry.ofDefaults();
        Retry retry = retryRegistry.retry(getClass().getSimpleName(), retryConfig);
        CheckedFunction0 checkedFunction0 = Retry.decorateCheckedSupplier(retry, ()->{
            log.info("executing...");
            throw new RuntimeException("accumulate retry. ");
        });
        Try<String> result = Try.of(checkedFunction0)
                .recover(throwable-> {
                    log.error("Failed to retry.", throwable);
                    return "null";
                });
//        Try<String> result = Try.of(checkedFunction0);
        log.info("result.isSuccess={}, result={}.", result.isSuccess(), result);
    }

    @Test
    public void testRetryOnResult() {
        RetryConfig retryConfig = RetryConfig.<String>custom()
                .maxAttempts(3)
                .failAfterMaxAttempts(true)
                .waitDuration(Duration.ofMillis(1000L))
//                .retryExceptions()
//                .ignoreExceptions()
//                .retryOnException()
                .retryOnResult(Objects::isNull)
//                .intervalFunction(()->{})
//                .intervalBiFunction(()->{})
//                .writableStackTraceEnabled()
                .build();
        RetryRegistry retryRegistry = RetryRegistry.ofDefaults();
        Retry retry = retryRegistry.retry(getClass().getSimpleName(), retryConfig);
        CheckedFunction0<String> checkedFunction0 = Retry.decorateCheckedSupplier(retry, ()->{
            log.info("executing...");
            return null;
        });
//        Try<String> result = Try.of(checkedFunction0)
//                .recover(throwable-> {
//                    log.error("Failed to retry.", throwable);
//                    return "null";
//                });
        Try<String> result = Try.of(checkedFunction0);
        log.info("result.isSuccess={}, result={}.", result.isSuccess(), result);
    }
}
