package com.delicious.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.delicious.entity.Food;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {
}
