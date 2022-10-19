package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select s from Student s where s.group.courses =: id")
    CourseResponse getAllStudentsByCourse(Long id);

    @Query("select i from Instructor i where i.courses =: id")
    CourseResponse getAllInstructorsByCourse(Long id);

}
