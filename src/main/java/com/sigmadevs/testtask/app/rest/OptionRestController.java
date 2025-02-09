package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.OptionDTO;
import com.sigmadevs.testtask.app.service.OptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OptionRestController {

    private final OptionService optionService;
    @PostMapping("/options")
    @ResponseStatus(HttpStatus.CREATED)
    public OptionDTO createOption(@RequestBody @Valid OptionDTO optionDTO) {
        return optionService.createOption(optionDTO);
    }
    @PutMapping("/options")
    @ResponseStatus(HttpStatus.OK)
    public OptionDTO updateOption(@RequestBody @Valid OptionDTO optionDTO) {
        return optionService.updateOption(optionDTO);
    }
    @GetMapping("/options")
    @ResponseStatus(HttpStatus.OK)
    public List<OptionDTO> getAllOptions() {
        return optionService.getAllOptions();
    }
    @GetMapping("/options/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OptionDTO getOptionById(@PathVariable("id") Long id) {
        return optionService.getOptionById(id);
    }
    @DeleteMapping("/options/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeOptionById(@PathVariable("id") Long id) {
        optionService.removeOptionById(id);
    }

}
