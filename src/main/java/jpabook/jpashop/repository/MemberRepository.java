package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor // 이것때문에 repository에서도 생성자주입으로 EntityManager를 넣어줄 수 있다.
public class MemberRepository {

    //@PersistenceContext
    private final EntityManager em;



    //디비 회원저장 로직
    public void save(Member member){
        em.persist(member);
    }//우선 영속성 컨텍스트에 회원객체를 넣고
    //transaction이 commit되는 시점에 디비에 insert한다

    //디비 회원 조회 로직
    public  Member findOne(Long id){
        return em.find(Member.class,id); // em.find(타입,pk)
    }
    //회원 목록 조회
    public List<Member> findAll(){
                                //첫번째가 JPQL 두번째가 반환타입
      return  em.createQuery("select m from Member m", Member.class) // from의 대상은 테이블이 아니고 엔티티이다
                .getResultList();
    }

    //이름으로 회원 목록 조회
    public List<Member> findByName(String name){
      return  em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
