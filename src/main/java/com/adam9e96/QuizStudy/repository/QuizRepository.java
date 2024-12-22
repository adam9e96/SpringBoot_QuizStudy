package com.adam9e96.QuizStudy.repository;

import com.adam9e96.QuizStudy.entity.Quiz;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * <p>
 * `Quiz` 엔티티에 대한 데이터 접근 계층을 담당하는 리포지토리 인터페이스입니다.
 * 이 인터페이스는 Spring Data 의 `CrudRepository` 를 상속 받아 기본적인 CRUD (생성, 조회, 업데이트, 삭제) 기능을 제공합니다.
 * </p>
 *
 * <p>
 * 추가적으로, 커스텀 쿼리를 정의하여 특정 요구사항에 맞는 데이터 조회를 수행할 수 있습니다.
 * </p>
 *
 * <p>
 * <strong>주의:</strong> `Quiz` 엔티티와 `Integer` 타입의 ID 를 사용합니다.
 * </p>
 *
 * @author adam9e96
 * @version 1.0.0
 * @since 2024-11-30
 */
public interface QuizRepository extends CrudRepository<Quiz, Integer> {

    /**
     * 데이터베이스의 `quiz` 테이블에서 무작위로 하나의 퀴즈 ID를 조회합니다.
     * <p>
     * 이 메소드는 퀴즈 중 하나를 무작위로 선택하고 해당 퀴즈의 ID를 반환합니다.
     * 주로 무작위 퀴즈를 제공하거나 테스트할 때 유용하게 사용됩니다.
     * </p>
     *
     * @return 무작위로 선택된 퀴즈의 ID. 퀴즈가 없을 경우 `null` 을 반환할 수 있습니다.
     */
    @Query("Select q.id from quiz q order by random() limit 1")
    Integer getRandomId();

//    /**
//     * 데이터베이스의 `quiz` 테이블에서 무작위로 5개의 퀴즈를 조회합니다.
//     * <p>
//     * 이 메소드는 퀴즈 중 5개를 무작위로 선택하여 반환합니다.
//     * </p>
//     *
//     * @param pageable 페이징 정보를 담은 {@link Pageable} 객체
//     * @return 무작위로 선택된 5개의 {@link Quiz} 객체 목록
//     */
//    @Query("SELECT q.question FROM Quiz q ORDER BY random();")
//    List<Quiz> getRandomQuestions(Pageable pageable);

    /**
     * 데이터베이스의 `quiz` 테이블에서 무작위로 5개의 퀴즈 내용을 조회합니다.
     * <p>
     * 이 메소드는 퀴즈 중 5개의 내용을 무작위로 선택하여 반환합니다.
     * </p>
     * <p>
     * //     * @param pageable 페이징 정보를 담은 {@link Pageable} 객체
     *
     * @return 무작위로 선택된 5개의 퀴즈 내용 {@link List<String>} 목록
     */
    @Query("SELECT q.question FROM Quiz q ORDER BY RANDOM() limit 5")
    List<String> getRandomQuestions();
}
