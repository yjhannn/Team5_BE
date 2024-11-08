package ojosama.talkak.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Slf4j
public class RedisTestContainerConfig implements BeforeAllCallback {

    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;
    private static final Network SHARED_NETWORK = Network.newNetwork();
    private static final GenericContainer<?> redisContainer;


    static {
        redisContainer = new GenericContainer<>(REDIS_IMAGE)
            .withNetwork(SHARED_NETWORK)
            .withNetworkAliases("redis-test")
            .withExposedPorts(REDIS_PORT);

        redisContainer.start();

        String host = redisContainer.getHost();
        Integer mappedPort = redisContainer.getMappedPort(REDIS_PORT);

        // System Properties 설정
        System.setProperty("spring.data.redis.host", host);
        System.setProperty("spring.data.redis.port", mappedPort.toString());

        log.debug("Redis TestContainer started at {}:{}", host, mappedPort);
    }


    @Override
    public void beforeAll(ExtensionContext context) {
        // 시스템 프로퍼티 설정 확인
        String host = System.getProperty("spring.data.redis.host");
        String port = System.getProperty("spring.data.redis.port");
        log.debug("Verified Redis properties - Host: {}, Port: {}", host, port);
    }

    public static String getHost() {
        return redisContainer.getHost();
    }

    public static Integer getPort() {
        return REDIS_PORT;
    }

    public static Network getNetwork() {
        return SHARED_NETWORK;
    }
}
