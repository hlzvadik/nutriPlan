package goltsov.nutriPlan.services;

import goltsov.nutriPlan.dto.FilterSortRequestIngredientDto;
import goltsov.nutriPlan.entities.Ingredient;
import goltsov.nutriPlan.repositories.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.getReferenceById(id);
    }

    public Ingredient getIngredientByName(String name) {
        return ingredientRepository.findByName(name);
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Ingredient updateIngredient(Long id, Ingredient ingredient) {
        Ingredient oldIngredient = ingredientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found by id=" + id));
        ingredient.setId(id);
        return ingredientRepository.save(ingredient);
    }

    public void deleteIngredientById(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found by id=" + id);
        }
        ingredientRepository.deleteById(id);
    }

    public Page<Ingredient> getFilteredIngredients(FilterSortRequestIngredientDto dto) {
        List<Sort.Order> orders = dto.sort() == null ? List.of() : dto.sort().stream()
                .map(s -> new Sort.Order(
                        "DESC".equals(s.direction()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                        s.field()
                )).toList();

        Sort sort = orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
        PageRequest pageable = PageRequest.of(dto.page(), dto.size(), sort);
        return ingredientRepository.getWithFilterAndSort(dto, pageable);
    }
}
