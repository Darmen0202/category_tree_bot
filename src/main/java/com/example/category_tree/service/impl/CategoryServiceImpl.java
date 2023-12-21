package com.example.category_tree.service.impl;

import com.example.category_tree.entity.Category;
import com.example.category_tree.repository.CategoryRepository;
import com.example.category_tree.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Category addCategory(String name, String parentName) {
        // Проверка на уникальность имени категории
        if (categoryRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Категория с именем '" + name + "' уже существует.");
        }

        Category parent = null;
        // Если указано имя родительской категории, пытаемся найти ее
        if (parentName != null && !parentName.isEmpty()) {
            Optional<Category> parentOpt = categoryRepository.findByName(parentName);
            if (parentOpt.isPresent()) {
                parent = parentOpt.get();
            } else {
                throw new IllegalArgumentException("Родительская категория не найдена");
            }
        }

        Category newCategory = new Category();
        newCategory.setName(name);
        newCategory.setParent(parent);
        return categoryRepository.save(newCategory);
    }

    @Override
    @Transactional
    public void removeCategory(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));

        // Удаление дочерних категорий перед удалением родителя
        category.getChildren().forEach(child -> removeCategory(child.getName()));
        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public String viewTree() {
        List<Category> categories = categoryRepository.findAll();
        return buildTreeRepresentation(categories, null, 0);
    }

    private String buildTreeRepresentation(List<Category> categories, Category parent, int level) {
        StringBuilder builder = new StringBuilder();
        String indent = " ".repeat(level * 2); // Отступ для текущего уровня
        String branch = (level > 0 ? "|-- " : ""); // Ветка перед именем категории

        categories.stream()
                .filter(cat -> (parent == null && cat.getParent() == null) ||
                        (parent != null && cat.getParent() != null && cat.getParent().getId().equals(parent.getId())))
                .forEach(cat -> {
                    // Добавление имени категории с отступом и веткой
                    builder.append(indent)
                            .append(branch)
                            .append(cat.getName())
                            .append("\n");
                    // Рекурсивный вызов для добавления дочерних элементов
                    builder.append(buildTreeRepresentation(categories, cat, level + 1));
                });

        return builder.toString();
    }

}
