package goltsov.nutriPlan.datainitializer;

import goltsov.nutriPlan.entities.*;
import goltsov.nutriPlan.repositories.*;
import goltsov.nutriPlan.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Profile("demo")
@Order(2)
public class DemoDataSeeder implements CommandLineRunner {

    private static final String[][] INGREDIENTS = {
            {"Овсяные хлопья", "352", "12.3", "61.8", "6.2"},
            {"Молоко 2,5%", "52", "2.8", "4.7", "2.5"},
            {"Яйцо куриное", "157", "12.7", "0.7", "11.5"},
            {"Куриная грудка", "113", "23.6", "0.4", "1.9"},
            {"Рис белый (сухой)", "344", "6.7", "78.9", "0.7"},
            {"Гречка (сухая)", "313", "12.6", "62.1", "3.3"},
            {"Макароны (сухие)", "337", "10.4", "69.7", "1.1"},
            {"Картофель", "77", "2.0", "16.3", "0.4"},
            {"Помидор", "20", "1.1", "3.7", "0.2"},
            {"Огурец", "15", "0.8", "2.8", "0.1"},
            {"Сыр твёрдый", "364", "23.2", "0.0", "29.5"},
            {"Творог 5%", "121", "17.2", "1.8", "5.0"},
            {"Йогурт натуральный", "60", "4.3", "3.5", "2.5"},
            {"Банан", "96", "1.5", "21.0", "0.2"},
            {"Яблоко", "47", "0.4", "9.8", "0.4"},
            {"Оливковое масло", "884", "0.0", "0.0", "99.9"},
            {"Говядина постная", "187", "18.9", "0.0", "12.4"},
            {"Лосось", "208", "20.4", "0.0", "13.4"},
            {"Брокколи", "34", "2.8", "6.6", "0.4"},
            {"Морковь", "32", "1.3", "6.9", "0.1"},
            {"Лук репчатый", "41", "1.4", "8.2", "0.2"},
            {"Хлеб цельнозерновой", "250", "9.0", "45.0", "3.5"},
            {"Авокадо", "160", "2.0", "8.5", "14.7"},
            {"Миндаль", "579", "21.2", "21.6", "49.9"},
            {"Мёд", "304", "0.3", "82.4", "0.0"},
            {"Чечевица (сухая)", "295", "24.0", "46.3", "1.5"},
            {"Тунец консервированный", "116", "25.5", "0.0", "1.0"},
            {"Тофу", "76", "8.1", "1.9", "4.8"},
            {"Шпинат", "23", "2.9", "3.6", "0.4"},
            {"Перец болгарский", "27", "1.3", "5.3", "0.1"},
            {"Сметана 15%", "162", "2.6", "3.0", "15.0"},
            {"Масло сливочное", "748", "0.5", "0.8", "82.5"}
    };

    private final IngredientRepository ingredientRepository;
    private final DishRepository dishRepository;
    private final RecipeRepository recipeRepository;
    private final EatingRepository eatingRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final UserService userService;

    public DemoDataSeeder(IngredientRepository ingredientRepository, DishRepository dishRepository,
                          RecipeRepository recipeRepository, EatingRepository eatingRepository,
                          RoleRepository roleRepository, UserRepository userRepository,
                          MealRepository mealRepository, UserService userService) {
        this.ingredientRepository = ingredientRepository;
        this.dishRepository = dishRepository;
        this.recipeRepository = recipeRepository;
        this.eatingRepository = eatingRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
        this.userService = userService;
    }

    private record DishSeed(String name, int likes, String recipe, List<EatingName> eatings, Object[] ingredients) {}

    private static DishSeed dish(String name, int likes, String recipe, List<EatingName> eatings, Object... ingredients) {
        return new DishSeed(name, likes, recipe, eatings, ingredients);
    }

