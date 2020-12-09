package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final String DEFAULT_SIZE = "20";
    private final String DEFAULT_PAGE = "1";
    private final String DEFAULT_SORT = "id";
    private final String DEFAULT_SORT_MODE = "asc";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User one(@PathVariable int id) {
        return userService.getById(id);
    }

    @GetMapping
    public Pagination<User> all(@RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
                                @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
                                @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
                                @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        return userService.getAll(page, size, sort, sortMode);
    }

    @GetMapping("/search")
    public Pagination<User> search(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_SIZE) int size,
            @RequestParam(name = "sort", required = false, defaultValue = DEFAULT_SORT) String sort,
            @RequestParam(name = "sort_mode", required = false, defaultValue = DEFAULT_SORT_MODE) String sortMode) {
        //TODO search logic
        return null;
    }

    @PostMapping
    public User add(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public User update(@RequestBody GiftCertificate certificate, @PathVariable int id) {
        return userService.addCertificate(certificate, id);
    }

    @DeleteMapping("/{id}")
    public String Delete(@PathVariable int id) {
        userService.delete(id);

        return "Deleted";
    }
}
