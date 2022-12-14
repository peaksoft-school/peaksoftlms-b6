package kg.peaksoft.peaksoftlmsb6.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "tests")
@Getter
@Setter
@NoArgsConstructor
public class Test {

    @Id
    @SequenceGenerator(name = "test_seq", sequenceName = "test_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "test_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String testName;

    @OneToMany(cascade = ALL)
    private List<Question> question;

    @OneToMany(cascade = ALL, mappedBy = "test")
    private List<Results> results;

    @OneToOne(cascade = {
            MERGE,
            REFRESH,
            DETACH})
    private Lesson lesson;

    private Boolean isEnable = true;

    public void addQuestion(Question question) {
        if(this.question == null) {
            this.question = new ArrayList<>();
        }
        this.question.add(question);
    }

    public Test(String testName) {
        this.testName = testName;
    }
}
