package kg.peaksoft.peaksoftlmsb6.service.serviceImpl;

import kg.peaksoft.peaksoftlmsb6.dto.request.GroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import kg.peaksoft.peaksoftlmsb6.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;


    @Override
    public List<GroupResponse> getAllGroups() {
        return groupRepository.getAllGroups();
    }

    @Override
    public GroupResponse createGroup(GroupRequest group) {
        Group group1 = new Group();
        group1.setGroupName(group.getGroupName());
        group1.setGroupDescription(group.getDescription());
        group1.setDateOfStart(group.getDateOfStart());
        group1.setGroupImage(group.getGroupImage());
        groupRepository.save(group1);
        return mapToResponse(group1);
    }

    @Override
    public GroupResponse getGroupById(Long id, User user) {
        User user1 = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new NotFoundException("User with email %s not found"));
        if (user1.getRole().getAuthority().equals("ADMIN")) {
            return groupRepository.getGroupById(id).orElseThrow(() -> new NotFoundException("Group with id %d not found"));
        }
        return null;
    }

    @Override
    public GroupResponse updateGroup(Long id, GroupRequest request) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Group not found"));
        group.setGroupName(request.getGroupName());
        group.setGroupDescription(request.getDescription());
        group.setGroupImage(request.getGroupImage());
        group.setDateOfStart(request.getDateOfStart());
        groupRepository.save(group);
        return mapToResponse(group);
    }

    @Override
    public GroupResponse deleteGroupById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Group not found"));
        groupRepository.delete(group);
        return mapToResponse(group);
    }

    private GroupResponse mapToResponse(Group group) {
        return new GroupResponse(
                group.getId(),
                group.getGroupName(),
                group.getGroupDescription(),
                group.getDateOfStart(),
                group.getGroupImage());
    }

}
