package per.itachi.java.circuitbreak.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import lombok.extern.slf4j.Slf4j;
import per.itachi.java.circuitbreak.hystrix.redis.RedisPort;

@Slf4j
public class GetUserRedisHystrixCommand extends HystrixCommand<String> {

    private RedisPort redisPort;

    private String username;

    public GetUserRedisHystrixCommand(RedisPort redisPort, String username) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("redisPort"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("loadUserInfo"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(10)//至少有10个请求，熔断器才进行错误率的计算
                        .withCircuitBreakerSleepWindowInMilliseconds(2000)//熔断器中断请求5秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(50)//错误率达到50开启熔断保护
                        .withExecutionTimeoutEnabled(true))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties
                        .Setter().withCoreSize(10)));
        this.redisPort = redisPort;
        this.username = username;
    }

    @Override
    protected String run() throws Exception {
        return redisPort.loadUserInfo(this.username);
    }

    @Override
    protected String getFallback() {
        return "error";
    }

}
