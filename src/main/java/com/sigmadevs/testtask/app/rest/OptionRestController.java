package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.OptionDTO;
import com.sigmadevs.testtask.app.dto.UpdateOptionDTO;
import com.sigmadevs.testtask.app.service.OptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OptionRestController {

    private final OptionService optionService;
    @PutMapping("/options")
    @ResponseStatus(HttpStatus.OK)
    public OptionDTO updateOption(@RequestBody @Valid UpdateOptionDTO updateOptionDTO, Principal principal) {
        return optionService.updateOption(updateOptionDTO, principal);
    }
    @GetMapping("/options/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public List<OptionDTO> getOptionsByTaskId(@PathVariable("taskId") Long taskId) {
        return optionService.getOptionsByTaskId(taskId);
    }

    @DeleteMapping("/options/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeOptionById(@PathVariable("id") Long id, Principal principal) {
        optionService.removeOptionById(id, principal);
    }

}