    private static List<DishSeed> dishSeeds() {
        return List.of(
                dish("Овсянка с бананом и миндалём", 120, "Залейте хлопья горячим молоком и варите 5–7 минут, помешивая. Добавьте нарезанный банан, миндаль и мёд.",
                        List.of(EatingName.BREAKFAST), "Овсяные хлопья", 50, "Молоко 2,5%", 200, "Банан", 80, "Миндаль", 30, "Мёд", 8),
                dish("Тост с авокадо и яйцом", 88, "Подсушите хлеб, разомните авокадо вилкой и намажьте на тост. Сверху выложите яйцо, сваренное «в мешочек».",
                        List.of(EatingName.BREAKFAST), "Хлеб цельнозерновой", 100, "Авокадо", 70, "Яйцо куриное", 60),
                dish("Йогурт с овсянкой и орехами", 70, "Выложите йогурт в миску, добавьте хлопья, нарезанный банан и орехи. Дайте постоять 10 минут.",
                        List.of(EatingName.BREAKFAST), "Йогурт натуральный", 200, "Овсяные хлопья", 45, "Банан", 80, "Миндаль", 25, "Мёд", 8),
                dish("Гречка с молоком и маслом", 60, "Отварите гречку до готовности, добавьте горячее молоко и масло, прогрейте 2–3 минуты.",
                        List.of(EatingName.BREAKFAST), "Гречка (сухая)", 70, "Молоко 2,5%", 250, "Масло сливочное", 10, "Миндаль", 10),
                dish("Омлет с помидорами и сыром", 95, "Взбейте яйца с молоком, посолите. Обжарьте помидоры на масле, залейте яйцами, посыпьте тёртым сыром и готовьте под крышкой 5 минут.",
                        List.of(EatingName.BREAKFAST, EatingName.DINNER), "Яйцо куриное", 150, "Помидор", 100, "Сыр твёрдый", 30, "Масло сливочное", 5, "Молоко 2,5%", 50),
                dish("Творог с яблоком и мёдом", 80, "Нарежьте яблоко кубиками, смешайте с творогом, полейте мёдом и посыпьте дроблёным миндалём.",
                        List.of(EatingName.BREAKFAST, EatingName.OTHER), "Творог 5%", 200, "Яблоко", 120, "Мёд", 10, "Миндаль", 15),
                dish("Рис с тофу и овощами", 55, "Отварите рис. Тофу нарежьте кубиками и обжарьте до корочки, добавьте овощи, тушите 7 минут. Смешайте с рисом.",
                        List.of(EatingName.LUNCH, EatingName.DINNER), "Рис белый (сухой)", 80, "Тофу", 150, "Брокколи", 100, "Перец болгарский", 80, "Оливковое масло", 14),
                dish("Паста с овощами и сыром", 90, "Отварите макароны. Обжарьте перец и помидоры 8 минут, смешайте с пастой и посыпьте тёртым сыром.",
                        List.of(EatingName.LUNCH), "Макароны (сухие)", 90, "Помидор", 150, "Перец болгарский", 80, "Сыр твёрдый", 25, "Оливковое масло", 14),
                dish("Чечевичный суп с картофелем", 65, "Обжарьте лук и морковь, добавьте чечевицу, картофель и 800 мл воды. Варите 25 минут.",
                        List.of(EatingName.LUNCH), "Чечевица (сухая)", 55, "Картофель", 150, "Морковь", 60, "Лук репчатый", 50, "Оливковое масло", 18),
                dish("Рис с курицей и овощами", 110, "Отварите рис. Курицу нарежьте и обжарьте на масле 8 минут, добавьте брокколи и тушите 5 минут. Смешайте с рисом.",
                        List.of(EatingName.LUNCH, EatingName.DINNER), "Куриная грудка", 50, "Рис белый (сухой)", 65, "Брокколи", 85, "Оливковое масло", 16),
                dish("Гречка с говядиной и овощами", 100, "Обжарьте говядину кусочками, добавьте лук, морковь и перец, тушите 20 минут. Подавайте с отварной гречкой.",
                        List.of(EatingName.LUNCH), "Гречка (сухая)", 95, "Говядина постная", 50, "Морковь", 65, "Лук репчатый", 50, "Перец болгарский", 105, "Оливковое масло", 14),
                dish("Курица с рисом и брокколи", 150, "Отварите рис. Курицу обжарьте на масле 8 минут, добавьте брокколи и тушите 5 минут. Подавайте с рисом.",
                        List.of(EatingName.LUNCH, EatingName.DINNER), "Куриная грудка", 150, "Рис белый (сухой)", 80, "Брокколи", 100, "Оливковое масло", 8),
                dish("Лосось с картофелем и шпинатом", 130, "Запеките лосось и картофель при 200 °C 20 минут. Шпинат слегка обжарьте на масле и подайте рядом.",
                        List.of(EatingName.DINNER), "Лосось", 50, "Картофель", 375, "Шпинат", 55, "Оливковое масло", 14),
                dish("Картофель с яйцом и сыром", 75, "Отварите картофель, заправьте маслом, сверху выложите яйцо и тёртый сыр. Подавайте со свежим огурцом.",
                        List.of(EatingName.DINNER), "Картофель", 375, "Яйцо куриное", 40, "Сыр твёрдый", 20, "Масло сливочное", 12, "Огурец", 135),
                dish("Тушёная говядина с картофелем", 100, "Обжарьте говядину с луком и морковью, добавьте картофель и воду, тушите 40 минут. В конце положите сметану.",
                        List.of(EatingName.DINNER, EatingName.LUNCH), "Говядина постная", 55, "Картофель", 290, "Лук репчатый", 60, "Морковь", 60, "Сметана 15%", 20, "Оливковое масло", 8),
                dish("Куриная грудка с овощами", 140, "Куриную грудку запеките 25 минут при 190 °C. Овощи приготовьте на пару 8 минут и подавайте вместе.",
                        List.of(EatingName.DINNER, EatingName.LUNCH), "Куриная грудка", 180, "Брокколи", 150, "Морковь", 80, "Оливковое масло", 10),
                dish("Омлет со шпинатом", 72, "Обжарьте шпинат на масле 2 минуты, залейте взбитыми яйцами, посыпьте сыром и готовьте под крышкой.",
                        List.of(EatingName.DINNER, EatingName.BREAKFAST), "Яйцо куриное", 200, "Шпинат", 100, "Сыр твёрдый", 30, "Оливковое масло", 5),
                dish("Салат с тунцом и яйцом", 85, "Нарежьте овощи и сваренные вкрутую яйца, добавьте тунец, заправьте маслом. Подавайте с хлебом.",
                        List.of(EatingName.DINNER, EatingName.LUNCH), "Тунец консервированный", 120, "Яйцо куриное", 100, "Огурец", 150, "Помидор", 150, "Оливковое масло", 10, "Хлеб цельнозерновой", 40),
                dish("Яблоко с миндалём", 40, "Нарежьте яблоко дольками и подавайте с горстью миндаля.",
                        List.of(EatingName.OTHER), "Яблоко", 150, "Миндаль", 25),
                dish("Бутерброд с сыром и помидором", 58, "Положите на хлеб сыр и ломтики помидора. По желанию подогрейте 2 минуты в духовке.",
                        List.of(EatingName.OTHER, EatingName.BREAKFAST), "Хлеб цельнозерновой", 60, "Сыр твёрдый", 30, "Помидор", 40)
        );
    }

