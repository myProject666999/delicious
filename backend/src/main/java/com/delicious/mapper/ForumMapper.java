package com.delicious.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.delicious.entity.Forum;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ForumMapper extends BaseMapper<Forum> {
}
