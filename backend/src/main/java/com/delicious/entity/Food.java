package com.delicious.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("food")
public class Food implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long categoryId;
    private String cover;
    private String images;
    private String introduction;
    private String origin;
    private String feature;
    private Long userId;
    private Integer viewCount;
    private Integer likeCount;
    private Integer status;
    
    @TableField(exist = false)
    private String categoryName;
    
    @TableField(exist = false)
    private String userName;
    
    @TableField(exist = false)
    private Boolean isLiked;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
