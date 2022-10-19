package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.GroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse;
import kg.peaksoft.peaksoftlmsb6.entity.User;

import java.util.List;

public interface GroupService {


    GroupResponse createGroup(GroupRequest group);

    GroupResponse deleteGroupById(Long id);

    GroupResponse updateGroup(Long id, GroupRequest group);

    GroupResponse getGroupById(Long id, User user);

    List<GroupResponse> getAllGroups();

}
