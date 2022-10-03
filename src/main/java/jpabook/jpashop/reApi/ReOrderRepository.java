package jpabook.jpashop.reApi;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReOrderRepository {
    private final EntityManager em;


    public List<Order> getOrders(int offset, int limit) {
         return em.createQuery("select o from Order o " +
                "join fetch o.member " +
                "join fetch o.delivery ")
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
    }

}
