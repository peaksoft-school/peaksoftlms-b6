package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.entity.User;

import java.util.List;

public interface CourseService {

    CourseResponse createCourse(CourseRequest request);
    CourseResponse deleteById(Long id);
    CourseResponse updateCourse(Long id, CourseRequest request);
    CourseResponse getCourseById(Long id, User user);
    CourseResponse getAllStudentsFromCourse(Long id);
    CourseResponse getAllInstructorsFromCourse(Long id);
    List<CourseResponse> getAllCourses();

}
