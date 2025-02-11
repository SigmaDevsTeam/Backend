package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.CreateQuestDTO;
import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.dto.UpdateQuestDTO;
import com.sigmadevs.testtask.app.service.QuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuestRestController {

    private final QuestService questService;

    @PostMapping(value = "/quests", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public QuestDTO createQuest(@RequestPart @Valid CreateQuestDTO quest, @RequestPart(required = false) MultipartFile image, Principal principal) {
        return questService.createQuest(quest,image,principal);
    }

    @PutMapping(value = "/quests", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public QuestDTO updateQuest(@RequestPart @Valid UpdateQuestDTO quest, @RequestPart(required = false) MultipartFile image, Principal principal) {
        return questService.updateQuest(quest,image,principal);
    }
    @GetMapping("/quests")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestDTO> getAllQuests(Principal principal) {
        return questService.getAllQuests(principal);
    }
    @DeleteMapping("/quests/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeQuestById(@PathVariable("id") Long id, Principal principal) {
        questService.removeQuestById(id,principal);
    }

}