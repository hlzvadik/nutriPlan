package goltsov.nutriPlan.controllers;

import goltsov.nutriPlan.dto.DishDto;
import goltsov.nutriPlan.dto.FilterSortRequestDishDto;
import goltsov.nutriPlan.services.DishService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/dish")
public class PublicDishController {
    private final DishService dishService;

    public PublicDishController(DishService dishService) {
        this.dishService = dishService;
    }

    @RequestMapping(value = "/filters", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Page<DishDto>> getDishesWithFiltersAndSort(@RequestBody FilterSortRequestDishDto filterSortRequestDishDto) {
        return ResponseEntity.ok(dishService.getFilteredDishes(filterSortRequestDishDto).map(dishService::dishToDishDto));
    }
}
