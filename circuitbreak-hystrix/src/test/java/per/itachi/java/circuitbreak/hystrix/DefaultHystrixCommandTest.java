package per.itachi.java.circuitbreak.hystrix;

import com.netflix.hystrix.HystrixCommand;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import per.itachi.java.circuitbreak.hystrix.command.GetUserRedisHystrixCommand;
import per.itachi.java.circuitbreak.hystrix.command.SetUserRedisHystrixCommand;
import per.itachi.java.circuitbreak.hystrix.redis.RedisMockAdapter;
import per.itachi.java.circuitbreak.hystrix.redis.RedisPort;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class DefaultHystrixCommandTest {

    @InjectMocks
    private RedisMockAdapter adapter;

    @Test
    public void testHystrixCommandForGetting() {
        HystrixCommand<String> command = null;
        int count = 10;
        for (int i = 0; i < count; ++i) {
            command = new GetUserRedisHystrixCommand(adapter, UUID.randomUUID().toString());
            log.info("The result is {}, circuitbreak.isOpen={}.",
                    command.execute(), command.isCircuitBreakerOpen());
        }
    }

    @Test
    public void testHystrixCommandForSetting() {
        HystrixCommand<String> command = null;
        int count = 10;
        for (int i = 0; i < count; ++i) {
            command = new SetUserRedisHystrixCommand(adapter, UUID.randomUUID().toString());
            log.info("The result is {}, circuitbreak.isOpen={}.",
                    command.execute(), command.isCircuitBreakerOpen());
        }
    }

    private void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        }
        catch (InterruptedException e) {
            log.error("", e);
        }
    }
}