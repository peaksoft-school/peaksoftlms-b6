package kg.peaksoft.peaksoftlmsb6.dto.response;

import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionStudentResponse {
    private Long id;
    private String question;
    private QuestionType questionType;
    private List<OptionStudentResponse> optionResponses;
}
