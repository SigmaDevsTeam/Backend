package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.CreateQuestDTO;
import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.dto.UpdateQuestDTO;
import com.sigmadevs.testtask.app.service.QuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
//    @ResponseStatus(HttpStatus.CREATED)
    public QuestDTO createQuest(@RequestPart @Valid CreateQuestDTO quest, @RequestPart(required = false) MultipartFile image, Principal principal) {
        return questService.createQuest(quest,image,principal);
    }

    //TODO
    @PutMapping(value = "/quests", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<QuestDTO> updateQuest(@RequestPart @Valid UpdateQuestDTO quest, @RequestPart(required = false) MultipartFile image, Principal principal) {
        return questService.updateQuest(quest,image,principal);
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

    @GetMapping("/quests/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestDTO> getQuestByUserId(@PathVariable("id") Long id) {
        return questService.getQuestByUserId(id);
    }

    @DeleteMapping("/quests/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> removeQuestById(@PathVariable("id") Long id, Principal principal) {
        return questService.removeQuestById(id,principal);
    }

}
