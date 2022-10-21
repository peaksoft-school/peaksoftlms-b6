package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.AssignInstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AssignInstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.AssignStudentResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Course Layout", description = "ADMIN can do a CRUD operations")
@PreAuthorize("hasAuthority('ADMIN')")
public class CourseApi {

    private final CourseService courseService;

    @PostMapping
    @Operation(summary = "Create course",
            description = "Admin create course by course request")
    public CourseResponse createCourse(@RequestBody CourseRequest request) {
        return courseService.createCourse(request);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update course",
            description = "Admin update course by id and course request")
    public CourseResponse updateCourse(@PathVariable("id") Long id, @RequestBody CourseRequest request) {
        return courseService.updateCourse(id, request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by id",
            description = "Admin get course by id")
    public CourseResponse getCourseById(@PathVariable("id") Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return courseService.getCourseById(id, user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course",
            description = "Admin delete course by id")
    public CourseResponse deleteCourse(@PathVariable("id") Long id) {
        return courseService.deleteById(id);
    }

    @GetMapping("/instructors/{id}")
    @Operation(summary = "Get instructors from course",
            description = "Admin get instructors from course by course id")
    public List<AssignInstructorResponse> getInstructorsFromCourse(@PathVariable("id") Long id) {
        return courseService.getAllInstructorsFromCourse(id);
    }

    @GetMapping("/students/{id}")
    @Operation(summary = "Get students from course",
            description = "Admin get students from course by course id")
    public List<AssignStudentResponse> getStudentsFromCourse(@PathVariable("id") Long id) {
        return courseService.getAllStudentsFromCourse(id);
    }

    @GetMapping
    @Operation(summary = "Get all courses",
            description = "Admin get all courses")
    public List<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping("/assign")
    @Operation(summary = "Assign instructor to course",
            description = "Admin assign instructor to course by their id")
    public AssignInstructorResponse assignInstructorToCourse(@RequestBody AssignInstructorRequest request) {
        return courseService.assignInstructorToCourse(request);
    }

    @PostMapping("/cancelAssign")
    @Operation(summary = "Cancel assign instructor to course",
            description = "Admin cancel assign instructor to course by id")
    public AssignInstructorResponse cancelAssignInstructorToCourse(@RequestBody AssignInstructorRequest request) {
        return courseService.cancelAssign(request);
    }

}
