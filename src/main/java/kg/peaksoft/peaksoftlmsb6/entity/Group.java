package kg.peaksoft.peaksoftlmsb6.entity;

import kg.peaksoft.peaksoftlmsb6.dto.request.GroupRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
public class Group {

    @Id
    @SequenceGenerator(name = "group_seq", sequenceName = "group_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "group_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String groupName;

    @Column(length = 100000)
    private String groupDescription;

    private LocalDate dateOfStart;

    private String groupImage;

    @OneToMany(cascade = ALL, mappedBy = "group")
    private List<Student> students;

    @ManyToMany(cascade = {DETACH, REFRESH}, mappedBy = "group")
    private List<Course> courses;

    public void addStudents(Student student) {
        if (this.students == null) {
            this.students = new ArrayList<>();
        }
        this.students.add(student);
    }

    public Group(GroupRequest request) {
        this.groupName = request.getGroupName();
        this.groupDescription = request.getDescription();
        this.dateOfStart = request.getDateOfStart();
        this.groupImage = request.getImage();
    }

}
