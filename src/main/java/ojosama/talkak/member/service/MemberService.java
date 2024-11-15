package ojosama.talkak.member.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ojosama.talkak.category.domain.Category;
import ojosama.talkak.category.domain.CategoryType;
import ojosama.talkak.category.domain.MemberCategory;
import ojosama.talkak.category.repository.CategoryRepository;
import ojosama.talkak.category.repository.MemberCategoryRepository;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.common.exception.code.CategoryError;
import ojosama.talkak.common.exception.code.MemberError;
import ojosama.talkak.member.domain.Member;
import ojosama.talkak.member.dto.AdditionalInfoRequest;
import ojosama.talkak.member.dto.AdditionalInfoResponse;
import ojosama.talkak.member.dto.MyPageInfoRequest;
import ojosama.talkak.member.dto.MyPageInfoResponse;
import ojosama.talkak.member.dto.ProfileResponse;
import ojosama.talkak.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberCategoryRepository memberCategoryRepository;
    private final CategoryRepository categoryRepository;


    public MemberService(MemberRepository memberRepository,
        MemberCategoryRepository memberCategoryRepository, CategoryRepository categoryRepository) {
        this.memberRepository = memberRepository;
        this.memberCategoryRepository = memberCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public MyPageInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> TalKakException.of(MemberError.NOT_EXISTING_MEMBER));
        List<Category> favoriteCategories = memberCategoryRepository.findAllCategoriesByMember(
            memberId);

        return MyPageInfoResponse.of(member, favoriteCategories);
    }

    @Transactional
    public AdditionalInfoResponse updateAdditionalInfo(Long memberId,
        AdditionalInfoRequest request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> TalKakException.of(MemberError.NOT_EXISTING_MEMBER));
        List<Category> categories = new ArrayList<>();

        member.createMemberInfo(request.gender(), request.age());

        request.categories()
            .stream()
            .map(CategoryType::fromName)
            .forEach(categoryType -> {
                Category category = categoryRepository.findByCategoryType(categoryType)
                    .orElseThrow(() -> TalKakException.of(CategoryError.NOT_EXISTING_CATEGORY));
                categories.add(category);
                memberCategoryRepository.save(MemberCategory.of(member, category));
            });

        return AdditionalInfoResponse.of(categories, request);
    }

    @Transactional
    public MyPageInfoResponse updateMemberInfo(Long memberId, MyPageInfoRequest request) {
        // 유저 성별, 나이 검증
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> TalKakException.of(MemberError.NOT_EXISTING_MEMBER));
        member.updateMemberInfo(request.gender(), request.age());

        // 1.유효한 카테고리 입력인지 사전 검증(카테고리 허용 개수와 일치하는지, 서로 다른 카테고리인지, 각각 존재하는 카테고리인지?)
        Set<Long> newDistinctCategoryIds = validateCategoryInputs(request);
        // 2. 새로 변경되는 카테고리 리스트에 존재하지 않는 기존 카테고리를 리스트에서 삭제
        List<MemberCategory> memberCategories = dropNotIncludedCategories(memberId, request);
        // 3. 새롭게 추가되는 카테고리가 무엇인지 찾고, 새롭게 추가되는 카테고리를 리스트에 추가
        addNewlyIncludedCategories(newDistinctCategoryIds, memberCategories, member);

        return MyPageInfoResponse.of(member,
            memberCategories.stream().map(MemberCategory::getCategory).toList());
    }

    public ProfileResponse getProfile(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> TalKakException.of(MemberError.NOT_EXISTING_MEMBER));
        return ProfileResponse.of(
            member.getId(),
            member.getEmail(),
            member.getUsername(),
            member.getImageUrl()
        );
    }

    private void addNewlyIncludedCategories(Set<Long> newDistinctCategoryIds,
        List<MemberCategory> memberCategories,
        Member member) {
        List<Long> newCategoryIds = newDistinctCategoryIds.stream()
            .filter(
                c -> memberCategories.stream().noneMatch(
                    mc -> mc.getCategory().getId().equals(c)
                )
            ).toList();
        List<MemberCategory> newMemberCategories = categoryRepository.findAllByCategoryIds(
                newCategoryIds)
            .stream()
            .map(c -> memberCategoryRepository.save(MemberCategory.of(member, c)))
            .toList();
        memberCategories.addAll(newMemberCategories);
    }

    private List<MemberCategory> dropNotIncludedCategories(Long memberId,
        MyPageInfoRequest request) {
        List<MemberCategory> memberCategories = memberCategoryRepository.findAllByMemberId(
            memberId);
        // 삭제해야 할 카테고리들을 찾습니다
        List<MemberCategory> categoriesToDelete = memberCategories.stream()
            .filter(mc -> request.categories().stream()
                .noneMatch(c -> c.equals(mc.getCategory().getId())))
            .toList();

        if (!categoriesToDelete.isEmpty()) {
            memberCategoryRepository.deleteAll(categoriesToDelete);
        }

        memberCategories.removeAll(categoriesToDelete);
        return memberCategories;
    }

    private Set<Long> validateCategoryInputs(MyPageInfoRequest request) {
        Set<Long> newDistinctCategoryIds = categoryRepository.findExistingIds(
            new HashSet<>(request.categories()));
        Category.validateCategoryInputs(newDistinctCategoryIds);
        return newDistinctCategoryIds;
    }
}