    @Override
    public void run(String... args) {
        seedIngredients();
        seedDishes();
        seedUsers();
    }

    private void seedIngredients() {
        for (String[] i : INGREDIENTS) {
            if (ingredientRepository.findByName(i[0]) == null) {
                ingredientRepository.save(new Ingredient(i[0], Double.valueOf(i[1]), Double.valueOf(i[2]),
                        Double.valueOf(i[3]), Double.valueOf(i[4])));
            }
        }
    }

    private void seedDishes() {
        for (DishSeed seed : dishSeeds()) {
            if (dishRepository.findAll().stream().anyMatch(d -> d.getName().equals(seed.name()))) continue;
            Set<Eating> eatings = new HashSet<>();
            seed.eatings().forEach(e -> eatings.add(eatingRepository.findByEatingName(e)));
            // нулевые итоги: addIngredient сам считает калории, БЖУ и вес по ингредиентам
            Dish dish = new Dish(seed.name(), 0.0, 0.0, 0.0, 0.0, 0, seed.likes(), eatings);
            for (int i = 0; i < seed.ingredients().length; i += 2) {
                dish.addIngredient(ingredientRepository.findByName((String) seed.ingredients()[i]), (Integer) seed.ingredients()[i + 1]);
            }
            dish = dishRepository.save(dish);
            recipeRepository.save(new Recipe(dish, seed.recipe()));
        }
    }

    private void seedUsers() {
        createUserIfAbsent("Admin", "admin@nutriplan.local", "admin123", 30,
                Set.of(roleRepository.getRoleByRoleName(RoleName.ROLE_USER), roleRepository.getRoleByRoleName(RoleName.ROLE_ADMIN)));
        User demo = createUserIfAbsent("Demo", "demo@nutriplan.local", "demo123", 25,
                Set.of(roleRepository.getRoleByRoleName(RoleName.ROLE_USER)));
        if (demo != null) {
            addDemoMeal(demo, "Овсянка с бананом и миндалём", EatingName.BREAKFAST, 1.0, 8);
            addDemoMeal(demo, "Рис с курицей и овощами", EatingName.LUNCH, 1.0, 13);
        }
    }

    private User createUserIfAbsent(String name, String email, String password, int age, Set<Role> roles) {
        if (userRepository.getByEmail(email) != null) return null;
        return userService.createUser(new User(name, email, password, age, LocalDateTime.now(), new HashSet<>(roles)));
    }

    private void addDemoMeal(User user, String dishName, EatingName eating, double share, int hour) {
        Dish dish = dishRepository.findAll().stream().filter(d -> d.getName().equals(dishName)).findFirst().orElse(null);
        if (dish == null) return;
        mealRepository.save(new Meal(user, dish, eatingRepository.findByEatingName(eating), share,
                LocalDate.now().atTime(hour, 0)));
    }
}
