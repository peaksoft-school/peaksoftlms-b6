package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.StudentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import kg.peaksoft.peaksoftlmsb6.entity.Student;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.StudentRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;


    public StudentResponse createStudent(StudentRequest studentRequest) {
        studentRequest.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        Group group = groupRepository.findById(studentRequest.getGroupId()).orElseThrow(
                () -> new NotFoundException(String.format("Group not found with id=%s", studentRequest.getGroupId())));
        Student student = new Student(studentRequest);
        group.addStudents(student);
        student.setGroup(group);
        studentRepository.save(student);
        return studentRepository.getStudent(student.getId());
    }

    public StudentResponse update(Long id, StudentRequest studentRequest) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Student with id =%s not found", studentRequest.getId())));
        Group group = groupRepository.findById(studentRequest.getGroupId()).orElseThrow(
                () -> new NotFoundException(String.format("Group not found with id=%s", studentRequest.getGroupId())));
        student.setGroup(group);
        group.addStudents(student);
        studentRepository.update(
                student.getId(),
                studentRequest.getFirstName(),
                studentRequest.getLastName(),
                studentRequest.getStudyFormat(),
                studentRequest.getPhoneNumber());
        student.getUser().setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        User user = userRepository.findById(student.getUser().getId())
                .orElseThrow(() -> new NotFoundException(String.format("User with id =%s not found", student.getUser().getId())));
        user.setEmail(studentRequest.getEmail());
        user.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        studentRepository.save(student);
        return studentRepository.getStudent(student.getId());
    }

    public SimpleResponse deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Not found"));
        studentRepository.delete(student);
        return new SimpleResponse(String.format("student with id = %s deleted",id));
    }

    public List<StudentResponse> getAllStudent() {
        return studentRepository.getAllStudents();
    }


    public List<StudentResponse> importExcel(MultipartFile files, Long groupId) throws IOException {
        List<Student> students = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
        XSSFSheet wordSheet = workbook.getSheetAt(0);

        for (int index = 0; index < wordSheet.getPhysicalNumberOfRows(); index++) {
            if (index > 0) {
                Student student = new Student();
                XSSFRow row = wordSheet.getRow(index);

                if(row.getCell(0)==null){
                    throw new BadRequestException("FirstName in line index "+index+" is empty!");
                }else {
                    student.setFirstName(row.getCell(0).getStringCellValue());
                }

                if(row.getCell(1)==null){
                    throw new BadRequestException("LastName in line index "+index+" is empty!");
                }else {
                    student.setLastName(row.getCell(1).getStringCellValue());
                }

                if(row.getCell(2)==null){
                    throw new BadRequestException("Phone number in line index "+index+" is empty!");
                }else {
                    student.setPhoneNumber(String.valueOf((long) row.getCell(2).getNumericCellValue()));
                }

                if(row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)==null){
                    throw new BadRequestException("Study format in line index "+index+" is empty!");
                }else {
                    student.setStudyFormat(StudyFormat.valueOf(row.getCell(3).getStringCellValue()));
                }
                User user = new User();

                if(row.getCell(4,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)==null){
                    throw new BadRequestException("Email in line index "+index+" is empty!");
                }else {
                    user.setEmail(row.getCell(4).getStringCellValue());
                }

                if(row.getCell(5)==null){
                    throw new BadRequestException("Role in line index "+index+" is empty!");
                }else {
                    user.setRole(Role.valueOf(row.getCell(5).getStringCellValue()));
                }

                if(row.getCell(6)==null){
                    throw new BadRequestException("Password in line index "+index+" is empty!");
                }else {
                    user.setPassword(passwordEncoder.encode(String.valueOf(row.getCell(6).getStringCellValue())));
                }

                if(row.getCell(7)==null){
                    throw new BadRequestException("IsBlocked in line index "+index+" is empty!");
                }else {
                    user.setIsBlock(Boolean.valueOf(row.getCell(7).getStringCellValue()));
                }

                student.setUser(user);
                students.add(student);
            }
        }

        for (Student student : students) {
            Group groupEntity = groupRepository.findById(groupId).orElseThrow(
                    () -> new NotFoundException(String.format("Group with id =%s not found",groupId)));
            student.setGroup(groupEntity);
            studentRepository.save(student);
        }

        List<StudentResponse> studentResponses = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            studentResponses.add(studentRepository.getStudent(student.getId()));
        }

        return studentResponses;
    }

}