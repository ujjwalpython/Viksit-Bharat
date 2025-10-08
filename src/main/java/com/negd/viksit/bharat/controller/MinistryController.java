package com.negd.viksit.bharat.controller;

import com.negd.viksit.bharat.dto.GovernmentEntityDto;
import com.negd.viksit.bharat.model.master.GovernmentEntity;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.service.MinistryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ministries")
public class MinistryController {
    private final MinistryService ministryService;

    public MinistryController(MinistryService ministryService) {
        this.ministryService = ministryService;
    }

    @GetMapping
    public List<GovernmentEntityDto> getAllMinistries() {
        return ministryService.getAllMinistries();
    }
}

