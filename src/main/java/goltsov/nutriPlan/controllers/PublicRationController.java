package goltsov.nutriPlan.controllers;

import goltsov.nutriPlan.dto.DishDto;
import goltsov.nutriPlan.dto.FilterSortRationDto;
import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.EatingName;
import goltsov.nutriPlan.services.DishService;
import goltsov.nutriPlan.services.RationService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/ration")
public class PublicRationController {

    private final RationService rationService;
    private final DishService dishService;

    public PublicRationController(RationService rationService, DishService dishService) {
        this.rationService = rationService;
        this.dishService = dishService;
    }

    @RequestMapping(value = "/filter", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<EatingName, Page<DishDto>> getDishesWithFiltersAndSort(@RequestBody FilterSortRationDto filterSortRationDto) {
        Map<EatingName, Page<Dish>> ration = rationService.getRationByFilters(filterSortRationDto);
        return ration.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().map(dishService::dishToDishDto)));
    }
}
