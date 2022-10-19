package kg.peaksoft.peaksoftlmsb6.service.serviceImpl;

import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Course;
import kg.peaksoft.peaksoftlmsb6.entity.Instructor;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.CourseRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import kg.peaksoft.peaksoftlmsb6.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public CourseResponse createCourse(CourseRequest request) {
        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setCourseImage(request.getImage());
        course.setDateOfStart(request.getDateOfStart());
        course.setCourseDescription(request.getDescription());
        Course course1 = courseRepository.save(course);
        return mapToResponse(course1);
    }

    @Override
    public CourseResponse deleteById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course not found"));
        course.setGroup(null);
        for (Instructor instructor : course.getInstructors()) {
            instructor.setCourses(null);
        }
        courseRepository.delete(course);
        return mapToResponse(course);
    }

    @Override
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course not found"));
        course.setCourseName(request.getCourseName());
        course.setCourseImage(request.getImage());
        course.setDateOfStart(request.getDateOfStart());
        course.setCourseDescription(request.getDescription());
        courseRepository.save(course);
        return mapToResponse(course);
    }

    @Override
    public CourseResponse getCourseById(Long id, User users) {
        User user = userRepository.findByEmail(users.getEmail()).orElseThrow(
                () -> new NotFoundException("User with email %s not found"));
        if (user.getRole().getAuthority().equals("ADMIN")) {
            Course course = courseRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("User with id %d not found"));
            return mapToResponse(course);
        }
        return null;
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return mapToResponses(courses);
    }

    @Override
    public CourseResponse getAllStudentsFromCourse(Long id) {
        return courseRepository.getAllStudentsByCourse(id);
    }

    @Override
    public CourseResponse getAllInstructorsFromCourse(Long id) {
        return courseRepository.getAllInstructorsByCourse(id);
    }

    private CourseResponse mapToResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getCourseName(),
                course.getCourseDescription(),
                course.getDateOfStart(),
                course.getCourseImage());
    }

    private List<CourseResponse> mapToResponses(List<Course> courses) {
        List<CourseResponse> courseResponses = new ArrayList<>();
        for (Course course : courses) {
            courseResponses.add(new CourseResponse(
                    course.getId(),
                    course.getCourseName(),
                    course.getCourseDescription(),
                    course.getDateOfStart(),
                    course.getCourseImage()));
        }
        return courseResponses;
    }
}
