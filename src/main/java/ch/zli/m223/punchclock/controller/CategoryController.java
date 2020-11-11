package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.domain.Category;
import ch.zli.m223.punchclock.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
        Category miscCategory = new Category();
        miscCategory.setTitle("Misc");
        categoryService.registerCategory(miscCategory);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category registerCategory(@Valid @RequestBody Category category) {
        return categoryService.registerCategory(category);
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@Valid @PathVariable long categoryId) {
        if (!categoryService.deleteCategory(categoryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public Category updateCategory(@Valid @PathVariable long categoryId, @Valid @RequestBody Category category) {
        if (categoryService.updateCategory(categoryId, category) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return category;
    }
}
