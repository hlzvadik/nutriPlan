package goltsov.nutriPlan.services;

import goltsov.nutriPlan.dto.FilterSortRationDto;
import goltsov.nutriPlan.dto.FilterSortRequestDishDto;
import goltsov.nutriPlan.dto.Range;
import goltsov.nutriPlan.entities.Dish;
import goltsov.nutriPlan.entities.EatingName;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RationService {

    private final DishService dishService;

    public RationService(DishService dishService) {
        this.dishService = dishService;
    }

    public Map<EatingName, Page<Dish>> getRationByFilters(FilterSortRationDto filterSortRationDto) {
        Map<EatingName, Page<Dish>> result = new HashMap<>();
        result.put(EatingName.BREAKFAST, dishService.getFilteredDishes(new FilterSortRequestDishDto(
                new Range<>(filterSortRationDto.kcal().min() * filterSortRationDto.percents().get(0), filterSortRationDto.kcal().max() * filterSortRationDto.percents().get(0)),
                new Range<>(filterSortRationDto.protein().min() * filterSortRationDto.percents().get(0), filterSortRationDto.protein().max() * filterSortRationDto.percents().get(0)),
                new Range<>(filterSortRationDto.carbohydrates().min() * filterSortRationDto.percents().get(0), filterSortRationDto.carbohydrates().max() * filterSortRationDto.percents().get(0)),
                new Range<>(filterSortRationDto.fats().min() * filterSortRationDto.percents().get(0), filterSortRationDto.fats().max() * filterSortRationDto.percents().get(0)),
                new Range<>(filterSortRationDto.rawWeight().min() * filterSortRationDto.percents().get(0), filterSortRationDto.rawWeight().max() * filterSortRationDto.percents().get(0)),

                null,
                null,

                null,
                filterSortRationDto.badIngredients(),

                List.of(EatingName.BREAKFAST.toString()),

                filterSortRationDto.page(),
                filterSortRationDto.size(),

                List.of(new FilterSortRequestDishDto.SortCriterial("countLikes", "DESC"))
        )));
        result.put(EatingName.LUNCH, dishService.getFilteredDishes(new FilterSortRequestDishDto(
                new Range<>(filterSortRationDto.kcal().min() * filterSortRationDto.percents().get(1), filterSortRationDto.kcal().max() * filterSortRationDto.percents().get(1)),
                new Range<>(filterSortRationDto.protein().min() * filterSortRationDto.percents().get(1), filterSortRationDto.protein().max() * filterSortRationDto.percents().get(1)),
                new Range<>(filterSortRationDto.carbohydrates().min() * filterSortRationDto.percents().get(1), filterSortRationDto.carbohydrates().max() * filterSortRationDto.percents().get(1)),
                new Range<>(filterSortRationDto.fats().min() * filterSortRationDto.percents().get(1), filterSortRationDto.fats().max() * filterSortRationDto.percents().get(1)),
                new Range<>(filterSortRationDto.rawWeight().min() * filterSortRationDto.percents().get(1), filterSortRationDto.rawWeight().max() * filterSortRationDto.percents().get(1)),

                null,
                null,

                null,
                filterSortRationDto.badIngredients(),

                List.of(EatingName.LUNCH.toString()),

                filterSortRationDto.page(),
                filterSortRationDto.size(),

                List.of(new FilterSortRequestDishDto.SortCriterial("countLikes", "DESC"))
        )));
        result.put(EatingName.DINNER, dishService.getFilteredDishes(new FilterSortRequestDishDto(
                new Range<>(filterSortRationDto.kcal().min() * filterSortRationDto.percents().get(2), filterSortRationDto.kcal().max() * filterSortRationDto.percents().get(2)),
                new Range<>(filterSortRationDto.protein().min() * filterSortRationDto.percents().get(2), filterSortRationDto.protein().max() * filterSortRationDto.percents().get(2)),
                new Range<>(filterSortRationDto.carbohydrates().min() * filterSortRationDto.percents().get(2), filterSortRationDto.carbohydrates().max() * filterSortRationDto.percents().get(2)),
                new Range<>(filterSortRationDto.fats().min() * filterSortRationDto.percents().get(2), filterSortRationDto.fats().max() * filterSortRationDto.percents().get(2)),
                new Range<>(filterSortRationDto.rawWeight().min() * filterSortRationDto.percents().get(2), filterSortRationDto.rawWeight().max() * filterSortRationDto.percents().get(2)),

                null,
                null,

                null,
                filterSortRationDto.badIngredients(),

                List.of(EatingName.DINNER.toString()),

                filterSortRationDto.page(),
                filterSortRationDto.size(),

                List.of(new FilterSortRequestDishDto.SortCriterial("countLikes", "DESC"))
        )));
        return result;
    }
}
