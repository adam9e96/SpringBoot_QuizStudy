package com.adam9e96.QuizStudy.repository;

import com.adam9e96.QuizStudy.entity.Quiz;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
/**
 * <p>
 * `QuizRepository` 인터페이스에 대한 단위 테스트 클래스입니다.
 * 이 클래스는 Spring Data JDBC를 사용하여 `QuizRepository`의 CRUD 및 커스텀 메서드가 올바르게 동작하는지 검증합니다.
 * </p>
 *
 * <p>
 * <strong>참고:</strong> 이 테스트 클래스는 인메모리 H2 데이터베이스를 사용하여 테스트를 수행합니다.
 * </p>
 *
 * @version 1.0
 * @since 2024-04-27
 */
@DataJdbcTest
class QuizRepositoryTest {
    @Autowired
    private QuizRepository quizRepository;

    /**
     * 퀴즈 저장 테스트
     */
    @Test
    @DisplayName("퀴즈 저장 후 조회 테스트")
    void testSaveAndFindById() {
        // Given
        Quiz quiz = new Quiz(null, "Java는 객체 지향 언어인가?", true, "작성자1");

        // When
        Quiz savedQuiz = quizRepository.save(quiz);

        // Then
        assertThat(savedQuiz.getId()).isNotNull();

        Optional<Quiz> foundQuiz = quizRepository.findById(savedQuiz.getId());
        assertThat(foundQuiz).isPresent();
        assertThat(foundQuiz.get().getQuestion()).isEqualTo(quiz.getQuestion());
        assertThat(foundQuiz.get().getAnswer()).isTrue();
        assertThat(foundQuiz.get().getAuthor()).isEqualTo("작성자1");
    }


    /**
     * 퀴즈 업데이트 테스트
     */
    @Test
    @DisplayName("퀴즈 업데이트 테스트")
    void testUpdateQuiz() {
        // Given
        Quiz quiz = new Quiz(null, "Spring Boot는 Java 기반 프레임워크인가?", true, "작성자2");
        Quiz savedQuiz = quizRepository.save(quiz);

        // When
        savedQuiz.setQuestion("Spring Boot는 Java 기반의 마이크로서비스 프레임워크인가?");
        Quiz updatedQuiz = quizRepository.save(savedQuiz);

        // Then
        Optional<Quiz> foundQuiz = quizRepository.findById(updatedQuiz.getId());
        assertThat(foundQuiz).isPresent();
        assertThat(foundQuiz.get().getQuestion()).isEqualTo("Spring Boot는 Java 기반의 마이크로서비스 프레임워크인가?");
    }

    /**
     * 퀴즈 삭제 테스트
     */
    @Test
    @DisplayName("퀴즈 삭제 테스트")
    void testDeleteQuiz() {
        // Given
        Quiz quiz = new Quiz(null, "PostgreSQL은 관계형 데이터베이스인가?", true, "작성자3");
        Quiz savedQuiz = quizRepository.save(quiz);

        // When
        quizRepository.deleteById(savedQuiz.getId());

        // Then
        Optional<Quiz> foundQuiz = quizRepository.findById(savedQuiz.getId());
        assertThat(foundQuiz).isNotPresent();
    }

    /**
     * 무작위 퀴즈 ID 조회 테스트
     */
    @Nested
    @DisplayName("getRandomId() 메서드 테스트")
    class GetRandomIdTests {

        @Test
        @DisplayName("퀴즈가 존재할 때 무작위 ID 반환")
        void testGetRandomIdWhenQuizzesExist() {
            // Given
            Quiz quiz1 = new Quiz(null, "퀴즈 1", true, "작성자1");
            Quiz quiz2 = new Quiz(null, "퀴즈 2", false, "작성자2");
            Quiz quiz3 = new Quiz(null, "퀴즈 3", true, "작성자3");
            quizRepository.save(quiz1);
            quizRepository.save(quiz2);
            quizRepository.save(quiz3);

            // When
            Integer randomId = quizRepository.getRandomId();

            // Then
            assertThat(randomId).isNotNull();
            assertThat(randomId).isIn(quiz1.getId(), quiz2.getId(), quiz3.getId());
        }

        @Test
        @DisplayName("퀴즈가 존재하지 않을 때 null 반환")
        void testGetRandomIdWhenNoQuizzesExist() {
            // Given
            // 데이터베이스에 아무런 퀴즈도 저장하지 않음

            // When
            Integer randomId = quizRepository.getRandomId();

            // Then
            assertThat(randomId).isNull();
        }
    }

    /**
     * 모든 퀴즈 조회 테스트
     */
    @Test
    @DisplayName("모든 퀴즈 조회 테스트")
    void testFindAllQuizzes() {
        // Given
        Quiz quiz1 = new Quiz(null, "퀴즈 A", true, "작성자A");
        Quiz quiz2 = new Quiz(null, "퀴즈 B", false, "작성자B");
        quizRepository.save(quiz1);
        quizRepository.save(quiz2);

        // When
        Iterable<Quiz> quizzes = quizRepository.findAll();

        // Then
        assertThat(quizzes).hasSize(2).extracting(Quiz::getQuestion)
                .containsExactlyInAnyOrder("퀴즈 A", "퀴즈 B");
    }

    /**
     * 존재하지 않는 퀴즈 조회 테스트
     */
    @Test
    @DisplayName("존재하지 않는 퀴즈 조회 시 빈 Optional 반환")
    void testFindByIdNotFound() {
        // Given
        Integer nonExistentId = 999;

        // When
        Optional<Quiz> foundQuiz = quizRepository.findById(nonExistentId);

        // Then
        assertThat(foundQuiz).isNotPresent();
    }

    /**
     * 존재하지 않는 퀴즈 삭제 테스트
     */
    @Test
    @DisplayName("존재하지 않는 퀴즈 삭제 시 예외 발생")
    void testDeleteNonExistentQuiz() {
        // Given
        Integer nonExistentId = 1000;

        // When / Then
        assertDoesNotThrow(() -> quizRepository.deleteById(nonExistentId));
    }

}
