package kg.peaksoft.peaksoftlmsb6.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "tests")
@Getter
@Setter
@NoArgsConstructor
public class Test {

    @Id
    @SequenceGenerator(name = "test_seq", sequenceName = "test_seq", allocationSize = 1)
    @GeneratedValue(generator = "test_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String testName;

    @OneToMany(cascade = ALL)
    private List<Question> question;

    @OneToOne(cascade = {
            MERGE,
            REFRESH,
            DETACH})
    private Lesson lesson;

    private Boolean isEnable;


}