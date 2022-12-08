package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.AssignGroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.AssignInstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.DeleteInstructorFromCourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AssignInstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Course;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.CourseRepository;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.InstructorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CourseService courseService;

    @Test
    void allStudentsFromCourse() {
        List<StudentResponse> students = courseService.getAllStudentsFromCourse(1L);

        assertEquals(5, students.size());
    }

    @Test
    void getAllInstructorsFromCourse() {
        List<AssignInstructorResponse> instructors = courseService.getAllInstructorsFromCourse(1L);

        assertEquals(1, instructors.size());
    }

    @Test
    void assignInstructorToCourse() {
        AssignInstructorRequest request = new AssignInstructorRequest();
        request.setInstructorId(List.of(2L));
        request.setCourseId(2L);

        SimpleResponse result = courseService.assignInstructorToCourse(request);

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));

        assertNotNull(result);
        assertThat(course.getInstructors().contains(instructorRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException("Instructor not found")))).isTrue();
    }

    @Test
    void assignGroupToCourse() {
        AssignGroupRequest request = new AssignGroupRequest();
        request.setGroupId(2L);
        request.setCourseId(2L);

        SimpleResponse result = courseService.assignGroupToCourse(request);

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));

        assertNotNull(result);
        assertThat(course.getGroup().contains(groupRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException("Group not found")))).isTrue();

    }

    @Test
    void unassigned() {
        DeleteInstructorFromCourseRequest request = new DeleteInstructorFromCourseRequest();
        request.setInstructorId(2L);
        request.setCourseId(2L);

        SimpleResponse result = courseService.unassigned(request);

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));

        assertNotNull(result);
        assertThat(course.getInstructors().contains(instructorRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException("Instructor not found")))).isFalse();
    }

    @Test
    void deleteGroupFromCourse() {
        AssignGroupRequest request = new AssignGroupRequest();
        request.setGroupId(2L);
        request.setCourseId(2L);

        SimpleResponse result = courseService.deleteGroupFromCourse(request);

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));

        assertNotNull(result);
        assertThat(course.getGroup().contains(groupRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException("Group not found")))).isFalse();
    }
}