package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select s from Student s where s.group.courses =: id")
    CourseResponse getAllStudentsByCourse(Long id);

    @Query("select i from Instructor i where i.courses =: id")
    CourseResponse getAllInstructorsByCourse(Long id);

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse(" +
            "c.id," +
            "c.courseName," +
            "c.courseDescription," +
            "c.dateOfStart," +
            "c.courseImage) from Course c")
    List<CourseResponse> getAllCourses();


    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse(" +
            "c.id," +
            "c.courseName," +
            "c.courseDescription," +
            "c.dateOfStart," +
            "c.courseImage) from Course c where c.id = ?1")
    Optional<CourseResponse> getCourseById(Long id);

}
