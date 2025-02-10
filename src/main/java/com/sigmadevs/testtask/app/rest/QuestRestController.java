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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuestRestController {

    private final QuestService questService;

    @PostMapping(value = "/quests", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public QuestDTO createQuest(@RequestPart @Valid CreateQuestDTO quest, @RequestPart(required = false) MultipartFile image) {
        return questService.createQuest(quest,image);
    }

    //TODO
    @PutMapping(value = "/quests", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public QuestDTO updateQuest(@RequestPart @Valid UpdateQuestDTO quest, @RequestPart(required = false) MultipartFile image) {
        return questService.updateQuest(quest,image);
    }

    @GetMapping("/quests")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestDTO> getAllQuests() {
        return questService.getAllQuests();
    }

    @GetMapping("/quests/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuestDTO getQuestById(@PathVariable("id") Long id) {
        return questService.getQuestById(id);
    }

    @DeleteMapping("/quests/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeQuestById(@PathVariable("id") Long id) {
        questService.removeQuestById(id);
    }

}
