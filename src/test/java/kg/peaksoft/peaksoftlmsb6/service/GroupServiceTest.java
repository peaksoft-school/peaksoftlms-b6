package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.GroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.StudentExcelRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import kg.peaksoft.peaksoftlmsb6.entity.Results;
import kg.peaksoft.peaksoftlmsb6.entity.Student;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.ResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class GroupServiceTest {

    @Mock
    private GroupRepository mockGroupRepository;
    @Mock
    private ResultRepository mockResultRepository;

    private GroupService groupServiceUnderTest;

    @BeforeEach
    void setUp() {
        groupServiceUnderTest = new GroupService(mockGroupRepository, mockResultRepository);
    }

    @Test
    void testCreateGroup() {
        // Setup
        final GroupRequest request = new GroupRequest();
        request.setGroupName("groupName");
        request.setDescription("description");
        request.setDateOfStart(LocalDate.of(2020, 1, 1));
        request.setImage("image");

        // Configure GroupRepository.save(...).
        final GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupName("groupName");
        groupRequest.setDescription("description");
        groupRequest.setDateOfStart(LocalDate.of(2020, 1, 1));
        groupRequest.setImage("image");
        final Group group = new Group(groupRequest);
        when(mockGroupRepository.save(any(Group.class))).thenReturn(group);

        // Configure GroupRepository.getGroup(...).
        final GroupResponse groupResponse = new GroupResponse(0L, "groupName", "description", LocalDate.of(2020, 1, 1),
                "image");
        when(mockGroupRepository.getGroup(0L)).thenReturn(groupResponse);

        // Run the test
        final GroupResponse result = groupServiceUnderTest.createGroup(request);

        // Verify the results
        verify(mockGroupRepository).save(any(Group.class));
    }

    @Test
    void testDeleteById() {
        // Setup
        // Configure GroupRepository.findById(...).
        final GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupName("groupName");
        groupRequest.setDescription("description");
        groupRequest.setDateOfStart(LocalDate.of(2020, 1, 1));
        groupRequest.setImage("image");
        final Optional<Group> group = Optional.of(new Group(groupRequest));
        when(mockGroupRepository.findById(0L)).thenReturn(group);

        // Configure ResultRepository.findResultByStudentsId(...).
        final StudentExcelRequest studentExcelRequest = new StudentExcelRequest();
        studentExcelRequest.setIndexRow(0);
        studentExcelRequest.setName("name");
        studentExcelRequest.setLastName("lastName");
        studentExcelRequest.setPhoneNumber("phoneNumber");
        studentExcelRequest.setStudyFormat(StudyFormat.ONLINE);
        studentExcelRequest.setEmail("email");
        final Results results = new Results(new kg.peaksoft.peaksoftlmsb6.entity.Test("testName"), 0, 0,
                LocalDate.of(2020, 1, 1), 0, new Student(studentExcelRequest, "encode"));
        when(mockResultRepository.findResultByStudentsId(0L)).thenReturn(results);

        // Run the test
        final SimpleResponse result = groupServiceUnderTest.deleteById(0L);

        // Verify the results
        verify(mockResultRepository).deleteById(0L);
        verify(mockGroupRepository).delete(any(Group.class));
    }

    @Test
    void testDeleteById_GroupRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockGroupRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupServiceUnderTest.deleteById(0L)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testDeleteById_ResultRepositoryFindResultByStudentsIdReturnsNull() {
        // Setup
        // Configure GroupRepository.findById(...).
        final GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupName("groupName");
        groupRequest.setDescription("description");
        groupRequest.setDateOfStart(LocalDate.of(2020, 1, 1));
        groupRequest.setImage("image");
        final Optional<Group> group = Optional.of(new Group(groupRequest));
        when(mockGroupRepository.findById(0L)).thenReturn(group);

        when(mockResultRepository.findResultByStudentsId(0L)).thenReturn(null);

        // Run the test
        final SimpleResponse result = groupServiceUnderTest.deleteById(0L);

        // Verify the results
        verify(mockResultRepository).deleteById(0L);
        verify(mockGroupRepository).delete(any(Group.class));
    }

    @Test
    void testUpdateGroup() {
        // Setup
        final GroupRequest request = new GroupRequest();
        request.setGroupName("groupName");
        request.setDescription("description");
        request.setDateOfStart(LocalDate.of(2020, 1, 1));
        request.setImage("image");

        // Configure GroupRepository.findById(...).
        final GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupName("groupName");
        groupRequest.setDescription("description");
        groupRequest.setDateOfStart(LocalDate.of(2020, 1, 1));
        groupRequest.setImage("image");
        final Optional<Group> group = Optional.of(new Group(groupRequest));
        when(mockGroupRepository.findById(0L)).thenReturn(group);

        // Run the test
        final GroupResponse result = groupServiceUnderTest.updateGroup(0L, request);

        // Verify the results
        verify(mockGroupRepository).update(0L, "groupName", "description", LocalDate.of(2020, 1, 1), "image");
    }

    @Test
    void testUpdateGroup_GroupRepositoryFindByIdReturnsAbsent() {
        // Setup
        final GroupRequest request = new GroupRequest();
        request.setGroupName("groupName");
        request.setDescription("description");
        request.setDateOfStart(LocalDate.of(2020, 1, 1));
        request.setImage("image");

        when(mockGroupRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupServiceUnderTest.updateGroup(0L, request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testGetAllStudentsFromGroup() {
        // Setup
        // Configure GroupRepository.findById(...).
        final GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupName("groupName");
        groupRequest.setDescription("description");
        groupRequest.setDateOfStart(LocalDate.of(2020, 1, 1));
        groupRequest.setImage("image");
        final Optional<Group> group = Optional.of(new Group(groupRequest));
        when(mockGroupRepository.findById(0L)).thenReturn(group);

        // Configure GroupRepository.getStudentsByGroupId(...).
        final List<StudentResponse> studentResponses = List.of(
                new StudentResponse(0L, "fullName", "groupName", StudyFormat.ONLINE, "phoneNumber", "email"));
        when(mockGroupRepository.getStudentsByGroupId(0L)).thenReturn(studentResponses);

        // Run the test
        final List<StudentResponse> result = groupServiceUnderTest.getAllStudentsFromGroup(0L);

        // Verify the results
    }

    @Test
    void testGetAllStudentsFromGroup_GroupRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockGroupRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupServiceUnderTest.getAllStudentsFromGroup(0L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testGetAllStudentsFromGroup_GroupRepositoryGetStudentsByGroupIdReturnsNoItems() {
        // Setup
        // Configure GroupRepository.findById(...).
        final GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupName("groupName");
        groupRequest.setDescription("description");
        groupRequest.setDateOfStart(LocalDate.of(2020, 1, 1));
        groupRequest.setImage("image");
        final Optional<Group> group = Optional.of(new Group(groupRequest));
        when(mockGroupRepository.findById(0L)).thenReturn(group);

        when(mockGroupRepository.getStudentsByGroupId(0L)).thenReturn(Collections.emptyList());

        // Run the test
        final List<StudentResponse> result = groupServiceUnderTest.getAllStudentsFromGroup(0L);

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetAllGroups() {
        // Setup
        // Configure GroupRepository.getAllGroups(...).
        final List<GroupResponse> groupResponses = List.of(
                new GroupResponse(0L, "groupName", "description", LocalDate.of(2020, 1, 1), "image"));
        when(mockGroupRepository.getAllGroups()).thenReturn(groupResponses);

        // Run the test
        final List<GroupResponse> result = groupServiceUnderTest.getAllGroups();

        // Verify the results
    }

    @Test
    void testGetAllGroups_GroupRepositoryReturnsNoItems() {
        // Setup
        when(mockGroupRepository.getAllGroups()).thenReturn(Collections.emptyList());

        // Run the test
        final List<GroupResponse> result = groupServiceUnderTest.getAllGroups();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetById() {
        // Setup
        // Configure GroupRepository.findById(...).
        final GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupName("groupName");
        groupRequest.setDescription("description");
        groupRequest.setDateOfStart(LocalDate.of(2020, 1, 1));
        groupRequest.setImage("image");
        final Optional<Group> group = Optional.of(new Group(groupRequest));
        when(mockGroupRepository.findById(0L)).thenReturn(group);

        // Configure GroupRepository.getGroup(...).
        final GroupResponse groupResponse = new GroupResponse(0L, "groupName", "description", LocalDate.of(2020, 1, 1),
                "image");
        when(mockGroupRepository.getGroup(0L)).thenReturn(groupResponse);

        // Run the test
        final GroupResponse result = groupServiceUnderTest.getById(0L);

        // Verify the results
    }

    @Test
    void testGetById_GroupRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockGroupRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> groupServiceUnderTest.getById(0L)).isInstanceOf(NoSuchElementException.class);
    }
}
