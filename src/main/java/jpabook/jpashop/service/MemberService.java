package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 중요! jpa의 모든 데이터변경은 트랜잭션안에서 실행되어야한다.데이터 변경이 있는 곳에서는 @Transactional이 필요하다
@RequiredArgsConstructor // 생성자 주입
public class MemberService {

    private final MemberRepository memberRepository;//생성자로 주입

    //회원 가입
    @Transactional // 읽기가 아닌 쓰기에서 readonly= true를 넣으면 절대 안된다!
    public Long join(Member member){
        validateDuplicateMember(member);//중복회원 검증 로직직
        memberRepository.save(member);
        return member.getId(); // 영속성 컨텍스트에 persist하면 엔티티의 키값인 @id붙은것(pk)은 들어가진다
    }

    private void validateDuplicateMember(Member member)  {
        //중복회원이 있다면 Exception터트릴려고
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    //회원 전체 조회
    //@Transactional(readOnly = true) // 이것을 주면 조회하는곳에서는 성능이 최적화 된다
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    //회원 한명 조회
    //@Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }


    //회원수정
    @Transactional
    public void update(Long id,String name){
        Member member = memberRepository.findOne(id); // 영속성 컨텍스트 꺼내온다
        member.setName(name);
        //변경감지로 수정
    }
}
