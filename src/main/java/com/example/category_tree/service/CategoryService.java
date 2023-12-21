package com.example.category_tree.service;

import com.example.category_tree.entity.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(String name, String parentName);
    void removeCategory(String name);
    String viewTree();
}
