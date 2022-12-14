package kg.peaksoft.peaksoftlmsb6.entity;

import kg.peaksoft.peaksoftlmsb6.dto.request.QuestionRequest;
import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "questions")
@Setter
@Getter
@NoArgsConstructor
public class Question {

    @Id
    @SequenceGenerator(name = "question_seq", sequenceName = "question_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "question_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String question;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @OneToMany(cascade = ALL)
    private List<Option> options;

    public Question(String question, QuestionType questionType) {
        this.question = question;
        this.questionType = questionType;
    }

    public Question(Question question) {
        this.id = question.getId();
        this.question = question.getQuestion();
        this.questionType = question.getQuestionType();
    }

    public Question(QuestionRequest question) {
        this.question = question.getQuestion();
        this.questionType = question.getQuestionType();
    }


    public void addOption(Option option) {
        if(this.options == null) {
            this.options = new ArrayList<>();
        }
        this.options.add(option);
    }
}
