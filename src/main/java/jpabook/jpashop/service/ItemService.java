package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 조회 서비스 로직이 많을 때 이렇게 쓰면 편리하다.
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // 클래스위의 어노테이션이 @Transactional(readOnly = true) 조회용의 성능 최적화라 쓰기용도는 꼭 붙여주자
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    //중요!!(변경 감지 기능) 수정 방법
    @Transactional          //트랜잭션이 있는 서비스 계층에 식별자( id )와 변경할 데이터를 명확하게 전달하자
    public void updateItem(Long itemId, String name ,int price, int stockQuantity){
        //28번째줄이 키포인트다 영속성컨텍스트로 뽑아와야한다. 28번째줄의 item은 영속성컨텍스트의 객체이다
        Item item = itemRepository.findOne(itemId); // 실제 영속성 컨텍스트에 있는 애를 찾은거다.
        item.setPrice(price);
        item.setName(name);
        item.setStockQuantity(stockQuantity);

        //dirty checking
        //영속엔티티의 값을 다시 셋팅 해줬으면 변경이 감지 되었다. transcation이 flush를 날려서 변경된 애를 다 찾아준다.
        //찾아서 업데이트쿼리를 날려서 업데이트를 알아서해준다.

        //영속성 컨텍스트에서 엔티티를 다시 {조회}한 후에 데이터를 수정하는 방법 == 변경 감지 기능
        //트랜잭션 안에서 엔티티를 다시 조회, 변경할 값 선택 트랜잭션 커밋 시점에 변경 감지(Dirty Checking)
        //이 동작해서 데이터베이스에 UPDATE SQL 실행
    }



    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }

}
