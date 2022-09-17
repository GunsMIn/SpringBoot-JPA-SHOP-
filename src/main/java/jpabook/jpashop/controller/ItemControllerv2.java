/*
package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemControllerv2 {

    private final ItemService itemService;


    @GetMapping("items/new")
    public String itemForm(@ModelAttribute(name = "form") BookForm form) {
        return "items/createItemForm";
    }

    @PostMapping("items/new")
    public String register(@Validated @ModelAttribute(name="form") BookForm form, BindingResult result) {

        log.info("BookForm={}",form);

        if(result.hasErrors()){
            return "items/createItemForm";
        }
        Book item = new Book();
        item.setName(form.getName());
        item.setStockQuantity(form.getStockQuantity());
        item.setPrice(form.getPrice());
        item.setIsbn(form.getIsbn());
        item.setAuthor(form.getAuthor());

        itemService.saveItem(item);

        return "redirect:/items";
    }

 */
/*   @GetMapping("/items")
    public String itemList(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }*//*


    @GetMapping("/items")
    public String list(Model model){ // 디비에서 상품 목록들을 조회해서 화면에 뿌려준 페이지로 이동하는 컨트롤러
        List<Item> items = itemService.findItems();
        model.addAttribute("items",items);
        return "items/itemList";
    }


    @GetMapping("/items/{id}/edit")
    public String editForm(@PathVariable Long id,Model model) {
        Book item = (Book) itemService.findOne(id);

        BookForm form = new BookForm();

        form.setId(item.getId());
        form.setName(item.getName());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        form.setPrice(item.getPrice());

        model.addAttribute("form", form);
        return "/items/updateForm";
    }
}
*/
