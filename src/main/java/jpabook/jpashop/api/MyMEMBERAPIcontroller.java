package jpabook.jpashop.api;


import com.sun.xml.bind.v2.model.core.ID;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MyMEMBERAPIcontroller {


    private final MemberService memberService;

    @PostMapping("/api/members")
    public CreateMemverResponse createMemver(@RequestBody CreateMemver memver) {

        Member member = new Member();

        member.setName(memver.getName());

        Long id = memberService.join(member);

        return new CreateMemverResponse(member.getName(), member.getId());

    }


    @Data
    static class CreateMemver{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class CreateMemverResponse{
        private String name;
        private Long id;

    }


    @PatchMapping("/api/memvers/{id}/update")
    public UpdateMemversResponse update(@PathVariable Long id, @RequestBody UpdateMemverRequest request) {

        memberService.updateV2(id,request.getName());
        Member one = memberService.findOne(id);
        return new UpdateMemversResponse(one.getId(), one.getName());
    }

    @Data
    static class UpdateMemverRequest{
       private  String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemversResponse{
        private Long id;
        private String name;
    }



    @GetMapping("/api/memversList")
    public Result getList() {
        List<Member> members = memberService.findMembers();
        List<MemberListResponse> memberDtos = members.stream().map(m -> new MemberListResponse(m.getName()))
                .collect(Collectors.toList());

        return new Result(memberDtos,memberDtos.size());
    }

    @Data
    @AllArgsConstructor
    static class MemberListResponse{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
        private int count;
    }

}
