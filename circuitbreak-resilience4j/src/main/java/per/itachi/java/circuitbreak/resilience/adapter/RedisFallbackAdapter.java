package per.itachi.java.circuitbreak.resilience.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import per.itachi.java.circuitbreak.resilience.dto.user.UserDto;

@Slf4j
@Component
public class RedisFallbackAdapter implements RedisPort{

    @Override
    public UserDto loadUserInfo(String username) {
        log.info("Downgraded, username={}. ", username);
        return null;
    }
}
