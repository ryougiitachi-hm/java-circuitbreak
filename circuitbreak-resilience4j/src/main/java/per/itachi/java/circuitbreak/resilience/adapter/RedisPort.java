package per.itachi.java.circuitbreak.resilience.adapter;

import per.itachi.java.circuitbreak.resilience.dto.user.UserDto;

public interface RedisPort {

    UserDto loadUserInfo(String username);
}
