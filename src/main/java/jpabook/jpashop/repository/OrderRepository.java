package jpabook.jpashop.repository;

import jpabook.jpashop.api.SimpleOrderQueryDto;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    //--order생성--//
    public void save(Order order) {
        em.persist(order);
    }

    //--order단건조회--//
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    //--order 목록 조회--//
    public List<Order> findAll() {
        return em.createQuery("select o from Order o", Order.class)
                .getResultList();
    }

    //fetch 조인
    //이 경우에는 Lazy로 되어있어도 이메소드는 진짜 객체의 값을 다가져와준다!
    public List<Order> findAllWithDelivery() {

        return em.createQuery(
                "select o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class)
                .getResultList();
    }



    public List<SimpleOrderQueryDto> findOrderDTO() {
        return em.createQuery(   //생성자에 엔티티를 그냥 넘겨버리면 식별자로 알기 때문에 밑작업을 해준다
                "select new jpabook.jpashop.api.SimpleOrderQueryDto(o.id,m.name,o.orderData,o.status,d.address) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d", SimpleOrderQueryDto.class
        ).getResultList();
    }



    //중요!! 결과적으로 1 : 다 fetch join에서는 페이징을 하면 안된다
    public  List<Order> findAllWithItem(){
        return em.createQuery(
                //식별자가 중복이 될수도있다.
                //디비에서 distinct를 사용 할 수 있지만 여기서 해주면
                //객체의 식별자id만이라도 중복이된다면 중복을 제거해준다. 여기서는 orderId
                "select distinct o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d " +
                        "join fetch  o.orderItems oi " +
                        "join fetch oi.item i ", Order.class)
                .setFirstResult(1)
                .setMaxResults(100) /*몇 번째 부터 몇 개 가져와/ /*여기서는 페이징 못한다.*/
                .getResultList();
                //중요!! 결과적으로 1 : 다 fetch join에서는 페이징을 하면 안된다
                //중요!! 컬렉션 fetch join은 1개만 사용하자 2개이상은 비 추천

    }



    //public List<Order> findAll(OrderSearch orderSearch){}
    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }



}