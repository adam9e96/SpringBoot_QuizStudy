package com.adam9e96.QuizStudy.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 퀴즈 관련 웹 폼 데이터를 처리하는 클래스입니다.
 * 이 클래스는 사용자로부터 입력받은 데이터를 캡슐화하며,
 * 퀴즈의 생성 및 수정 시 필요한 정보를 담고 있습니다.
 * </p>
 *
 * <p>
 * <strong>주의 :</strong> 폼 요구사항에 따라 `QuizForm` 클래스는 자유롭게 변경 될 수있지만,
 * `Quiz` 엔티티 클래스는 변경할 수 없습니다.
 * </p>
 *
 * @author adam9e96
 * @version 1.0.0
 * @since 2024-11-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizForm {
    /**
     * 퀴즈의 고유 식별자입니다.
     * <p>
     * 퀴즈의 수정 시 해당 퀴즈를 식별하기 위해 사용됩니다.
     * </p>
     */
    private Integer id;

    /**
     * 퀴즈의 질문 내용입니다.
     * <p>
     * 사용자로부터 입력받은 퀴즈의 질문을 저장합니다.
     * 이 필드는 필수 입력 항목으로, 비어 있을 수 없습니다.
     * </p>
     */
    @NotBlank(message = "퀴즈 질문은 필수 입력 항목입니다.")
    private String question;
    /**
     * 퀴즈의 정답입니다.
     * <p>
     * 사용자가 제공한 정답을 저장합니다.
     * </p>
     */
    @NotNull(message = "퀴즈 정답은 필수 입력 항목입니다.")
    private Boolean answer;
    /**
     * 퀴즈를 작성한 사용자의 이름입니다.
     * <p>
     * 사용자로부터 입력받은 작성자 이름을 저장합니다.
     * 이 필드는 필수 입력 항목으로, 비어 있을 수 없습니다.
     * </p>
     */
    @NotBlank(message = "작성자 이름은 필수 입력 항목입니다.")
    private String author;

    /**
     * 퀴즈의 등록 또는 변경을 판단하기 위한 플래그입니다.
     * <p>
     * `true` 인 경우 새로운 퀴즈를 등록하는 모드,
     * `false` 인 경우 기존 퀴즈를 수정하는 것을 의미합니다.
     * </p>
     */
    private Boolean newQuiz;
}
