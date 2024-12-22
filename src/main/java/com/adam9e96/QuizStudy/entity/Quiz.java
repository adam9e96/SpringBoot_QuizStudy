package com.adam9e96.QuizStudy.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 *
 * <p>
 * 퀴즈 정보를 나타내는 엔티티(Entity) 클래스입니다.
 * 이 클래스는 데이터베이스의 `quizdb` 스키마의 `quiz` 테이블과 매핑되며,
 * 퀴즈의 내용, 정답, 작성자 등의 정보를 저장합니다.
 * </p>
 *
 * @author adam9e96
 * @version 1.0.0
 * @since 2024-11-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    /**
     * 퀴즈의 고유  식별자입니다.
     * <p>
     * 데이터베이스에서 각 퀴즈를 구분하기 위한 기본 키로 사용됩니다.
     * </p>
     */
    @Id
    private Integer id;
    /**
     * 퀴즈의 질문 내용입니다.
     * <p>
     * 사용자가 풀어야 할 문제의 내용을 저장합니다.
     * </p>
     */
    private String question;
    /**
     * 퀴즈의 정답입니다.
     * <p>
     * 정답이 참(true) 또는 거짓(false)으로 표시됩니다.
     * </p>
     */
    private Boolean answer;
    /**
     * 퀴즈를 작성한 사용자의 이름입니다.
     * <p>
     * 퀴즈의 작성자를 식별하기 위해 사용됩니다.
     * </p>
     */
    @NotNull
    private String author;

}
