package ojosama.talkak.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKeyUtil {
    REFRESH_TOKEN("refresh_token:%d");

    private final String format;

    public String of(Long id) {
        return String.format(format, id);
    }
}
