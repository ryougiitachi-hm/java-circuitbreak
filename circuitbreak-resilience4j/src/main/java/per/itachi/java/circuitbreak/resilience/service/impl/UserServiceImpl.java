package per.itachi.java.circuitbreak.resilience.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.itachi.java.circuitbreak.resilience.adapter.RedisPort;
import per.itachi.java.circuitbreak.resilience.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisPort redisPort;

    @Override
    public void showUserInfo(String username) {
        redisPort.loadUserInfo(username);
    }
}
