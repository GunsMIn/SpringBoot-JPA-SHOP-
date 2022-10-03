package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController  // == @Controller + @ReseponseBody
@RequiredArgsConstructor
public class MemberApiController {

    //생성자 주입
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){

        return memberService.findMembers();
        //이렇게 제작시에 Member는 Order와 연관관계과 되어있기때문데
        //@JsonIgnore을 사용해서 api응답시 제외시켜주었다(비추천)
    }

    @GetMapping("/api/v2/members")
    public Result membersV2(){
        List<Member> members = memberService.findMembers();
        List<MemberDto> memberDtoList = members.stream().map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(memberDtoList,memberDtoList.size());
    }


    ///////////조회 DTO////////////
    //장점 : 내가 노출하고싶은것만 노출 시킬 수 있다.
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
        private int count;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }
    ///////////////////////////////

    //회원 등록 api
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        //바로 윗줄에서의 문제는 API에 맞춰서 별도의 엔티티르 만드는 것이 좋다
        Long id = memberService.join(member);
        return new CreateMemberResponse(id); // @RestController때문에 @ReseponseBody가 있다.
    }

    /*CreateMemberRequest(DTO) 를 Member 엔티티 대신에 RequestBody와 매핑한다.
    엔티티와 프레젠테이션 계층을 위한 로직을 분리할 수 있다.
    엔티티와 API 스펙을 명확하게 분리할 수 있다.
    엔티티가 변해도 API 스펙이 변하지 않는다.
*/
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
                                                                //별도의 객체를 만들어준다
        //큰 깨달음!!!!!!!!!!!!!! dto를 직접만들어서 해당하는 필드의 값을 Member에 다시 넣어주자!
        //request가 바로  memberService.join() 못들어가니까
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    //회원 등록 api 객체
    @Data
    static class CreateMemberRequest{
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    //수정 API
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMemberRequest request
    ){
        memberService.update(id,request.getName()); // 여기서 이미 영속성 컨텍스트에있는 엔티티를 바꿔줌
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(),findMember.getName());
    }

    //나의 수정
    @PutMapping("/api/{id}/update")
    public UpdateMemberResponse updateUser(@RequestBody UpdateMemberRequest request, @PathVariable Long id){
        memberService.update(id,request.getName());
        Member one = memberService.findOne(id);
        return new UpdateMemberResponse(one.getId(), one.getName());
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }






    ///////////////////////////////////////////////////////////////////////////////////////////
}
