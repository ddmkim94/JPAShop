package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 테스트가 종료되면 데이터 rollback (테스트 클래스에서만 이렇게 동작)
class MemberServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    // @PersistenceContext EntityManager em;

    @Test
    // @Rollback(value = false) // 테스트 후 commit -> INSERT 날라감!
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("박은빈");
        member.setAddress(new Address("seoul", "baker.St", "21"));

        // when
        Long saveId = memberService.join(member);
        // em.flush(); INSERT문만 보고 rollback을 하고 싶은 경우에 사용

        // then
        Assertions.assertThat(member).isEqualTo(memberRepository.findOne(saveId));
    }

    @Test
    public void 중복_회원_조회() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("member");

        Member member2 = new Member();
        member2.setName("member");
        
        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }
}