package goltsov.nutriPlan.controllers;

import goltsov.nutriPlan.dto.DishDto;
import goltsov.nutriPlan.dto.FilterSortRationDto;
import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.EatingName;
import goltsov.nutriPlan.services.RationService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/public/ration")
public class PublicRationService {

    private final RationService rationService;

    public PublicRationService(RationService rationService) {
        this.rationService = rationService;
    }

    @GetMapping("/filter")
    public Page<Map<EatingName, DishDto>> getDishesWithFiltersAndSort(@RequestBody FilterSortRationDto filterSortRationDto) {
        
    }
}
