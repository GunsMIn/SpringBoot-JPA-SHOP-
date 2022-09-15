package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyMemberRepository {

    @PersistenceContext
    private final EntityManager em;

    //회원저장
    public void save(Member member){
        em.persist(member);
    }

    //회원조회
    public Member findOne(Long id){
        return em.find(Member.class, id); // em.persist(타입,pk)
    }

    //회원 목록조회
    public List<Member> findList(){
       return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }


}
