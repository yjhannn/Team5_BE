package ojosama.talkak.category.domain;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CategoryType {
    FOOD("음식"), JOURNEY("여행"), GAME("게임"), MUSIC("음악"), SPORT("스포츠");

    private final String name;

    public static CategoryType fromName(String name) {
        return Arrays.stream(CategoryType.values())
            .filter(categoryType -> categoryType.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

}
