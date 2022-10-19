package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse(" +
            "g.id," +
            "g.groupName," +
            "g.groupDescription," +
            "g.dateOfStart," +
            "g.groupImage)from Group g")
    List<GroupResponse> getAllGroups();

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse(" +
            "g.id," +
            "g.groupName," +
            "g.groupDescription," +
            "g.dateOfStart," +
            "g.groupImage" +
            ") from Group g where g.id = ?1")
    Optional<GroupResponse> getGroupById(Long id);


}