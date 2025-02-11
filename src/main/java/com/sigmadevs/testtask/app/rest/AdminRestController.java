package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.dto.UserDTO;
import com.sigmadevs.testtask.app.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminRestController {

    private final AdminService adminService;
    @GetMapping("/admin/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {

        return adminService.getAllUsers(page, size)
                .stream().toList();
    }

    @GetMapping("/admin/quests")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestDTO> getAllQuests(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {

        return adminService.getAllQuests(page, size).stream()
                .toList();
    }

    @DeleteMapping("/admin/quests/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeQuestById(@PathVariable("id") Long id) {
        adminService.removeQuestById(id);
    }

}
