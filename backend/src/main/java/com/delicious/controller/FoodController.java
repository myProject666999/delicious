package com.delicious.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.delicious.common.Result;
import com.delicious.entity.Food;
import com.delicious.entity.FoodCategory;
import com.delicious.service.FoodCategoryService;
import com.delicious.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FoodService foodService;
    
    @Autowired
    private FoodCategoryService foodCategoryService;

    @GetMapping("/category/list")
    public Result<List<FoodCategory>> categoryList() {
        List<FoodCategory> list = foodCategoryService.listAll();
        return Result.success(list);
    }

    @GetMapping("/category/page")
    public Result<Page<FoodCategory>> categoryPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        Page<FoodCategory> page = foodCategoryService.pageList(pageNum, pageSize, name);
        return Result.success(page);
    }

    @PostMapping("/category/save")
    public Result<Void> categorySave(@RequestBody FoodCategory category) {
        foodCategoryService.saveOrUpdate(category);
        return Result.success();
    }

    @DeleteMapping("/category/delete/{id}")
    public Result<Void> categoryDelete(@PathVariable Long id) {
        foodCategoryService.removeById(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<Page<Food>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        Page<Food> page = foodService.pageList(pageNum, pageSize, name, categoryId, status);
        return Result.success(page);
    }

    @GetMapping("/front/list")
    public Result<Page<Food>> frontList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long userId) {
        Page<Food> page = foodService.frontPageList(pageNum, pageSize, name, categoryId, userId);
        return Result.success(page);
    }

    @GetMapping("/detail/{id}")
    public Result<Food> detail(@PathVariable Long id,
                                 @RequestParam(required = false) Long userId) {
        foodService.viewCountIncrement(id);
        Food food = foodService.getDetail(id, userId);
        return Result.success(food);
    }

    @GetMapping("/recommend")
    public Result<List<Food>> recommend(@RequestParam(defaultValue = "6") Integer limit) {
        List<Food> list = foodService.getRecommend(limit);
        return Result.success(list);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Food food) {
        foodService.saveOrUpdate(food);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        foodService.removeById(id);
        return Result.success();
    }

    @PostMapping("/like")
    public Result<Boolean> toggleLike(@RequestParam Long userId, @RequestParam Long foodId) {
        boolean result = foodService.toggleLike(userId, foodId);
        return Result.success(result);
    }
}
