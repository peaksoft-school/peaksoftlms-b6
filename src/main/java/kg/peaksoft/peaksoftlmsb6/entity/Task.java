package kg.peaksoft.peaksoftlmsb6.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kg.peaksoft.peaksoftlmsb6.dto.request.TaskRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "tasks")
@Setter
@Getter
@NoArgsConstructor
public class Task {

    @Id
    @SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "task_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String taskName;

    @JsonIgnore
    @OneToMany(cascade = ALL, mappedBy = "task")
    private List<Content> contents;

    @OneToOne(cascade = {
            MERGE,
            REFRESH,
            DETACH,
            PERSIST})
    private Lesson lesson;

    public Task(TaskRequest request) {
        this.taskName = request.getTaskName();
    }

    public void addContent(Content content) {
        if(this.contents == null) {
            this.contents = new ArrayList<>();
        }
        this.contents.add(content);
    }

}
