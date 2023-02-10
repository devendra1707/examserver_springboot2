package com.exam.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.model.exam.Question;
import com.exam.model.exam.Quiz;
import com.exam.service.QuestionService;
import com.exam.service.QuizService;

@CrossOrigin("*")
@RestController
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private QuizService quizService;

	// add Question

	@PostMapping("/")
	public ResponseEntity<Question> addQuestion(@RequestBody Question question) {
		return ResponseEntity.ok(this.questionService.addQuestion(question));
	}

	// Update Question

	@PutMapping("/")
	public ResponseEntity<Question> updateQuestion(@RequestBody Question question) {
		return ResponseEntity.ok(this.questionService.updateQuestion(question));
	}

	// get All Question of any Quiz

	@GetMapping("/quiz/{qid}")
	public ResponseEntity<?> getQuestionsOfQuiz(@PathVariable("qid") Long qid) {
		/*
		 * Get All The Question Quiz quiz = new Quiz(); quiz.setQId(qid); Set<Question>
		 * questionOfQuiz = this.questionService.getQuestionsOfQuiz(quiz); return
		 * ResponseEntity.ok(questionOfQuiz);
		 * 
		 */
		/*
		 * Quiz quiz = this.quizService.getQuiz(qid); Set<Question> questions =
		 * quiz.getQuestions();
		 * 
		 * List list = new ArrayList(questions); if (list.size() >
		 * Integer.parseInt(quiz.getNumberOfQuestions())) { list = list.subList(0,
		 * Integer.parseInt(quiz.getNumberOfQuestions() + 1)); }
		 * Collections.shuffle(list); return ResponseEntity.ok(list);
		 */
		Quiz quiz = this.quizService.getQuiz(qid);
		Set<Question> questions = quiz.getQuestions();

		List<Question> list = new ArrayList(questions);
		if (list.size() > Integer.parseInt(quiz.getNumberOfQuestions())) {
			list = list.subList(0, Integer.parseInt(quiz.getNumberOfQuestions() + 1));

			list.forEach((q) -> {
				q.setAnswer("");
			});
		}
		Collections.shuffle(list);
		return ResponseEntity.ok(list);

	}

	// get All Question of any Quiz For Admin

	@GetMapping("/quiz/all/{qid}")
	public ResponseEntity<?> getQuestionsOfQuizAdmin(@PathVariable("qid") Long qid) {

//			Get All The Question

		Quiz quiz = new Quiz();
		quiz.setQId(qid);
		Set<Question> questionOfQuiz = this.questionService.getQuestionsOfQuiz(quiz);
		return ResponseEntity.ok(questionOfQuiz);

	}

	// get Single Question

	@GetMapping("/{quesId}")
	public Question get(@PathVariable("quesId") Long quesId) {
		return this.questionService.getQuestion(quesId);
	}

	// delete question

	@DeleteMapping("/{quesId}")
	public void deleteQuestion(@PathVariable("quesId") Long quesId) {
		this.questionService.deleteQuestion(quesId);
	}

	// Evaluate Quiz

	@PostMapping("/eval-quiz")
	public ResponseEntity<?> evalQuiz(@RequestBody List<Question> questions) {

		System.out.println(questions);

		double marksGot = 0;
		int correctAnswers = 0;
		int attempted = 0;

		for (Question q : questions) {
			// single Question

			Question question = this.questionService.get(q.getQuesId());
			if (question.getAnswer().equals(q.getGivenAnswer())) {
				// correct
				correctAnswers++;

				Double marksSingle = Double.parseDouble(questions.get(0).getQuiz().getMaxMarks()) / questions.size();
				marksGot += marksSingle;
			}

			if (q.getGivenAnswer() != null) {
				attempted++;
			}
		}
		Map<Object, Object> map = Map.of("marksGot", marksGot, "correctAnswers", correctAnswers, "attempted",
				attempted);
		return ResponseEntity.ok(map);
	}

}
