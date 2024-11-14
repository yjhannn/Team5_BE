package ojosama.talkak.member.service;

import static ojosama.talkak.common.exception.ExceptionAssertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import ojosama.talkak.category.domain.Category;
import ojosama.talkak.category.domain.CategoryType;
import ojosama.talkak.category.domain.MemberCategory;
import ojosama.talkak.category.repository.CategoryRepository;
import ojosama.talkak.category.repository.MemberCategoryRepository;
import ojosama.talkak.common.exception.ExceptionAssertions;
import ojosama.talkak.common.exception.code.MemberError;
import ojosama.talkak.member.domain.Age;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.member.domain.MembershipTier;
import ojosama.talkak.member.dto.CategoryResponse;
import ojosama.talkak.member.dto.MyPageInfoRequest;
import ojosama.talkak.member.dto.MyPageInfoResponse;
import ojosama.talkak.member.repository.MemberRepository;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(MemberService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MyPageTest {

    @Autowired
    private MemberService memberService;

    static List<Category> categories;
    static Member member;
    static List<Long> categoryIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
    static List<String> genders = Arrays.asList("남자", "여자");
    static List<String> ages = Arrays.asList("10대", "20대", "30대", "40대", "50대 이상");

    @BeforeAll
    static void before(@Autowired MemberRepository memberRepository,
        @Autowired CategoryRepository categoryRepository,
        @Autowired MemberCategoryRepository memberCategoryRepository) {  // static 키워드 없이도 사용 가능
        member = memberRepository.save(demoMember());
        categories = Arrays.stream(CategoryType.values())
            .map(ct -> categoryRepository.save(new Category(ct)))
            .toList();

        List<Category> preCategories = new ArrayList<>();
        preCategories.add(categories.get(0));
        preCategories.add(categories.get(1));
        preCategories.add(categories.get(2));

        List<MemberCategory> memberCategories = preCategories.stream()
            .map(c -> MemberCategory.of(member, c))
            .limit(3)
            .toList();

        memberCategoryRepository.saveAll(memberCategories);
    }

    @DisplayName("마이페이지 수정 성공 테스트")
    @ParameterizedTest
    @MethodSource("succeedParameters")
    @Transactional
    void success_myPage_info(String gender, String age, List<Long> categories) {
        //given
        MyPageInfoRequest request = new MyPageInfoRequest(gender, age, categories);
        System.out.println("request.categories().size() = " + request.categories().size());
        //when
        MyPageInfoResponse response = memberService.updateMemberInfo(member.getId(),
            request);
        //then
        assertThat(response.gender()).isEqualTo(gender);
        assertThat(response.age()).isEqualTo(age);
        assertThat(response.categories().stream().map(CategoryResponse::id))
            .containsAll(categories);
    }

    @DisplayName("마이페이지 수정 실패 테스트 - 성별 오류")
    @ParameterizedTest
    @ValueSource(strings = {"남성", "여성", "남", "여", "male", "female"})
    @Transactional
    void fail_myPage_info_gender(String gender) {
        MyPageInfoRequest request = new MyPageInfoRequest(gender, "10대", Arrays.asList(1L, 2L, 3L));
        assertErrorCode(MemberError.ERROR_UPDATE_MEMBER_INFO,
            () -> memberService.updateMemberInfo(member.getId(), request));
    }

    @DisplayName("마이페이지 수정 실패 테스트 - 나이 오류")
    @ParameterizedTest
    @ValueSource(strings = {"10세", "50대", "60대", "10", "50", "twenty"})
    @Transactional
    void fail_myPage_info_age(String age) {
        MyPageInfoRequest request = new MyPageInfoRequest("남자", age, Arrays.asList(1L, 2L, 3L));
        assertErrorCode(MemberError.ERROR_UPDATE_MEMBER_INFO,
            () -> memberService.updateMemberInfo(member.getId(), request));
    }

    @DisplayName("마이페이지 수정 실패 테스트 - 왼쪽 경계")
    @ParameterizedTest
    @MethodSource("failParametersLeft")
    @Transactional
    void fail_myPage_info_left(String gender, String age, List<Long> categories) {
        //given
        MyPageInfoRequest request = new MyPageInfoRequest(gender, age, categories);
        System.out.println("request.categories().size() = " + request.categories().size());
        //when
        assertErrorCode(MemberError.ERROR_UPDATE_MEMBER_INFO,
            () -> memberService.updateMemberInfo(member.getId(),
                request));
    }

    @DisplayName("마이페이지 수정 실패 테스트 - 오른쪽 경계")
    @ParameterizedTest
    @MethodSource("failParametersRight")
    @Transactional
    void fail_myPage_info_right(String gender, String age, List<Long> categories) {
        //given
        MyPageInfoRequest request = new MyPageInfoRequest(gender, age, categories);
        System.out.println("request.categories().size() = " + request.categories().size());
        //when
        assertErrorCode(MemberError.ERROR_UPDATE_MEMBER_INFO,
            () -> memberService.updateMemberInfo(member.getId(),
                request));
    }

    private static Stream<Arguments> succeedParameters() {
        return provideMyPageTestParameters(3);
    }

    private static Stream<Arguments> failParametersLeft() {
        return provideMyPageTestParameters(2);
    }

    private static Stream<Arguments> failParametersRight() {
        return provideMyPageTestParameters(4);
    }

    private static Stream<Arguments> provideMyPageTestParameters(int cnt) {
        // Apache Commons Collections의 PermutationIterator 사용
        Iterator<List<Long>> categoryPermutations = new PermutationIterator<>(categoryIds);
        List<List<Long>> categoryGroups = new ArrayList<>();

        while (categoryPermutations.hasNext()) {
            List<Long> perm = categoryPermutations.next();
            categoryGroups.add(perm.subList(0, cnt));  // 앞의 3개만 선택
        }

        return genders.stream()
            .flatMap(gender -> ages.stream()
                .flatMap(age -> categoryGroups.stream()
                    .map(categories -> Arguments.of(gender, age, categories))));
    }

    private static Member demoMember() {
        return new Member("철수 김", "https://",
            "abc123@a.com", true, Age.TWENTY, MembershipTier.Basic, 0, new ArrayList<>(), true);
    }
}
