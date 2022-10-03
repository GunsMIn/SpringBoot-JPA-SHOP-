package jpabook.jpashop.reApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SaveResponseDTO {
    private Long id;

    public SaveResponseDTO() {
    }

    public SaveResponseDTO(Long id) {
        this.id = id;
    }
}
