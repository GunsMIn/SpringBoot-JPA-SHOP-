package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController  // == @Controller + @ReseponseBody
@RequiredArgsConstructor
public class MemberApiControllers {

    private final MemberService memberService;

  //회원 등록 aoi
    @Data
    static class RequestMember{
       @NotEmpty
        private String name;
    }

    @Data
    static class ResponseMember{
        private Long id;

        public ResponseMember(Long id) {
            this.id = id;
        }
    }

    @PostMapping("my/member")
    public ResponseMember insertMember(@RequestBody @Valid  RequestMember requestMember){
        Member member = new Member();
        member.setName(requestMember.getName());
        Long id = memberService.join(member);
        return new ResponseMember(id);
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    @Data
    @AllArgsConstructor
    static  class RequestupdateMember{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class ResponseUpdataMember{
        private Long id;
        private String name;
    }


    //회원 수정
    @PutMapping("/my/update/{id}")
    public ResponseUpdataMember update(
            @PathVariable Long id,
            @RequestBody RequestupdateMember member){
        memberService.update(id,member.getName());
        Member one = memberService.findOne(id);
        return new ResponseUpdataMember(one.getId(), one.getName());
    }



    //////////
    @Data
    @AllArgsConstructor
    static class MemberListDTO{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Results<S>{
        private S data;
    }

    @GetMapping("/my/membersList")
    public List<MemberListDTO> emmmsdd(){
        List<Member> members = memberService.findMembers();
        List<MemberListDTO> memberList = members.stream().map(m -> new MemberListDTO(m.getName()))
                .collect(Collectors.toList());
        return memberList;
    }
}
