package jpabook.jpashop.api;


import com.sun.xml.bind.v2.model.core.ID;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MyMEMBERAPIcontroller {

    private final MemberService memberService;


    @GetMapping("/Myapi/v1/members")
    public List<Member> memberList() {
        List<Member> members = memberService.findMembers();
        return members;
    }


    @GetMapping("/Myapi/v2/members")
    public Result memberListV2(){
        List<Member> members = memberService.findMembers();
        List<MemberDto> MemberDtoList = members.stream().map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(MemberDtoList);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name; // 이름만 뽑아오기위해서
    }


    //등록 api
    @PostMapping("/Myapi/v1/members")
    public CreateMemberResponse saveMember(@Valid @RequestBody Member member, BindingResult result) {

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest{
        private String name;
    }


    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }


    //등록 api
    @PostMapping("/Myapi/v2/members")
    public CreateMemberResponse saveMemberV2(@Valid @RequestBody CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }


    @PutMapping("/Myapi/v1/members/{id}")
    public UpdateResponse update(@RequestBody UpdateRequest request, @PathVariable Long id) {
        memberService.update(id,request.getName());
        //여기서 이미 바뀜
        Member member =  memberService.findOne(id);
        return new UpdateResponse(member.getName(),member.getId());
    }


    @Data
    static class UpdateRequest{
        private String name;
    }


    @Data
    @AllArgsConstructor
    static class UpdateResponse{
        private String name;
        private Long id;
    }

}
