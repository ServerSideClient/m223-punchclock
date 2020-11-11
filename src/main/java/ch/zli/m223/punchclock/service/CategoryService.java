package ch.zli.m223.punchclock.service;

import ch.zli.m223.punchclock.domain.Category;
import ch.zli.m223.punchclock.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category registerCategory(Category category) {
        return categoryRepository.saveAndFlush(category);
    }

    public Boolean deleteCategory(long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            return true;
        }
        return false;
    }

    public Category updateCategory(long categoryId, Category category) {
        if (category.getId() == categoryId) {
            if (categoryRepository.existsById(categoryId)) {
                categoryRepository.deleteById(categoryId);
                return categoryRepository.saveAndFlush(category);
            }
        }
        return null;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

}
