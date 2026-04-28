package com.delicious.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.delicious.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecipeMapper extends BaseMapper<Recipe> {
}
