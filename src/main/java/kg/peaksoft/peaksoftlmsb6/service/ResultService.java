package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.PassTestRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.*;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final StudentRepository studentRepository;

    public StudentResultResponse passTest(PassTestRequest passTestRequest, Authentication authentication) {
        Test test = testRepository.findById(passTestRequest.getTestId()).orElseThrow(
                () -> new NotFoundException("Тест не найден"));
        User user = (User) authentication.getPrincipal();
        User user1 = userRepository.findById(user.getId()).orElseThrow(
                () -> new NotFoundException("Пользователь не найден"));
        List<QuestionResponse> mapper = new ArrayList<>();
        List<Question> map = new ArrayList<>();
        int percent = 0;
        for (Map.Entry<Long, List<Long>> answer : passTestRequest.getAnswers().entrySet()) {
            Question question = questionRepository.findById(answer.getKey()).orElseThrow(
                    () -> new NotFoundException("Вопрос не найден"));
            if (question.getQuestionType().equals(QuestionType.SINGLETON)) {
                question.setOptions(null);
                for (Long optionId : answer.getValue()) {
                    Option option = optionRepository.findById(optionId).orElseThrow(
                            () -> new NotFoundException("Вариант не найден"));
                    if (option.getIsTrue().equals(true)) {
                        percent += 100 % test.getQuestion().size();
                    }
                    question.addOption(option);
                }
                map.add(question);
            } else if (question.getQuestionType().equals(QuestionType.MULTIPLE)) {
                int countOfCorrect = 0;
                int counter = 0;
                for (Option o : question.getOptions()) {
                    Option option = optionRepository.findById(o.getId()).orElseThrow(
                            () -> new NotFoundException("Вариант не найден"));
                    if (option.getIsTrue().equals(true)) {
                        counter++;
                    }
                }
                int point = 100 % test.getQuestion().size();
                Long duplicate = 0L;
                question.setOptions(null);
                for (Long optionId : answer.getValue()) {
                    Option option = optionRepository.findById(optionId).orElseThrow(
                            () -> new NotFoundException("Вариант не найден"));
                    if(option.getId().equals(duplicate)) {
                        throw new BadRequestException("Нельзя выбирать один вариант несколько раз");
                    }
                    if (option.getIsTrue().equals(true)) {
                        countOfCorrect++;
                    } else {
                        countOfCorrect--;
                    }
                    question.addOption(option);
                    duplicate = option.getId();
                }
                if (countOfCorrect < 0) {
                    countOfCorrect = 0;
                }
                if (countOfCorrect == 0) {
                    percent += 0;
                } else if (countOfCorrect == counter) {
                    percent += point;
                } else if (countOfCorrect < counter) {
                    percent += (point % counter) * countOfCorrect;
                }
                map.add(question);
            }
        }
        for (Question question : map) {
            QuestionResponse questionResponse = new QuestionResponse(question);
            List<OptionResponse> optionResponses = new ArrayList<>();
            for (Option option : question.getOptions()) {
                optionResponses.add(new OptionResponse(option));
            }
            questionResponse.setOptionResponses(optionResponses);
            mapper.add(questionResponse);
        }
        Student student = null;
        if (user1.getRole().equals(Role.STUDENT)) {
            student = studentRepository.findByUserId(user1.getId()).orElseThrow(
                    () -> new NotFoundException("Студент не найден"));
            if (test.getIsEnable().equals(true)) {
                Results results = new Results(
                        test,
                        LocalDate.now(),
                        percent,
                        student);
                resultRepository.save(results);
            } else if (test.getIsEnable().equals(false)) {
                throw new BadRequestException("Вы не можете пойти тест");
            }
        }
        return new StudentResultResponse("Набрано баллов "
                + percent + " из " + test.getQuestion().size(), mapper);
    }


    public List<ResultResponse> getAllResults(Long id) {
        Test test = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Тест не найден"));
        return mapToResponse(resultRepository.findResultByTestId(test.getId()));
    }

    public ResultResponse getById(Long id) {
        Results results = resultRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Результат не найден"));
        return resultRepository.getResult(results.getId());
    }

    private List<ResultResponse> mapToResponse(List<Results> results) {
        List<ResultResponse> resultResponses = new ArrayList<>();
        for (Results result : results) {
            resultResponses.add(new ResultResponse(result));
        }
        return resultResponses;
    }
}

//    public StudentResultResponse passTest(PassTestRequest passTestRequest, Authentication authentication) {
//        Test test = testRepository.findById(passTestRequest.getTestId()).orElseThrow(
//                () -> new NotFoundException("Test not found"));
//        User user = (User) authentication.getPrincipal();
//        User user1 = userRepository.findById(user.getId()).orElseThrow(
//                () -> new NotFoundException("User not found"));
//        int amountOfCorrectAnswers = 0;
//        Map<Long, List<Long>> mapper = new HashMap<>();
//        for (Map.Entry<Long, List<Long>> answer : passTestRequest.getAnswers().entrySet()) {
//            List<Long> amount = new ArrayList<>();
//            Question question = questionRepository.findById(answer.getKey()).orElseThrow(
//                    () -> new NotFoundException("Question not found"));
//            if (question.getQuestionType().equals(QuestionType.SINGLETON)) {
//                for (Long optionId : answer.getValue()) {
//                    Option option = optionRepository.findById(optionId).orElseThrow(
//                            () -> new NotFoundException("Option not found"));
//                    if (option.getIsTrue()) {
//                        amountOfCorrectAnswers++;
//                    }
//                }
//            } else if (question.getQuestionType().equals(QuestionType.MULTIPLE)) {
//                long counter = 0L;
//                for (Long optionId : answer.getValue()) {
//                    Option option = optionRepository.findById(optionId).orElseThrow(
//                            () -> new NotFoundException("Option not found"));
//                    if (option.getIsTrue().equals(true)) {
//                        amountOfCorrectAnswers++;
//                        counter++;
//                    } else if (option.getIsTrue().equals(false) && amountOfCorrectAnswers != 0) {
////                        amountOfCorrectAnswers--;
//                        counter--;
//                    }
//                }
//                if (counter < 0) {
//                    counter = 0;
//                    amount.add(counter);
//                    mapper.put(answer.getKey(), amount);
//                } else {
//                    amount.add(counter);
//                    mapper.put(answer.getKey(), amount);
//                }
//            }
//        }
//        Student student = null;
//        if (user1.getRole().equals(Role.STUDENT)) {
//            student = studentRepository.findByUserId(user1.getId()).orElseThrow(
//                    () -> new NotFoundException("Student not found"));
//            if (test.getIsEnable().equals(true)) {
//                Results results = new Results(
//                        test,
//                        LocalDate.now(),
//                        amountOfCorrectAnswers,
//                        student);
//                resultRepository.save(results);
//            } else if (test.getIsEnable().equals(false)) {
//                throw new BadRequestException("You can't pass the test");
//            }
//        }
//        return new StudentResultResponse("Набрано баллов "
//                + amountOfCorrectAnswers + " из " + test.getQuestion().size(), mapper);
//    }
