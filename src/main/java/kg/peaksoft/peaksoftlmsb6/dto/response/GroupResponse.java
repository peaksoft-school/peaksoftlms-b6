package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class GroupResponse {
    private Long id;

    private String groupName;

    @Column(length = 100000)
    private String groupDescription;

    private LocalDate dateOfStart;

    private String groupImage;

}
