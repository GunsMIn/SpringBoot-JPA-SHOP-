package jpabook.jpashop.controller;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
                            //@ModelAttribute MemberForm memberform

        model.addAttribute("memberForm",new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){ //@Valid를쓰면 저 클래스에있는 어노테이션사용가능

        //@Valid에서  오류가 있고 그 다음에 BindingResult가 있다면 오류가 담길 수 있다.
        if(result.hasErrors()){// 만약 바인딩된것에서 오류가있다면
            return "members/createMemberForm"; // 다시 돌려보내서 에러타임리프를 만나게되겠다
        }

        //글로벌 오류
        if(form.getCity().isEmpty() ||form.getStreet().isEmpty() || form.getZipcode().isEmpty()){
            result.reject("AddressFail","집주소를 정확하게 입력해주세요");
            return "members/createMemberForm";
        }

        //입베디드 값 타입에 넣어 주었다
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);
        memberService.join(member);

        return "redirect:/"; // 리다이렉트로 홈화면에
    }


    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

}
