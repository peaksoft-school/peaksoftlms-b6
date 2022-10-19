package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GroupRequest {
    private String groupName;
    private LocalDate dateOfStart;
    private String description;
    private String groupImage;
}
