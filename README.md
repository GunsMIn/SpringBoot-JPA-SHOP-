# SpringBoot-JPA-SHOP-
Spring boot 와 JPA로 제작한 쇼핑몰 
# ✨SPRING_STUDY
✔스프링 학습 관련 레파지토리입니다.✔
<hr>
<b>🎈학습 내용</b><br>
- 엔티티 제작 : 회원,주문,배송,상품,카테고리,주소 엔티티제작 / 연관관계 1:N , 1:1 , N:M 매핑<br>
- fetch 추가 : fetch를 LAZY상태로 변경 및 양뱡항 편의 메소드 추가<br>
- 서비스,레퍼지토리 추가 : 회원 엔티티에 관한 service / repository제작 및 item 비지니스 로직 , exception 제작<br>
- 서비스,레퍼지토리 추가2 : order, orderItem, item에 관한 repository / service 제작 <br>
- 컨트롤러, view 추가 : 메인페이지, 회원가입페이지, 회원목록 페이지추가 및 memberController 제작<br>
- last commit : 상품등록페이지, 상품주문페이지 제작 및 상품 등록,수정,삭제 컨틀롤러제작<br>

<b>- 엔티티의 성능 최적화 :</b><br>
1.모든 연관관계는 지연로딩으로 설정해주자!<br>
그 이유는 즉시로딩을 하면 예측이 굉장히 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다. 특히 JPQL을 실행할때 N+1 문제가 발생된다.<br>
연관된 엔티티를 함께 DB에서 조회해야 할 경우에는 fetch join을 이용한다.<br>
@XToOne(OneToOne, ManyToOne) 관계는 기본이 즉시로딩이므로 직접 지연로딩으로 설정해야 한다<br>

2.컬렉션은 필드에서 초기화하자 <br>
컬렉션은 필드에서 바로 초기화 하는 것이 안전하다.null 문제에서 안전하다.<br>
하이버네이트는 엔티티를 영속화 할 때, 컬랙션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다.<br>
만약 getOrders() 처럼 임의의 메서드에서 컬력션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있다.<br>
따라서 필드레벨에서 생성하는 것이 가장 안전하고, 코드도 간결하다.<br>

<b>- service, repository 개발 :</b><br>
1.엔티티가있는 model 단에서 비지니스 로직을 만들 수 있다면 만들자!<br>
도메인 주도 설계 엔티티자체에서 해결할 수 있을때는 엔티티안에 비지니스 로직을 만드는게 가장 응집도가있다.<br><br> 

<b>- controller,view 단 개발 :</b><br>
1.엔티티와 폼에 전송되는 객체를 철저하게 분리 시켜주는 것이 좋다.<br> 
그러기 위해서 form에서 데이터 전송에서 필요한 model을 만들어 주었고 validation까지 적용시켜주었다 . 글로벌오류는 controller에서<br> 
result.reject()메소드를 통하여 적용시켜주었다<br><br>
2.검색조건추가 , 수정(변경감지 방법 = dirtycheck)
OrderSearch를 만들어주어서 회원명과 주문상태로 검색하게 개발해주었다.<br> 
수정의 방법에서는 merge대신 변경감지기법(dirty check)을 사용하였다. 영속성 컨텍스트의 커밋 전에 객체의 정보가 변경된다면 jpa가 변경을 감지하여<br> 
update 쿼리가 필요없이 알아서 수정해준다. merger는 되도록이면 피하는게 좋은데 값을 안바꿔주면 null로 대체되기 때문이다.<br><br> 



