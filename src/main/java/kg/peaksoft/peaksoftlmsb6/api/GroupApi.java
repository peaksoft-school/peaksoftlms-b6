package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.GroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.GroupInnerPage;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Group CRUD")
@PreAuthorize("hasAuthority('ADMIN')")
public class GroupApi {

    private final GroupService groupService;

    @PostMapping()
    @Operation(description = "ADMIN can create group")
    public GroupResponse createGroup(@RequestBody GroupRequest request) {
        return groupService.createGroup(request);
    }

    @PutMapping("{id}")
    @Operation(description = "ADMIN can update group by id")
    public GroupResponse updateGroup(@PathVariable("id") Long id, @RequestBody GroupRequest request) {
        return groupService.updateGroup(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "ADMIN can delete group by id")
    public GroupResponse deleteGroup(@PathVariable("id") Long id) {
        return groupService.deleteGroupById(id);
    }

    @GetMapping("/{id}")
    @Operation(description = "ADMIN get group by id")
    public GroupResponse getGroupById(@PathVariable("id") Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return groupService.getGroupById(id, user);
    }

    @GetMapping()
    @Operation(description = "ADMIN can get all groups")
    public List<GroupResponse> getAllGroups() {
        return groupService.getAllGroups();
    }


    @GetMapping("/students/{id}")
    @Operation(description = "ADMIN can get all students by group id")
    public List<GroupInnerPage> getAllStudentsByGroupId(@PathVariable Long id){
        return groupService.getAllStudentsFromGroup(id);
    }

}

