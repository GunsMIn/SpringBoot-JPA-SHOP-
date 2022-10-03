package jpabook.jpashop.reApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class SaveMemberDTO {
    private String name;

    public SaveMemberDTO() {
    }

    public SaveMemberDTO(String name) {
        this.name = name;
    }
}