<b>- api 개발 :</b><br>
1.기존의 엔티티와 api에서 사용되는 엔티티는 철저하게 분리해준다 <br> 
api를 개발을 완성하고 기존의 엔티티를 약간이라도 변경한다면 유지보수가 굉장히 힘들다. 반드시 엔티티를 새로만들어서 개발하자!<br> 
2.fetch join을 사용하여 엔티티안에 연관관계를 함께 조회할 수 있다.<br>
다만 주의해야 할 점은 @xxxToMany의 관계에서는 컬렉션을 조회할 때는 그 컬렉션의 엔티티또한 DTO로 변환해준다.<br>
3.컬렉션관계가 있는 엔티티를 조회할 때 페이징처리가 안되는 이슈 해결책!<br>
xxxToOne관계는 전부다 fetch join을 해주고 xxxToMany관계는 default_batch_fetch_size =? 를 사용해준다.<br>
이 옵션을 사용하면 컬렉션이나, 프록시 객체를 한꺼번에 설정한 size 만큼 IN 쿼리로 조회한다. 즉 페이징처리가 가능해진다

## 등록(@PostMapping 시 기존 엔티티와 분리)

![image](https://user-images.githubusercontent.com/104709432/199659304-a9d37c3b-4377-4cb8-ab8c-7ad3019b2752.png)
<summary>CreateMemberRequest 를 Member 엔티티 대신에 RequestBody와 매핑한다.</summary>
<summary><summary><summary>엔티티와 프레젠테이션 계층을 위한 로직을 분리할 수 있다.</summary>
<summary><summary>엔티티와 API 스펙을 명확하게 분리할 수 있다.</summary>
<summary>엔티티가 변해도 API 스펙이 변하지 않는다</summary>

## 수정(@PatchMapping,@PutMapping 시 기존 엔티티와 분리)
![image](https://user-images.githubusercontent.com/104709432/199659432-a05c5ed9-5ea4-4216-adde-7a94111d107f.png)
<summary>UpdateMemberRequest 를 Member 엔티티 대신에 RequestBody와 매핑한다.</summary>
<summary><summary>엔티티와 API 스펙을 명확하게 분리할 수 있다.</summary>
<summary>memberService.updateMember(id,updateMemberRequest)는 service단에서 dirty checking -> 변경감지 기법을 사용</summary>

<br><br>
<b>🎈학습에 대하여 느낌점</b><br>
jpa의 동작 방식에 대하여 알게되었다. 그리고 매핑의 종류와 각각의 매핑에대하여 더 상세하게 알게되었고<br>
줄 곧 myBatis로 프로젝트를 해온 나에게는 jpa란 정말 편리한 것 같다.<br>
또한 Rest API 방식으로 사용할 때 에는 제일 중요한 점은 기존의 엔티티를 그대로 사용하거나 노출하면 안된다는 점이다.<br>
따라서 기존의 엔티티를 그대로 사용하지 않기위해서 DTO를 만들어서 사용하자! 주의할점은 다 :1 관계에서는 DTO를 하나만 생각해도 되지만 컬렉션 관계인<br>
1:N관계에서는 또 다른 DTO를 만들어주어야한다
  <hr>
  <b>1.메인페이지</b><br>
<img width="80%" src="https://user-images.githubusercontent.com/104709432/179339446-7fecbd7b-eb72-4ba8-807f-a555fb7767a2.JPG"/>
  <b>2.회원등록</b><br>
<img width ="80%" src="https://user-images.githubusercontent.com/104709432/179339586-6189416e-53d3-434a-b78e-117189b01f42.JPG"/>
  <b>3.회원목록</b><br>
<img width ="80%" src="https://user-images.githubusercontent.com/104709432/179339588-7344ceb0-415a-49e7-9e5d-4cc8c0ca02fc.JPG"/>
  <b>4.상품등록</b><br>
<img width ="80%" src="https://user-images.githubusercontent.com/104709432/179339594-65f1158d-1363-4189-8471-f7b0a3fb9d4c.JPG"/>
<b>5.상품목록</b><br>
<img width ="80%" src="https://user-images.githubusercontent.com/104709432/179339598-d0a68ed2-1795-4b2f-a60b-399c4b224728.JPG"/>
  <b>6.상품주문</b><br>
<img width ="80%" src="https://user-images.githubusercontent.com/104709432/179339601-461910c4-4792-4604-8c78-fae9a47b6038.JPG"/>
