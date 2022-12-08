package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.InstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.UpdateInstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Course;
import kg.peaksoft.peaksoftlmsb6.entity.Instructor;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.InstructorRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock
    private InstructorRepository mockInstructorRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private UserRepository mockUserRepository;

    private InstructorService instructorServiceUnderTest;

    @BeforeEach
    void setUp() {
        instructorServiceUnderTest = new InstructorService(mockInstructorRepository, mockPasswordEncoder,
                mockUserRepository);
    }

    @Test
    void testCreateInstructor() {
        InstructorRequest request = new InstructorRequest("firstName", "lastName", "phoneNumber", "email",
                "specialization", "password");
        when(mockUserRepository.existsByEmail("email")).thenReturn(false);
        when(mockPasswordEncoder.encode("password")).thenReturn("password");

        Instructor instructor = new Instructor(
                new InstructorRequest("firstName", "lastName", "phoneNumber", "email", "specialization", "password"));
        when(mockInstructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        InstructorResponse instructorResponse = new InstructorResponse(0L, "fullName", "phoneNumber",
                "specialization", "email");
        when(mockInstructorRepository.getInstructor(0L)).thenReturn(instructorResponse);

        InstructorResponse result = instructorServiceUnderTest.createInstructor(request);

        verify(mockInstructorRepository).save(any(Instructor.class));
    }

    @Test
    void testUpdateInstructor() {
        UpdateInstructorRequest request = new UpdateInstructorRequest("full name", "phoneNumber", "email",
                "specialization");

        final Optional<Instructor> instructor = Optional.of(new Instructor(
                new InstructorRequest("firstName", "lastName", "phoneNumber", "email", "specialization", "password")));
        when(mockInstructorRepository.findById(0L)).thenReturn(instructor);

        final User user1 = new User();
        user1.setId(0L);
        user1.setEmail("email");
        user1.setPassword("password");
        user1.setRole(Role.ADMIN);
        final Instructor instructor1 = new Instructor();
        instructor1.setId(0L);
        instructor1.setFirstName("firstName");
        instructor1.setLastName("lastName");
        instructor1.setPhoneNumber("phoneNumber");
        instructor1.setSpecialization("specialization");
        final Course course = new Course();
        course.setId(0L);
        course.setCourseName("courseName");
        course.setCourseDescription("courseDescription");
        course.setDateOfStart(LocalDate.of(2020, 1, 1));
        instructor1.setCourses(List.of(course));
        user1.setInstructor(instructor1);
        final Optional<User> user = Optional.of(user1);
        when(mockUserRepository.findById(0L)).thenReturn(user);

        final Instructor instructor2 = new Instructor(
                new InstructorRequest("firstName", "lastName", "phoneNumber", "email", "specialization", "password"));
        when(mockInstructorRepository.save(any(Instructor.class))).thenReturn(instructor2);

        final InstructorResponse instructorResponse = new InstructorResponse(0L, "fullName", "phoneNumber",
                "specialization", "email");
        when(mockInstructorRepository.getInstructor(0L)).thenReturn(instructorResponse);

        final InstructorResponse result = instructorServiceUnderTest.updateInstructor(0L, request);

        verify(mockInstructorRepository).update(0L, "firstName", "lastName", "specialization", "phoneNumber");
        verify(mockInstructorRepository).save(any(Instructor.class));
    }

    @Test
    void testUpdateInstructor_InstructorRepositoryFindByIdReturnsAbsent() {
        // Setup
        final UpdateInstructorRequest request = new UpdateInstructorRequest("fullName", "phoneNumber", "email",
                "specialization");
        when(mockInstructorRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> instructorServiceUnderTest.updateInstructor(0L, request))
                .isInstanceOf(NotFoundException.class);
    }


    @Test
    void testDeleteInstructorById() {
        // Setup
        // Configure InstructorRepository.findById(...).
        final Optional<Instructor> instructor = Optional.of(new Instructor(
                new InstructorRequest("firstName", "lastName", "phoneNumber", "email", "specialization", "password")));
        when(mockInstructorRepository.findById(0L)).thenReturn(instructor);

        // Run the test
        final SimpleResponse result = instructorServiceUnderTest.deleteInstructorById(0L);

        // Verify the results
        verify(mockInstructorRepository).delete(any(Instructor.class));
    }

    @Test
    void testDeleteInstructorById_InstructorRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockInstructorRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> instructorServiceUnderTest.deleteInstructorById(0L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void testGetAllInstructors() {
        // Setup
        // Configure InstructorRepository.getAllInstructors(...).
        final List<InstructorResponse> instructorResponses = List.of(
                new InstructorResponse(0L, "fullName", "phoneNumber", "specialization", "email"));
        when(mockInstructorRepository.getAllInstructors()).thenReturn(instructorResponses);

        // Run the test
        final List<InstructorResponse> result = instructorServiceUnderTest.getAllInstructors();

        // Verify the results
    }

    @Test
    void testGetAllInstructors_InstructorRepositoryReturnsNoItems() {
        // Setup
        when(mockInstructorRepository.getAllInstructors()).thenReturn(Collections.emptyList());

        // Run the test
        final List<InstructorResponse> result = instructorServiceUnderTest.getAllInstructors();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetById() {
        // Setup
        // Configure InstructorRepository.findById(...).
        final Optional<Instructor> instructor = Optional.of(new Instructor(
                new InstructorRequest("firstName", "lastName", "phoneNumber", "email", "specialization", "password")));
        when(mockInstructorRepository.findById(0L)).thenReturn(instructor);

        // Configure InstructorRepository.getInstructor(...).
        final InstructorResponse instructorResponse = new InstructorResponse(0L, "fullName", "phoneNumber",
                "specialization", "email");
        when(mockInstructorRepository.getInstructor(0L)).thenReturn(instructorResponse);

        // Run the test
        final InstructorResponse result = instructorServiceUnderTest.getById(0L);

        // Verify the results
    }

    @Test
    void testGetById_InstructorRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockInstructorRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> instructorServiceUnderTest.getById(0L)).isInstanceOf(NotFoundException.class);
    }
}
