package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Modifying
    @Transactional
    @Query("update Course set " +
            "courseName = :courseName, " +
            "courseDescription = :description, " +
            "dateOfStart = :dateOfStart, " +
            "courseImage = :image where id = :id")
    void update(@Param("id") Long id,
                @Param("courseName") String courseName,
                @Param("description") String description,
                @Param("dateOfStart") LocalDate dateOfStart,
                @Param("image") String image);
}
