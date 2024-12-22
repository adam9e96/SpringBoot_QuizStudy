package com.adam9e96.QuizStudy.service;


import com.adam9e96.QuizStudy.entity.Quiz;

import java.util.List;
import java.util.Optional;

/**
 * Quiz 서비스 인터페이스
 *
 * <p>
 * 이 인터페이스는 퀴즈 관련 비즈니스 로직을 정의하며,
 * 퀴즈의 생성, 조회, 업데이트, 삭제 등의 기능을 제공합니다.
 * </p>
 *
 * <p>
 * <strong>주의:</strong> 이 서비스는 트랜잭션 관리가 필요한 경우, 구현 클래스에서 {@link org.springframework.transaction.annotation.Transactional}
 * 어노테이션을 사용하여 트랜잭션을 관리해야 합니다.
 * </p>
 *
 * @author adam9e96
 * @version 1.0.0
 * @since 2024-11-30
 */
public interface QuizService {

    /**
     * 등록된 모든 퀴즈 정보를 조회합니다.
     *
     * @return 등록된 모든 퀴즈의 {@link Iterable<Quiz>} 컬렉션
     */
    Iterable<Quiz> selectAll();

    /**
     * 주어진 ID를 사용하여 특정 퀴즈 정보를 조회합니다.
     *
     * @param id : 조회할 퀴즈의 고유 ID
     * @return 해당 ID에 해당하는 퀴즈 정보가 존재하면 {@link Optional}로 반환하고, 존재하지 않으면 빈 {@link Optional}을 반환
     */
    Optional<Quiz> selectOneById(int id);

    /**
     * 데이터베이스에서 무작위로 선택된 퀴즈 정보를 조히합니다.
     *
     * @return 무작위로 선택된 퀴즈의 {@link Optional} 객체. 퀴즈가 존재하지 않으면 빈 {@link Optional} 을 반환
     */
    Optional<Quiz> selectOneRandomQuiz();

    /**
     * 특정 퀴즈의 정답 여부를 확인합니다.
     *
     * @param id       정답 여부를 확인할 퀴즈의 고유 ID
     * @param myAnswer 사용자가 제출한 답변 (true : 정답, false : 오답)
     * @return 퀴즈의 정답과 사용자의 답변일 일치하면 {@code true}, 그렇지 않으면 {@code false}
     */
    Boolean checkQuiz(Integer id, Boolean myAnswer);

    /**
     * 새로운 퀴즈를 등록합니다.
     *
     * @param quiz 저장할 퀴즈 정보가 담긴 {@link Quiz} 객체
     */
    void insertQuiz(Quiz quiz);

    /**
     * 기존에 등록된 퀴즈 정보를 업데이트합니다.
     *
     * @param quiz 업데이트할 퀴즈 정보가 담긴 {@link Quiz} 객체. 객체의 ID는 업데이트할 퀴즈의 고유 ID를 나타냅니다.
     */
    void updateQuiz(Quiz quiz);

    /**
     * 주어진 ID를 사용하여 퀴즈를 삭제합니다.
     *
     * @param id 삭제할 퀴즈의 id
     */
    void deleteQuizById(Integer id);

    /**
     * 무작위로 5개의 퀴즈를 조회합니다.
     *
     * @return 무작위로 선택된 5개의 {@link List<String>} 객체 목록
     */
    List<String> getRandomQuizzes();


}
