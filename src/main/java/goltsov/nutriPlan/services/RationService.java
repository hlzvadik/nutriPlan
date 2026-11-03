package goltsov.nutriPlan.services;

import goltsov.nutriPlan.dto.FilterSortRationDto;
import goltsov.nutriPlan.dto.FilterSortRequestDishDto;
import goltsov.nutriPlan.dto.Range;
import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.EatingName;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RationService {

    private static final List<EatingName> RATION_EATINGS = List.of(EatingName.BREAKFAST, EatingName.LUNCH, EatingName.DINNER);

    private final DishService dishService;

    public RationService(DishService dishService) {
        this.dishService = dishService;
    }

    public Map<EatingName, Page<Dish>> getRationByFilters(FilterSortRationDto dto) {
        Map<EatingName, Page<Dish>> result = new LinkedHashMap<>();
        for (int i = 0; i < RATION_EATINGS.size(); i++) {
            EatingName eating = RATION_EATINGS.get(i);
            double share = dto.percents().get(i) / 100.0;
            result.put(eating, dishService.getFilteredDishes(buildFilter(dto, eating, share)));
        }
        return result;
    }

    private FilterSortRequestDishDto buildFilter(FilterSortRationDto dto, EatingName eating, double share) {
        return new FilterSortRequestDishDto(
                scale(dto.kcal(), share),
                scale(dto.protein(), share),
                scale(dto.carbohydrates(), share),
                scale(dto.fats(), share),
                new Range<>((int) (dto.rawWeight().min() * share), (int) (dto.rawWeight().max() * share)),

                null,
                null,

                null,
                dto.badIngredients(),

                List.of(eating),

                dto.page(),
                dto.size(),

                List.of(new FilterSortRequestDishDto.SortCriterial("countLikes", "DESC"))
        );
    }

    private Range<Double> scale(Range<Double> range, double share) {
        return new Range<>(range.min() * share, range.max() * share);
    }
}
