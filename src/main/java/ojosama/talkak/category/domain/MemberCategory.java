package ojosama.talkak.category.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.common.exception.code.MemberError;
import ojosama.talkak.member.domain.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "member_category")
public class MemberCategory {

    @EmbeddedId
    private MemberCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, insertable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, insertable = false, updatable = false)
    private Category category;

    private MemberCategory(Member member, Category category) {
        this.id = new MemberCategoryId(member.getId(), category.getId());
        this.member = member;
        this.category = category;
    }

    public static MemberCategory of(Member member, Category category) {
        return new MemberCategory(member, category);
    }

    public static void isValidCategories(List<Long> categories) {
        if(categories.size() != 3) {
            throw TalKakException.of(MemberError.ERROR_UPDATE_MEMBER_INFO);
        }
    }
}
