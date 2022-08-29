package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;


import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTest {

    @Autowired
    private GuestBookRepository guestBookRepository;

    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1,300).forEach(i->{

            Guestbook guestbook = Guestbook.builder()
                    .title("Title..." +i)
                    .content("Content..."+i)
                    .writer("Writer..."+i)
                    .build();
            System.out.println(guestBookRepository.save(guestbook));
        });
    }
    @Test
    public void updateTest(){

        Optional<Guestbook> result = guestBookRepository.findById(300L); //존재하는 번호로 테스트

        if(result.isPresent()){
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Change Title....");
            guestbook.changeContent("Change Content....");

            guestBookRepository.save(guestbook);
        }
    }

    /**
     * 단일항목 검색테스트
     */
    @Test
    public void testQuery1(){

       Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook; //1

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder(); //2

        BooleanExpression expression = qGuestbook.title.contains(keyword);//3

        builder.and(expression);//4

        Page<Guestbook> result = guestBookRepository.findAll(builder,pageable);//5

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
        /**
         * 가장먼저 동적으로 처리하기 위해서 Q도메인클래스르 얻어옴,Q도메인 클래스를 이용하며ㅛㄴ 엔티티클래스에 선언된 title,content같은 필들들을
         * 변수로 활용가능
         * BooleanBuilder는 where문에 들어가는 조건들을 넣어주는 컨테이너라고 간주하면됨
         * 원하는 조건은 필드값과 같이 결합해서 생성,BooleanBuilder안에 들어가는 값은 com.querydsl.core.types.Predicate 타입이어야함
         * 만들어진 조건은 where문에 and 나 or 같은 키워드를 추가
         * BooleanBulider는 GusetBookRepository에 추가된 QuerydslPredicateExcutor 인터페이스의 findALl()을 사용할수있습니다.
         */
    }
    @Test
    public void testQuery2(){

        Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword ="1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.title.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent);//1----------------

        builder.and(exAll); //2------

        builder.and(qGuestbook.gno.gt(0L)); //3---------

        Page<Guestbook> result = guestBookRepository.findAll(builder,pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
}
