package per.itachi.java.circuitbreak.resilience.adapter;

import per.itachi.java.circuitbreak.resilience.dto.user.UserDto;

public class RedisMockAdapter implements RedisPort {

    @Override
    public UserDto loadUserInfo(String username) {
        return null;
    }
}

