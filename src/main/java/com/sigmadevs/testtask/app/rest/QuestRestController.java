package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.service.QuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuestRestController {

    private final QuestService questService;
    @PostMapping("/quests")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestDTO createQuest(@RequestBody @Valid QuestDTO questDTO) {
        return questService.createQuest(questDTO);
    }
    @PutMapping("/quests")
    @ResponseStatus(HttpStatus.OK)
    public QuestDTO updateQuest(@RequestBody @Valid QuestDTO questDTO) {
        return questService.updateQuest(questDTO);
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
