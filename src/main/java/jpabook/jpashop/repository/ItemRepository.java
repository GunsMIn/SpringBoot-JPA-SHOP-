package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    //상품 저장
    public void save(Item item){
        /*save()
        id 가 없으면 신규로 보고 persist() 실행 -> id가 없다는 것은 완전히 새로 생성하는 객체
        id 가 있으면 이미 데이터베이스에 저장된 엔티티를 수정한다고 보고, merge() 를 실행 ->update랑 비슷*/
        if(item.getId() == null){//jpa에 저장하기 전까지는 id값이 없다
            em.persist(item);
        }else{ // 이것의 뜻은 이미 기존에 식별자가 존재한다면 준영속 엔티티로 보고
            em.merge(item);
        }
    }

    //아이템 하나 조회
    public Item findOne(Long id){
        return em.find(Item.class, id); // em.find(타입,pk)
    }

    //아이템 목록 조회
    public List<Item> findAll(){
      return  em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }



}
