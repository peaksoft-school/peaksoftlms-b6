package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.StudentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import kg.peaksoft.peaksoftlmsb6.entity.Student;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.StudentRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
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
    private final JavaMailSender javaMailSender;

    public StudentResponse createStudent(StudentRequest studentRequest) throws MessagingException {
        Group group = groupRepository.findById(studentRequest.getGroupId()).orElseThrow(
                () -> new NotFoundException(String.format("Group not found with id=%s", studentRequest.getGroupId())));
        Student student = new Student(studentRequest);
        group.addStudents(student);
        student.setGroup(group);
        sendEmail(student.getUser().getEmail());
        student.getUser().setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        studentRepository.save(student);
        return studentRepository.getStudent(student.getId());
    }

    private SimpleResponse sendEmail(String email) throws MessagingException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с email =%s не найден", email)));
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setSubject("[peaksoftlms-b6] reset password link");
        messageHelper.setFrom("peaksoftlms-b6@gmail.com");
        messageHelper.setTo(email);
        messageHelper.setText("Username: " + user.getUsername() + "\tPassword: " + user.getPassword(), true);
        javaMailSender.send(mimeMessage);
        return new SimpleResponse("Письмо успешно отправлено студенту");
    }


    public StudentResponse update(Long id, StudentRequest studentRequest) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Student with id =%s not found", id)));
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
        return new SimpleResponse(String.format("student with id = %s deleted", id));
    }


    public List<StudentResponse> getAllStudent(StudyFormat studyFormat) {
        if (studyFormat.equals(StudyFormat.ALL)) {
            return studentRepository.getAllStudents();
        } else {
            return studentRepository.findStudentByStudyFormat(studyFormat);
        }
    }

}