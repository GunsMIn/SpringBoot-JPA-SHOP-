package jpabook.jpashop.reApi.DTO;


import jpabook.jpashop.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderIemDTO {

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderIemDTO(OrderItem orderItem) {
        this.itemName = orderItem.getItem().getName();
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
    }
}
