package ojosama.talkak.member.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ojosama.talkak.comment.domain.Comment;
import ojosama.talkak.common.entity.BaseEntity;
import ojosama.talkak.common.exception.TalKakException;
import ojosama.talkak.common.exception.code.MemberError;

@Entity
@Table(name = "member")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    private String username;
    private String imageUrl;
    @Column(unique = true)
    private String email;
    private Boolean gender = true;
    @Enumerated(EnumType.STRING)
    private Age age = Age.TEN;
    @Enumerated(EnumType.STRING)
    private MembershipTier membership = MembershipTier.Basic;
    private Integer point = 0;
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;


    private Member(String username, String imageUrl, String email) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.email = email;
    }

    public static Member of(String username, String imageUrl, String email) {
        return new Member(username, imageUrl, email);
    }

    public void createMemberInfo(String gender, String age) {
        checkMemberInfoInputs(gender, age);
        Age newAge = Age.fromName(age);
        if (!gender.matches("남자|여자") || newAge == null) {
            throw TalKakException.of(MemberError.ERROR_UPDATE_MEMBER_INFO);
        }

        this.gender = gender.equals("남자");
        this.age = newAge;
    }

    public void updateMemberInfo(String gender, String age) {
        checkMemberInfoInputs(gender, age);
        Age newAge = Age.fromName(age);
        if (!gender.matches("남자|여자") || newAge == null) {
            throw TalKakException.of(MemberError.ERROR_UPDATE_MEMBER_INFO);
        }

        if(this.gender != gender.equals("남자")) {
            this.gender = !this.gender;
        }
        this.age = newAge;
    }

    private static void checkMemberInfoInputs(String gender, String age) {
        if (gender == null || age == null) {
            throw TalKakException.of(MemberError.ERROR_UPDATE_MEMBER_INFO);
        }
    }

    public String convertGenderToString() {
        return gender ? "남자" : "여자";
    }
}
