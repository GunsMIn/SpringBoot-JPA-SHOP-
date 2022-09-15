package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MyItemRepository {

    @PersistenceContext
    private final EntityManager em;


    //저장
    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        }else{
            em.merge(item);
        }
    }
}
