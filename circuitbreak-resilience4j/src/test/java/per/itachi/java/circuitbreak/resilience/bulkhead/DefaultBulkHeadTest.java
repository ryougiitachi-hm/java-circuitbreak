package per.itachi.java.circuitbreak.resilience.bulkhead;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class DefaultBulkHeadTest {

    @Test
    public void testBulkHead() {
        BulkheadConfig bulkheadConfig = BulkheadConfig.custom()
//                .fairCallHandlingStrategyEnabled()
                .maxConcurrentCalls(1)
                .maxWaitDuration(Duration.ofSeconds(1L))
//                .writableStackTraceEnabled()
                .build();
        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.ofDefaults();
        Bulkhead bulkhead = bulkheadRegistry.bulkhead(getClass().getSimpleName(), bulkheadConfig);
        CheckedFunction0<String> checkedFunction0 = Bulkhead.decorateCheckedSupplier(bulkhead, ()->{
            log.info("sleep...");
            TimeUnit.SECONDS.sleep(2L);
            return UUID.randomUUID().toString();
        });
        int count = 5;
        for(int i = 0; i < count; ++i) {
            Try<String> result = Try.of(checkedFunction0).recover(throwable -> {
                log.error("Error occurred. ", throwable);
                return "";
            });
            log.info("result.isSuccess={}, result={}.", result.isSuccess(), result.get());
        }
    }

    @Test
    public void testThreadPoolBulkhead() {}
}
