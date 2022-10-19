package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
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

    @PostMapping("/create/")
    @Operation(description = "ADMIN create a course by course name, " +
            "date of start, description and image specified in the course request")
    public CourseResponse createCourse(@RequestBody CourseRequest request) {
        return courseService.createCourse(request);
    }

    @PutMapping("/update/{id}")
    @Operation(description = "ADMIN can update course by id and fields which is specified in the course request")
    public CourseResponse updateCourse(@PathVariable("id") Long id, @RequestBody CourseRequest request) {
        return courseService.updateCourse(id, request);
    }

    @GetMapping("/getCourse/{id}")
    @Operation(description = "ADMIN get course by id")
    public CourseResponse getCourseById(@PathVariable("id") Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return courseService.getCourseById(id, user);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "ADMIN can delete course by id")
    public CourseResponse deleteCourse(@PathVariable("id") Long id) {
        return courseService.deleteById(id);
    }

    @GetMapping("/getInstructors/{id}")
    @Operation(description = "ADMIN can get all instructors in the course")
    public CourseResponse getInstructorsFromCourse(@PathVariable("id") Long id) {
        return courseService.getAllInstructorsFromCourse(id);
    }

    @GetMapping("/getStudents/{id}")
    @Operation(description = "ADMIN can get all students in the course")
    public CourseResponse getStudentsFromCourse(@PathVariable("id") Long id) {
        return courseService.getAllStudentsFromCourse(id);
    }

    @GetMapping("/all")
    @Operation(description = "ADMIN can get all courses")
    public List<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

}
