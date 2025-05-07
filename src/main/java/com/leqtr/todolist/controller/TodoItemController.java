package com.leqtr.todolist.controller;

import com.leqtr.todolist.configuration.SecurityUtil;
import com.leqtr.todolist.model.TodoItem;
import com.leqtr.todolist.service.TodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoItemController {

    private final TodoItemService todoItemService;

    private final SecurityUtil securityUtil;

    @GetMapping("/")
    public ModelAndView viewHomePage(Model model) {
        return findPaginated(1, "title", "asc", model);
    }

    @GetMapping("/showNewTodoItemForm")
    public ModelAndView showNewTodoItemForm() {
        ModelAndView register = new ModelAndView("new_todoitem");
        TodoItem todoItem = new TodoItem();
        register.addObject("todoitem", todoItem);
        return register;
    }

    @PostMapping("/saveTodoItem")
    public ModelAndView saveTodoItem(@ModelAttribute("todoitem") TodoItem todoItem) {
        TodoItem savedTodoItem = todoItemService.saveTodoItem(todoItem);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
        ModelAndView form = new ModelAndView("update_todoitem");
        TodoItem todoItem = todoItemService.getTodoItemById(id);
        form.addObject("todoitem", todoItem);
        return form;
    }

    @GetMapping("/deleteTodoItem/{id}")
    public ModelAndView deleteTodoItem(@PathVariable(value = "id") long id) {
        todoItemService.deleteTodoItemById(id);
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

        Page<TodoItem> page = todoItemService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<TodoItem> listTodoItems = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listTodoItems", listTodoItems);
        loggingMessage("Request received!");
        List<TodoItem> completedTodoItems = todoItemService.getCompletedTodoItem();
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
