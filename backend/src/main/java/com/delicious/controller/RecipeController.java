package com.delicious.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.delicious.common.Result;
import com.delicious.entity.Recipe;
import com.delicious.entity.RecipeCategory;
import com.delicious.service.RecipeCategoryService;
import com.delicious.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private RecipeCategoryService recipeCategoryService;

    @GetMapping("/category/list")
    public Result<List<RecipeCategory>> categoryList() {
        List<RecipeCategory> list = recipeCategoryService.listAll();
        return Result.success(list);
    }

    @GetMapping("/category/page")
    public Result<Page<RecipeCategory>> categoryPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        Page<RecipeCategory> page = recipeCategoryService.pageList(pageNum, pageSize, name);
        return Result.success(page);
    }

    @PostMapping("/category/save")
    public Result<Void> categorySave(@RequestBody RecipeCategory category) {
        recipeCategoryService.saveOrUpdate(category);
        return Result.success();
    }

    @DeleteMapping("/category/delete/{id}")
    public Result<Void> categoryDelete(@PathVariable Long id) {
        recipeCategoryService.removeById(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<Page<Recipe>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        Page<Recipe> page = recipeService.pageList(pageNum, pageSize, title, categoryId, status);
        return Result.success(page);
    }

    @GetMapping("/front/list")
    public Result<Page<Recipe>> frontList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long userId) {
        Page<Recipe> page = recipeService.frontPageList(pageNum, pageSize, title, categoryId, userId);
        return Result.success(page);
    }

    @GetMapping("/detail/{id}")
    public Result<Recipe> detail(@PathVariable Long id, 
                                   @RequestParam(required = false) Long userId) {
        recipeService.viewCountIncrement(id);
        Recipe recipe = recipeService.getDetail(id, userId);
        return Result.success(recipe);
    }

    @GetMapping("/recommend")
    public Result<List<Recipe>> recommend(@RequestParam(defaultValue = "6") Integer limit) {
        List<Recipe> list = recipeService.getRecommend(limit);
        return Result.success(list);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Recipe recipe) {
        recipeService.saveOrUpdate(recipe);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        recipeService.removeById(id);
        return Result.success();
    }

    @PostMapping("/like")
    public Result<Boolean> toggleLike(@RequestParam Long userId, @RequestParam Long recipeId) {
        boolean result = recipeService.toggleLike(userId, recipeId);
        return Result.success(result);
    }
}
