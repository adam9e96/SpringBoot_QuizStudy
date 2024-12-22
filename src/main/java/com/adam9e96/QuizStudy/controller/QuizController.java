package com.adam9e96.QuizStudy.controller;

import com.adam9e96.QuizStudy.entity.Quiz;
import com.adam9e96.QuizStudy.form.QuizForm;
import com.adam9e96.QuizStudy.service.QuizService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Quiz 컨트롤러
 */
@Controller
@RequestMapping("/quiz")
@Log4j2
public class QuizController {
    /**
     * DI 대상
     */
    @Autowired
    QuizService quizService;

    /**
     * form-backing bean의 초기화
     */
    @ModelAttribute
    public QuizForm setUpForm() {
        QuizForm quizForm = new QuizForm();
        // 라디오 버튼의 초깃값 설정
        quizForm.setAnswer(true);
        return quizForm;
    }

    /**
     * Quiz 목록 표시
     */
    @GetMapping
    public String showList(QuizForm quizForm, Model model) {
        log.info("showList 메소드 실행됨");
        // 신규 등록 설정
        quizForm.setNewQuiz(true); // 신규 등록 모드 : insert mode
        log.info("quizForm 값 : {}", quizForm.toString());
        // 퀴즈 목록 취득
        Iterable<Quiz> list = quizService.selectAll();
        // 표시용 모델에 저장
        model.addAttribute("list", list);
        model.addAttribute("title", "등록 폼");
        model.addAttribute("site_title", "OX 퀴즈 애플리케이션: CRUD");
        log.info("quizService 호출 결과 : {}", list.toString());
        return "crud";
    }

    /**
     * Quiz 데이터를 1건 등록 <br>
     * Create : C
     */
    @PostMapping("/insert")
    public String insert(@Validated QuizForm quizForm, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes) {
        // Form에서 Entity로 넣기
        log.info("quizForm 객체 : {}", quizForm.toString());
        Quiz quiz = new Quiz();
        quiz.setQuestion(quizForm.getQuestion());
        quiz.setAnswer(quizForm.getAnswer());
        quiz.setAuthor(quizForm.getAuthor());
        log.info("엔티티에 담긴 값 : {}", quiz.toString());

        // 입력 체크
        if (!bindingResult.hasErrors()) {
            quizService.insertQuiz(quiz);
            redirectAttributes.addFlashAttribute("complete", "등록이 완료되었습니다.");
            log.info("insert 성공");
            return "redirect:/quiz";
        } else {
            // 에러가 발생한 경우에는 목록 표시로 변경
            return showList(quizForm, model);
        }
    }

    /**
     * Quiz 데이터를 1건 취득해서 폼 안에 표시 <br>
     * Update : U
     * <p>
     * 메소드 동작
     * 1. 요청 처리: 클라이언트가 특정 id를 가지고 /quiz/{id} 엔드포인트에 접근.
     * 2. 데이터 조회: 서비스 계층을 통해 해당 id에 해당하는 Quiz 엔티티를 조회합니다.
     * 3. 변환: 조회된 Quiz 엔티티를 QuizForm으로 변환합니다. (makeQuizForm 메소드 사용)
     * 4. 모델에 추가: 변환된 QuizForm을 모델에 추가하여 뷰로 전달합니다.
     */
    @GetMapping("/{id}")
    public String showUpdate(QuizForm quizForm, @PathVariable Integer id, Model model) {
        log.info("showUpdate 호출됨");
        log.info("요청된 id {}", id);
        log.info("quizForm : {}", quizForm.toString());

        // Quiz 를 취득(Optional 로 래핑)
        // 서비스 계층을 통해 해당 id에 해당하는 Quiz 엔티티 조회
        Optional<Quiz> quizOptional = quizService.selectOneById(id);

        log.info("조회된 quiz 엔티티 {}", quizOptional.toString());

        // QuizForm에 채워넣기
        // 변환 : QUiz 엔티티를 quizForm으로 변환 makeQuizForm 메소드 사용
        Optional<QuizForm> quizFormOptional = quizOptional.map(t -> makeQuizForm(t));
        log.info("quizForm으로 변환 {}", quizFormOptional.toString());

        // QuizForm이 null 이 아니라면 값을 취득
        if (quizFormOptional.isPresent()) {
            log.info("before quizForm {}", quizForm.toString());
            quizForm = quizFormOptional.get();
            log.info("after quizForm {}", quizForm.toString());
        }

        // 변경용 모델 생성
        // 변환된 quizForm을 모델에 추가
        makeUpdateModel(quizForm, model);
        log.info("showUpdate 메소드 성공");
        log.info("crud 이동");
        return "crud";
    }

    /**
     * 변경용 모델 생성
     */
    private void makeUpdateModel(QuizForm quizForm, Model model) {
        model.addAttribute("id", quizForm.getId());
        quizForm.setNewQuiz(false); // 이게 핵심. crud.html로 돌아가면 false 라서 quiz/update로 이동함(post)
        model.addAttribute("quizForm", quizForm);
        model.addAttribute("title", "변경 폼");
        log.info("7");
    }

