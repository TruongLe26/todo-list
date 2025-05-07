package com.leqtr.todolist.controller;

import com.leqtr.todolist.configuration.SecurityUtil;
import com.leqtr.todolist.model.*;
import com.leqtr.todolist.service.GroupService;
import com.leqtr.todolist.service.NotificationService;
import com.leqtr.todolist.service.TodoItemService;
import com.leqtr.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final UserService userService;

    private final GroupService groupService;

    private final TodoItemService todoItemService;

    private final NotificationService notificationService;

    private final SecurityUtil securityUtil;

//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public ModelAndView viewGroupPage(Model model) {
        return findPaginated(1, "title", "asc", model);
    }

    @GetMapping("/viewMembers/{id}")
    public ModelAndView viewMembers(@PathVariable(value = "id") long id, Model model) {
        return findPaginatedUser(id, 1, "email", "asc", model);
    }

    @GetMapping("/showNewGroupForm")
    public ModelAndView showNewGroupForm() {
        ModelAndView registerGroup = new ModelAndView("new_group");
        Group group = new Group();
        registerGroup.addObject("group", group);
        return registerGroup;
    }

    @PostMapping("/createGroup")
    public ModelAndView createGroup(@ModelAttribute("group") Group group) {
        groupService.createGroup(group);
        return new ModelAndView("redirect:/groups");
    }

    @GetMapping("/showNewMemberForm/{id}")
    public ModelAndView showNewMemberForm(@PathVariable(value = "id") long id) {
        ModelAndView newMem = new ModelAndView("new_member");
        NewMemberForm newMemberForm = new NewMemberForm();
        newMem.addObject("newMemberForm", newMemberForm);
        newMem.addObject("id", id);
        return newMem;
    }

    @PostMapping("/saveNewMember/{id}")
    public ModelAndView saveNewMember(@PathVariable(value = "id") long id,
                                      @ModelAttribute(value = "newMemberForm") NewMemberForm newMemberForm, Model model) {
        String result = groupService.addNewMemberToGroup(id, newMemberForm.getEmail());

        switch (result) {
            case "success" -> {
                return new ModelAndView("redirect:/groups/viewMembers/{id}");
            }
            case "not-admin" -> model.addAttribute("notAdminWarning", "You are not admin in this group");
            case "already-in-group" -> model.addAttribute("warningMessage", "This user is already in the group");
            case "not-found" -> model.addAttribute("errorMessage", "User not found in the database");
        }

        ModelAndView newMem = new ModelAndView("new_member");
        newMem.addObject("newMemberForm", newMemberForm);
        newMem.addObject("id", id);
        return newMem;

    }

    @GetMapping("/leaveGroup/{id}")
    public ModelAndView leaveGroup(@PathVariable(name = "id") long id) {
        groupService.leaveGroup(id);
        return new ModelAndView("redirect:/groups");
    }

    @GetMapping("/removeUser/{groupId}/{id}")
    public ModelAndView removeUser(@PathVariable(value = "groupId") long groupId, @PathVariable(name = "id") long id) {
        groupService.removeUser(groupId, id);
        return new ModelAndView("redirect:/groups/viewMembers/{groupId}");
    }

    @GetMapping("/promoteUser/{groupId}/{id}")
    public ModelAndView promoteUser(@PathVariable(value = "groupId") long groupId, @PathVariable(name = "id") long id) {
        groupService.promoteUser(groupId, id);
        return new ModelAndView("redirect:/groups/viewMembers/{groupId}");
    }

    @GetMapping("/viewTodoItems/{id}")
    public ModelAndView viewTodoItems(@PathVariable(value = "id") long id, Model model) {
        return findPaginatedTodoItemByGroup(id, 1, "title", "asc", model);
    }

    @GetMapping("/showNewGroupTodoItemForm/{id}")
    public ModelAndView showNewTodoItemForm(@PathVariable(value = "id") long id) {
        ModelAndView register = new ModelAndView("new_grouptodoitem");
        register.addObject("groupId", id);
        TodoItem todoItem = new TodoItem();
        register.addObject("todoitem", todoItem);
        return register;
    }

    @PostMapping("/saveGroupTodoItem/{id}")
    public ModelAndView saveTodoItem(@PathVariable(value = "id") long id, @ModelAttribute("todoitem") TodoItem todoItem) {
        groupService.saveGroupTodoItem(todoItem, id);
        return new ModelAndView("redirect:/groups/viewTodoItems/{id}");
    }

    @PostMapping("/updateGroupTodoItem/{groupId}")
    public ModelAndView updateGroupTodoItem(@PathVariable(value = "groupId") long groupId,
                                            @ModelAttribute("todoitem") TodoItem todoItem) {
        User user = userService.findByEmail(securityUtil.getSessionUser());
        boolean isAdmin = groupService.isUserAdminInGroup(groupId, user.getId());

        if (!isAdmin) {
            String message = user.getEmail() + " has just made change to to-do item \""+ todoItem.getTitle() +
                    "\" in your group \"" + groupService.getGroupById(groupId).getName() + "\"";
            User admin = groupService.findAdminByGroupId(groupId);

            groupService.saveGroupTodoItem(todoItem, groupId);

            Notification notification = new Notification(admin, user.getEmail(), groupId, todoItem.getId(), message);
            notificationService.saveNotification(notification);

//            messagingTemplate.convertAndSendToUser(admin.getEmail(), "/notifications", message);

            return new ModelAndView("redirect:/groups/viewTodoItems/{groupId}");
        }

        groupService.saveGroupTodoItem(todoItem, groupId);
        return new ModelAndView("redirect:/groups/viewTodoItems/{groupId}");
    }

    @GetMapping("/deleteGroupTodoItem/{groupId}/{id}")
    public ModelAndView deleteGroupTodoItem(@PathVariable(value = "groupId") long groupId, @PathVariable(value = "id") long id) {
        groupService.deleteTodoItemFromGroup(groupId, id);
        return new ModelAndView("redirect:/groups/viewTodoItems/{groupId}");
    }

    @GetMapping("/showFormForUpdateGroupTodoItem/{groupId}/{id}")
    public ModelAndView showFormForUpdateGroupTodoItem(@PathVariable(value = "groupId") long groupId,
                                                       @PathVariable(value = "id") long id,
                                                       Model model) {
        model.addAttribute("groupId", groupId);
        ModelAndView form = new ModelAndView("update_grouptodoitem");
        TodoItem todoItem = todoItemService.getTodoItemById(id);
        form.addObject("todoitem", todoItem);
        return form;
    }

    @GetMapping("/page/{pageNo}")
    public ModelAndView findPaginated(
            @PathVariable(value = "pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model
    ) {
        int pageSize = 5;

        Page<Group> page = groupService.findPaginatedGroup(pageNo, pageSize, sortField, sortDir);
        List<Group> groups = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("groups", groups);

        String info = securityUtil.getInfo();
        model.addAttribute("curUser", info);

        return new ModelAndView("groups");
    }

    @GetMapping("/viewMembers/{id}/page/{pageNo}")
    public ModelAndView findPaginatedUser(
            @PathVariable(value = "id") long id,
            @PathVariable(value = "pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model
    ) {
        int pageSize = 5;

        Page<User> page = groupService.findPaginatedUser(id, pageNo, pageSize, sortField, sortDir);
        List<User> mems = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("mems", mems);

        String info = securityUtil.getInfo();
        String user = securityUtil.getSessionUser();
        boolean isAdmin = groupService.getRoleInGroup(id).equals("ADMIN");

        model.addAttribute("curUser", info);
        model.addAttribute("groupId", id);
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", isAdmin);

        return new ModelAndView("view_groupmembers");
    }

    @GetMapping("/viewTodoItems/{id}/page/{pageNo}")
    public ModelAndView findPaginatedTodoItemByGroup(
            @PathVariable(value = "id") long id,
            @PathVariable(value = "pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model
    ) {
        int pageSize = 5;

        Page<TodoItem> page = groupService.findPaginatedTodoItemInGroup(id, pageNo, pageSize, sortField, sortDir);
        List<TodoItem> groupTodoItems = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("groupTodoItems", groupTodoItems);

        String info = securityUtil.getInfo();
        model.addAttribute("curUser", info);
        model.addAttribute("groupId", id);

        return new ModelAndView("view_grouptodoitems");
    }

}