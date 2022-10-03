package jpabook.jpashop.reApi;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.reApi.DTO.SaveMemberDTO;
import jpabook.jpashop.reApi.DTO.SaveResponseDTO;
import jpabook.jpashop.reApi.DTO.UpdateMemberDTO;
import jpabook.jpashop.reApi.DTO.UpdateMemberResonse;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.MyMembeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GeneratorType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class ReMemberApi {

    private final MyMembeService memberService;
    //등록
    @PostMapping("/register")
    public SaveResponseDTO saveMember(@RequestBody SaveMemberDTO saveMemberDTO) {
        Member member = new Member();
        member.setName(saveMemberDTO.getName());
        Long id = memberService.join(member);
        return new SaveResponseDTO(id);
    }

    @PatchMapping("{id}/update")
    public UpdateMemberResonse updateMember(@RequestBody UpdateMemberDTO memberDTO,@PathVariable Long id) {
        memberService.updateMember(id,memberDTO.getName());
        Member member = memberService.findMember(id);
        return new UpdateMemberResonse(member.getId(),member.getName());
    }



    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
        private int count;
    }

    @Data
    @AllArgsConstructor
    static class MemberListDTO{
        private Long id;
        private String name;
    }


    @GetMapping
    public Result getMembers() {
        List<Member> memberList = memberService.findMemberList();
        List<MemberListDTO> collect = memberList.stream().map(m -> new MemberListDTO(m.getId(), m.getName()))
                .collect(Collectors.toList());
        return new  Result(collect,collect.size());
    }


}
