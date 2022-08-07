package jpabook.jpashop.service;

import com.sun.xml.bind.v2.schemagen.xmlschema.AttrDecls;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional // 데이터의 변경이 있어야하기 때문에 필요하다. test단에서는 rollback을 해준다
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    //롤백상태지만 디비에 들어가는 것을 보고싶어
    @Autowired
    EntityManager em;


   @Test // @Rollback(false) 디비의 결과를 보고싶다면!
   public void 회원가입() throws Exception {
       //given
       Member member = new Member();
       member.setName("kim");

       //when
       Long saveId = memberService.join(member);

       //then
       em.flush(); //flush란 영속성 컨텍스트에있는 변경이나 등록내용을 디비에 반영하느것이다.
       Assertions.assertEquals(member,memberRepository.findOne(saveId));

   }


   @Test
   public void 중복_회원_검사사() throws Exception {
       //given
       Member member = new Member();
       member.setName("김건우");
       Member member2 = new Member();
       member2.setName("김건우");

       //when
       memberService.join(member);
       try {
           memberService.join(member2);//예외가 발생해야한다!!!!!!
       }catch (IllegalStateException e){
           return;
       }
       //then
       Assertions.fail("예외가 발생해야 한다");

   }





}