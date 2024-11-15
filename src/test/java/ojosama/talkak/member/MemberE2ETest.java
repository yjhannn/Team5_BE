package ojosama.talkak.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Stream;
import ojosama.talkak.annotation.WithMockMember;
import ojosama.talkak.category.domain.CategoryType;
import ojosama.talkak.category.repository.CategoryRepository;
import ojosama.talkak.member.domain.Age;
import ojosama.talkak.member.dto.AdditionalInfoRequest;
import ojosama.talkak.member.dto.AdditionalInfoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MemberE2ETest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    private static final String MALE = "남자";
    private static final String FEMALE = "여자";
    private static final List<String> validCategories = List.of(
        CategoryType.JOURNEY.getName(),
        CategoryType.SPORT.getName(),
        CategoryType.MUSIC.getName()
    );

    @Test
    @WithMockMember
    @DisplayName("추가 정보 입력 정상 테스트")
    void additionalInfoTestWithValidInput() throws Exception {
        // given
        AdditionalInfoRequest request = new AdditionalInfoRequest(
            validCategories,
            Age.TWENTY.getName(),
            FEMALE
        );
        String body = objectMapper.writeValueAsString(request);

        // when
        String expected = objectMapper.writeValueAsString(
            AdditionalInfoResponse.of(
                validCategories.stream()
                    .map(CategoryType::fromName)
                    .map(categoryType -> categoryRepository.findByCategoryType(categoryType).get())
                    .toList(),
                request
            )
        );

        // then
        mvc.perform(
                patch("/api/additional-info")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isOk())
            .andExpect(content().json(expected));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("abnormalAdditionalInfoRequestProvider")
    @WithMockMember
    @DisplayName("추가 정보 입력 예외 테스트")
    void additionalInfoTestWithInvalidInput(AdditionalInfoRequest request) throws Exception {
        // given
        String body = objectMapper.writeValueAsString(request);

        // when
        // then
        mvc.perform(
                patch("/api/additional-info")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isBadRequest());
    }

    static Stream<AdditionalInfoRequest> abnormalAdditionalInfoRequestProvider() {
        return Stream.of(
            new AdditionalInfoRequest(
                List.of(CategoryType.GAME.getName(), CategoryType.FOOD.getName()),
                Age.TEN.getName(),
                MALE
            ),
            new AdditionalInfoRequest(
                validCategories,
                "wrong age",
                FEMALE
            ),
            new AdditionalInfoRequest(
                validCategories,
                Age.FIFTY.getName(),
                "wrong gender"
            )
        );
    }
}
