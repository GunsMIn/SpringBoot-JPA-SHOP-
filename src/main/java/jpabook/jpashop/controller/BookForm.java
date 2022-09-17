package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class BookForm {

    private Long id; // 나중에 수정까지 보여주려고 id를 넣은 것 같다

    @NotEmpty(message = "상품명을 필수로 입력하여주세요")
    private String name;

    private int price;

    private int stockQuantity;

    private String author;

    private String isbn;
}
