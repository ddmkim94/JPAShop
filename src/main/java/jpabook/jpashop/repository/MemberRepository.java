package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository // spring-boot가 component scan을 통해 해당 엔티티를 조회
public class MemberRepository {

    @PersistenceContext // Create EntityManager
    private EntityManager em;

    /* => EntityManagerFactory Injection!
    @PersistenceUnit
    private EntityManagerFactory emf;
    */

    // 회원 저장
    public void save(Member member) {
        em.persist(member); // member 객체를 영속성 컨텍스트에서 관리
        // member.getId();
    }

    // 특정 회원 조회(ID)
    public Member findOne(Long id) {
        return em.find(Member.class, id); // 식별자를 통해서 해당 회원을 검색
    }

    // 전체 회원 조회
    public List<Member> findAll() {
        // createQuery: JPQL을 사용해서 DB를 검색할 때 사용하는 Method
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // 특정 회원 조회(NAME)
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name).getResultList();
    }
}
