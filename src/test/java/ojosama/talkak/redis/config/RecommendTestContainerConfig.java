package ojosama.talkak.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Slf4j
public class RecommendTestContainerConfig implements BeforeAllCallback {

    private static final String FASTAPI_IMAGE = "ojosama_repo-fastapi-app:latest";
    private static final int FASTAPI_PORT = 8000;
    private static final int REDIS_PORT = 6379;
    private static final GenericContainer<?> fastApiContainer;

    static {
        // FastAPI 컨테이너 설정
        fastApiContainer = new GenericContainer<>(FASTAPI_IMAGE)
            .withNetwork(RedisTestContainerConfig.getNetwork())  // Redis와 같은 네트워크 사용
            .withExposedPorts(FASTAPI_PORT)
            .withEnv("REDIS_HOST", "redis-test")  // Redis 컨테이너의 네트워크 별칭
            .withEnv("REDIS_PORT", String.valueOf(REDIS_PORT))
            .withLogConsumer(outputFrame -> {
                log.debug("FastAPI: {}", outputFrame.getUtf8String());
            });

        // FastAPI 컨테이너 시작
        fastApiContainer.start();


        log.debug("FastAPI container started - Host: {}, Port: {}",
            fastApiContainer.getHost(),
            fastApiContainer.getMappedPort(FASTAPI_PORT));
    }

    @Override
    public void beforeAll(ExtensionContext context) {
    }


    public static String getHost() {
        return fastApiContainer.getHost();
    }

    public static Integer getPort() {
        return fastApiContainer.getMappedPort(FASTAPI_PORT);
    }
}
