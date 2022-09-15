package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyMemberService {

    private final MemberRepository memberRepository;

    //회원 가입
    public Long join(Member member){
        duplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void duplicateMember(Member member) {
        List<Member> memberByName = memberRepository.findByName(member.getName());
        if(!memberByName.isEmpty()){
            throw new IllegalArgumentException("이미 가입된 회원입니다");
        }
    }
}