    /**
     * id 를 키로 사용해 데이터를 변경
     */
    @PostMapping("/update")
    public String update(
            @Validated QuizForm quizForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        log.info("update() 실행 {}", quizForm.toString());
        log.info("update() 실행 {}", model.toString());

        // QuizForm 객체를 Quiz 엔티티로 변환
        Quiz quiz = makeQuiz(quizForm);
        // 입력 체크
        if (!bindingResult.hasErrors()) {
            // 변경 처리, Flash scope를 사용해서 리다이렉트 설정
            quizService.updateQuiz(quiz);
            redirectAttributes.addFlashAttribute("complete", "변경이 완료되었습니다.");
            // 변경 화면을 표시
            return "redirect:/quiz/" + quiz.getId();
        } else {
            // 오류 발생 시 (폼에 데이터를 입력하지 않은경우(기존에 쓰여진것도 지운경우)
            // 변경용 모델을 생성
            model.addAttribute("error", "bindingResult.getAllErrors()");
            log.error("입력 검증 오류 발생: {}", bindingResult.getAllErrors());
            makeUpdateModel(quizForm, model);
            return "crud";
        }
    }
    // ----- ⟦아래는 Form과 도메인 객체를 다시 채우기⟧ -----

    /**
     * QuizForm에서 Quiz로 다시 채우기, 반환값으로 돌려줌
     */
    private Quiz makeQuiz(QuizForm quizForm) {
        Quiz quiz = new Quiz();
        quiz.setId(quizForm.getId());
        quiz.setQuestion(quizForm.getQuestion());
        quiz.setAnswer(quizForm.getAnswer());
        quiz.setAuthor(quizForm.getAuthor());
        return quiz;
    }

    /**
     * Quiz에서 QuizForm으로 다시 채우기, 반환값으로 돌려줌
     */
    private QuizForm makeQuizForm(Quiz quiz) {
        QuizForm quizForm = new QuizForm();
        quizForm.setId(quiz.getId());
        quizForm.setQuestion(quiz.getQuestion());
        quizForm.setAnswer(quiz.getAnswer());
        quizForm.setAuthor(quiz.getAuthor());
        return quizForm;
    }

    /**
     * id를 키로 사용해 데이터를 삭제
     */
    @PostMapping("/delete")
    public String delete(
            @RequestParam("id") String id,
            Model model,
            RedirectAttributes redirectAttributes) {
        // 퀴즈 정보 1건을 삭제하고 리다이렉트
        quizService.deleteQuizById(Integer.valueOf(id));
        redirectAttributes.addFlashAttribute("delComplete", "삭제 완료했습니다.");
        return "redirect:/quiz";
    }

    /**
     * Quiz 데이터를 랜덤으로 한 건 가져와 화면에 표시
     */
    @GetMapping("/play")
    public String showQUiz(QuizForm quizForm, Model model) {

        // QUiz 정보 취득(Optional 으로 래핑)
        Optional<Quiz> quizOptional = quizService.selectOneRandomQuiz();
        log.info("랜덤으로 가져온 Quiz 정보 {}", quizOptional.toString());

        // 같이 있는지 확인
        if (quizOptional.isPresent()) {
            // QuizForm 으로 채우기
            Optional<QuizForm> quizFormOptional = quizOptional.map(t -> makeQuizForm(t));
            quizForm = quizFormOptional.get();
            log.info("play에 보여줄 quizForm 객체 : {}", quizForm.toString());
        } else {
            model.addAttribute("msg", "등록된 문제가 없습니다.");
            return "play";
        }
        // 표시용 모델에 저장
        model.addAttribute("quizForm", quizForm);
        return "play";
    }

    /**
     * 퀴즈의 정답/오답 판단
     */
    @PostMapping("/check")
    public String checkQuiz(
            QuizForm quizForm,
            @RequestParam Boolean answer,
            Model model) {
        if (quizService.checkQuiz(quizForm.getId(), answer)) {
            model.addAttribute("msg", "정답입니다.");
        } else {
            model.addAttribute("msg", "오답입니다.");
        }
        return "answer";
    }

    @GetMapping("/random")
    public String showRandomQuizzes(Model model) {
        log.info("showRandomQuizzes() 실행됨");

        List<String> randomQuizzes = quizService.getRandomQuizzes();
        log.info("랜덤으로 선택된 퀴즈 목록: {} ", randomQuizzes);
        model.addAttribute("randomQuizzes", randomQuizzes);
        model.addAttribute("title", "랜덤 퀴즈 목록");
        model.addAttribute("site_title", "OX 퀴즈 애플리케이션: 랜덤 퀴즈");
        return "randomQuizzes"; // 뷰 파일을 반환
    }

}
