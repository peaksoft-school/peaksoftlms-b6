package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.AssignInstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AssignInstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.AssignStudentResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.CourseRepository;
import kg.peaksoft.peaksoftlmsb6.repository.InstructorRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final InstructorRepository instructorRepository;

    public CourseResponse createCourse(CourseRequest request) {
        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setCourseImage(request.getImage());
        course.setDateOfStart(request.getDateOfStart());
        course.setCourseDescription(request.getDescription());
        Course course1 = courseRepository.save(course);
        return mapToResponse(course1);
    }


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


    public CourseResponse getCourseById(Long id, User users) {
        User user = userRepository.findByEmail(users.getEmail()).orElseThrow(
                () -> new NotFoundException("User with email %s not found"));
        if (user.getRole().getAuthority().equals("ADMIN")) {
            return courseRepository.getCourseById(id).orElseThrow(
                    () -> new NotFoundException("Course with id %d not found"));
        }
        return null;
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.getAllCourses();
    }

    public List<AssignStudentResponse> getAllStudentsFromCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course not found"));
        Group group = course.getGroup();
        List<AssignStudentResponse> assignStudent = new ArrayList<>();
        for (Student student : group.getStudents()) {
            assignStudent.add(new AssignStudentResponse(student.getId(),
                    student.getFirstName() + " " + student.getLastName(),
                    student.getGroup(), student.getStudyFormat()
                    , student.getPhoneNumber(), student.getEmail()));
        }
        return assignStudent;
    }

    public List<AssignInstructorResponse> getAllInstructorsFromCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Not found"));
        List<Instructor> instructors = course.getInstructors();
        List<AssignInstructorResponse> assignResponse = new ArrayList<>();
        for (Instructor instructor : instructors) {
            assignResponse.add(new AssignInstructorResponse(
                    instructor.getId(),
                    instructor.getFirstName() + " " + instructor.getLastName(),
                    instructor.getSpecialization(), instructor.getPhoneNumber(),
                    instructor.getUser().getEmail()));
        }
        return assignResponse;
    }

    public AssignInstructorResponse assignInstructorToCourse(AssignInstructorRequest request) {
        Instructor instructor = instructorRepository.findById(request.getInstructorId()).orElseThrow(
                () -> new NotFoundException("Instructor not found"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));
        instructor.addCourse(course);
        course.addInstructor(instructor);
        String[] fullName = {instructor.getFirstName(), instructor.getLastName()};
        User user = instructor.getUser();
        courseRepository.save(course);
        return new AssignInstructorResponse(instructor.getId(),
                fullName[0] + " " + fullName[1],
                instructor.getSpecialization(),
                instructor.getPhoneNumber(),
                user.getEmail());
    }


    public AssignInstructorResponse cancelAssign(AssignInstructorRequest request) {
        Instructor instructors = instructorRepository.findById(request.getInstructorId()).orElseThrow(
                () -> new NotFoundException("Instructor not found"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));
        for (Instructor instructor : course.getInstructors()) {
            instructor.setCourses(null);
        }
        courseRepository.save(course);
        instructorRepository.save(instructors);
        String[] fullName = {instructors.getFirstName(), instructors.getLastName()};
        User user = instructors.getUser();
        return new AssignInstructorResponse(instructors.getId(),
                fullName[0] + " " + fullName[1],
                instructors.getSpecialization(),
                instructors.getPhoneNumber(),
                user.getEmail());
    }


    private CourseResponse mapToResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getCourseName(),
                course.getCourseDescription(),
                course.getDateOfStart(),
                course.getCourseImage());
    }
}
