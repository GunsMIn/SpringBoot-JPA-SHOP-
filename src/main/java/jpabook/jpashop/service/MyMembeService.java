package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MyMembeService {

    private final MemberRepository memberRepository;


    //회원가입 서비스
    public Long join(Member member) {
        //동명이인 x
        validateName(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateName(Member member) {
        List<Member> findByNameMember = memberRepository.findByName(member.getName());
        if (!findByNameMember.isEmpty()) {
            throw new IllegalStateException("동명이인");
        }
    }


    //회원조회
    @Transactional(readOnly = true)
    public Member findMember(Long id) {
        Member findember = memberRepository.findOne(id);
        return findember;
    }

    @Transactional(readOnly = true)
    public List<Member> findMemberList() {
        return memberRepository.findAll();
    }

    //update서비스
    @Transactional
    public void updateMember(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }



}
