package per.itachi.java.circuitbreak.hystrix.redis;

public interface RedisPort {

    String loadUserInfo(String username);

    void updateUserInfo(String username);
}
