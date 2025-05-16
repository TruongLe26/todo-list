package com.leqtr.todolist.controller;

import com.leqtr.shared.dto.TodoItemDTO;
import com.leqtr.todolist.configuration.SecurityUtil;
import com.leqtr.todolist.service.KafkaService;
import com.leqtr.todolist.service.TodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TodoItemController {

    private final TodoItemService todoItemService;
    private final KafkaService kafkaService;
    private final SecurityUtil securityUtil;

    @GetMapping("/")
    public ModelAndView viewHomePage(Model model) {
        return findPaginated(1, "title", "asc", model);
    }

    @GetMapping("/showNewTodoItemForm")
    public ModelAndView showNewTodoItemForm() {
        ModelAndView register = new ModelAndView("new_todoitem");
        TodoItemDTO todoItemDTO = new TodoItemDTO();
        register.addObject("todoitem", todoItemDTO);
        return register;
    }

    @GetMapping("/todoitems/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TodoItemDTO getTodoItemById(@PathVariable String id) {
        return todoItemService.getTodoItemById(id);
    }

    @PostMapping("/saveTodoItem")
    public ModelAndView saveTodoItem(@ModelAttribute("todoitem") TodoItemDTO todoItemDto) {
        kafkaService.createTodoItem(todoItemDto);
        return new ModelAndView("redirect:/");
    }

    @PostMapping("/updateTodoItem")
    public ModelAndView updateTodoItem(@ModelAttribute("todoitem") TodoItemDTO todoItemDto) {
        todoItemService.updateTodoItem(todoItemDto);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable(value = "id") String id, Model model) {
        ModelAndView form = new ModelAndView("update_todoitem");
        TodoItemDTO todoItemDTO = todoItemService.getTodoItemById(id);
        form.addObject("todoitem", todoItemDTO);
        return form;
    }

    @GetMapping("/deleteTodoItem/{id}")
    public ModelAndView deleteTodoItem(@PathVariable(value = "id") String id) {
        todoItemService.deleteTodoItemById(id);
        return new ModelAndView("redirect:/");
    }

    @PostMapping("/deleteTodoItems")
    public ModelAndView batchDeleteTodoItem(@RequestParam("selectedIds") List<String> selectedIds) {
        System.out.println("Selected IDs: " + selectedIds);
        todoItemService.deleteTodoItems(selectedIds);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/page/{pageNo}")
    public ModelAndView findPaginated(
            @PathVariable(value = "pageNo") int pageNo,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model
    ) {
        int pageSize = 5;

        // **
        Page<TodoItemDTO> page = todoItemService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<TodoItemDTO> listTodoItems = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listTodoItems", listTodoItems);
        loggingMessage("Request received!");
        List<TodoItemDTO> completedTodoItems = todoItemService.getCompletedTodoItem();
        loggingMessage("Data retrieved!");
        model.addAttribute("completedTodoItems", completedTodoItems);

        String username = securityUtil.getInfo();
        model.addAttribute("curUser", username);

        return new ModelAndView("index");
    }

    private void loggingMessage(String message) {
        System.out.printf("[%s] %s%n", java.time.LocalTime.now().truncatedTo(ChronoUnit.MILLIS), message);
    }
}
