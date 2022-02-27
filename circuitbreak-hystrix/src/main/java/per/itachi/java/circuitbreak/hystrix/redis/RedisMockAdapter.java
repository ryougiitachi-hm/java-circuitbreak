package per.itachi.java.circuitbreak.hystrix.redis;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisMockAdapter implements RedisPort{

    @Override
    public String loadUserInfo(String username) {
        log.info("Redis start loading user info. ");
        try {
            TimeUnit.SECONDS.sleep(1L);
        }
        catch (InterruptedException e) {
            log.error("Sleep interrupted. ", e);
        }
        return UUID.randomUUID().toString();
    }

    @Override
    public void updateUserInfo(String username) {
        try {
            TimeUnit.SECONDS.sleep(1L);
            log.info("Redis start updating user info. ");
        }
        catch (InterruptedException e) {
            log.error("Sleep interrupted. ", e);
        }
    }

}
