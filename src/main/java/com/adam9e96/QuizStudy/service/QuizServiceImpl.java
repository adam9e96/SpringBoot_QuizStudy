package com.adam9e96.QuizStudy.service;

import com.adam9e96.QuizStudy.entity.Quiz;
import com.adam9e96.QuizStudy.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Quiz 서비스 구현 클래스
 *
 * <p>
 * 이 클래스는 {@link QuizService} 인터페이스를 구현하며, 퀴즈 관련 비즈니스 로직을 담당합니다.
 * 데이터 접근 계층인 {@link QuizRepository} 를 사용하여 퀴즈 데이터를 조회, 저장, 수정, 삭제하는 기능을 제공합니다.
 * </p>
 *
 * <p>
 * <strong>주요 기능:</strong>
 * <ul>
 *     <li>모든 퀴즈 조회</li>
 *     <li>특정 ID의 퀴즈 조회</li>
 *     <li>무작위 퀴즈 조회</li>
 *     <li>퀴즈 정답 여부 확인</li>
 *     <li>퀴즈 등록</li>
 *     <li>퀴즈 수정</li>
 *     <li>퀴즈 삭제</li>
 * </ul>
 * </p>
 * <hr>
 * <p>
 * <strong>어노테이션 설명:</strong>
 * <ul>
 *     <li>{@code @Service}: 이 클래스가 서비스 계층의 컴포넌트임을 Spring에게 알립니다.</li>
 *     <li>{@code @Transactional}: 이 클래스의 모든 메서드가 트랜잭션 내에서 실행되도록 설정합니다.</li>
 *     <li>{@code @RequiredArgsConstructor}: Lombok을 사용하여 필드에 대한 생성자를 자동으로 생성합니다. final 필드에 대한 생성자 주입을 용이하게 합니다.</li>
 * </ul>
 * </p>
 *
 * @author adam9e96
 * @version 1.0.0
 * @since 2024-11-30
 */
@Service
@Transactional
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    /**
     * <p>
     * #생성자(@RequiredArgsConstructor 사용)(Lombok 활용)를 이용한 빈 주입 방법
     * </p>
     * <p>
     * 퀴즈 데이터 접근을 담당하는 리포지토리
     * <p>
     * <p>
     * {@code final} 키워드를 사용하여 불변성을 유지하며, 생성자 주입을 통해 의존성을 주입받습니다.
     * </p>
     */
    private final QuizRepository quizRepository;

    /**
     * 등록된 모든 퀴즈 정보를 조회합니다.
     *
     * @return 등록된 모든 퀴즈의 {@link Iterable} 컬렉션
     */
    @Override
    public Iterable<Quiz> selectAll() {
        return quizRepository.findAll();
    }

    /**
     * 주어진 ID를 사용하여 특정 퀴즈 정보를 조회합니다.
     *
     * @param id 조회할 퀴즈의 고유 ID
     * @return 해당 ID에 해당하는 퀴즈 정보가 존재하면 {@link Optional}로 반환하고, 존재하지 않으면 빈 {@link Optional}을 반환
     */
    @Override
    public Optional<Quiz> selectOneById(int id) {
        return quizRepository.findById(id);
    }


    /**
     * 데이터베이스에서 무작위로 선택된 퀴즈 정보를 조회합니다.
     *
     * @return 무작위로 선택된 퀴즈의 {@link Optional} 객체. 퀴즈가 존재하지 않으면 빈 {@link Optional}을 반환
     */
    @Override
    public Optional<Quiz> selectOneRandomQuiz() {
        // 랜덤으로 id 값을 가져오기
        Integer randId = quizRepository.getRandomId();

        // 퀴즈가 없는 경우
        if (randId == null) {
            // 빈 Optional 인스턴스를 반환
            return Optional.empty();
        }
        return quizRepository.findById(randId);

    }


    /**
     * 특정 퀴즈의 정답 여부를 확인합니다.
     *
     * @param id       정답 여부를 확인할 퀴즈의 고유 ID
     * @param myAnswer 사용자가 제출한 답변 (true: 정답, false: 오답)
     * @return 퀴즈의 정답과 사용자의 답변이 일치하면 {@code true}, 그렇지 않으면 {@code false}
     */
    @Override
    public Boolean checkQuiz(Integer id, Boolean myAnswer) {
        // 퀴즈 정답/오답 판단용 변수
        Boolean check = false;

        // 대상 퀴즈를 가져오기
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);

        // 퀴즈를 가져왔는지 확인
        if (optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            // 퀴즈 정답 확인
            if (quiz.getAnswer().equals(myAnswer)) {
                check = true;
            }
        }
        return check;
    }

    /**
     * 새로운 퀴즈를 등록합니다.
     *
     * @param quiz 저장할 퀴즈 정보가 담긴 {@link Quiz} 객체
     * @throws {@link org.springframework.dao.DataIntegrityViolationException} 필수 필드가 누락된 경우 예외를 던질 수 있습니다.
     */
    @Override
    public void insertQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    /**
     * 기존의 퀴즈 정보를 업데이트합니다.
     *
     * <p>
     * {@code Quiz} 객체의 ID를 사용하여 해당 퀴즈를 찾아 업데이트합니다.
     * </p>
     *
     * @param quiz 업데이트할 퀴즈 정보가 담긴 {@link Quiz} 객체. 객체의 ID는 업데이트할 퀴즈의 고유 ID를 나타냅니다.
     */
    @Override
    public void updateQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    /**
     * 주어진 ID를 사용하여 퀴즈를 삭제합니다.
     *
     * <p>
     * {@code  QuizRepository.deleteById(Integer id)} 메서드를 호출하여 퀴즈를 삭제합니다.
     * </p>
     *
     * @param id 삭제할 퀴즈의 고유 ID
     */
    @Override
    public void deleteQuizById(Integer id) {
        quizRepository.deleteById(id);
    }

    @Override
    public List<String> getRandomQuizzes() {
        // Pageable 객체를 생성하여 상위 5개의 결과를 요청
//        PageRequest pageRequest = PageRequest.of(0, 5);
        return quizRepository.getRandomQuestions();
    }

}
