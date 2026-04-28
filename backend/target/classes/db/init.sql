-- 创建数据库
CREATE DATABASE IF NOT EXISTS delicious_food DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE delicious_food;

-- 管理员表
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `gender` tinyint(1) DEFAULT '0' COMMENT '性别 0未知 1男 2女',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1正常 0禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 菜谱分类表
DROP TABLE IF EXISTS `recipe_category`;
CREATE TABLE `recipe_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1启用 0禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜谱分类表';

-- 美食菜谱表
DROP TABLE IF EXISTS `recipe`;
CREATE TABLE `recipe` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) NOT NULL COMMENT '菜谱名称',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
  `introduction` varchar(500) DEFAULT NULL COMMENT '简介',
  `ingredients` text COMMENT '食材',
  `steps` text COMMENT '步骤说明',
  `tips` varchar(500) DEFAULT NULL COMMENT '小贴士',
  `user_id` bigint(20) DEFAULT NULL COMMENT '发布用户ID',
  `view_count` int(11) DEFAULT '0' COMMENT '浏览量',
  `like_count` int(11) DEFAULT '0' COMMENT '点赞数',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1正常 0下架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美食菜谱表';

-- 美食分类表
DROP TABLE IF EXISTS `food_category`;
CREATE TABLE `food_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1启用 0禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美食分类表';

-- 美食信息表
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '美食名称',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
  `images` text COMMENT '图片列表，逗号分隔',
  `introduction` text COMMENT '简介',
  `origin` varchar(100) DEFAULT NULL COMMENT '产地',
  `feature` text COMMENT '特色',
  `user_id` bigint(20) DEFAULT NULL COMMENT '发布用户ID',
  `view_count` int(11) DEFAULT '0' COMMENT '浏览量',
  `like_count` int(11) DEFAULT '0' COMMENT '点赞数',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1正常 0下架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美食信息表';

-- 美食论坛表
DROP TABLE IF EXISTS `forum`;
CREATE TABLE `forum` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(200) NOT NULL COMMENT '帖子标题',
  `content` text COMMENT '帖子内容',
  `images` text COMMENT '图片列表，逗号分隔',
  `user_id` bigint(20) NOT NULL COMMENT '发布用户ID',
  `view_count` int(11) DEFAULT '0' COMMENT '浏览量',
  `reply_count` int(11) DEFAULT '0' COMMENT '回复数',
  `is_top` tinyint(1) DEFAULT '0' COMMENT '是否置顶 1是 0否',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1正常 0隐藏',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美食论坛表';

-- 论坛回复表
DROP TABLE IF EXISTS `forum_reply`;
CREATE TABLE `forum_reply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `forum_id` bigint(20) NOT NULL COMMENT '帖子ID',
  `user_id` bigint(20) NOT NULL COMMENT '回复用户ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父回复ID，0为直接回复',
  `reply_user_id` bigint(20) DEFAULT NULL COMMENT '被回复用户ID',
  `content` text COMMENT '回复内容',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1正常 0隐藏',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_forum_id` (`forum_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛回复表';

-- 系统公告表
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
  `author` varchar(50) DEFAULT NULL COMMENT '作者',
  `view_count` int(11) DEFAULT '0' COMMENT '浏览量',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1发布 0草稿',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

-- 轮播图表
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `image` varchar(255) NOT NULL COMMENT '图片路径',
  `link_url` varchar(255) DEFAULT NULL COMMENT '跳转链接',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1启用 0禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- 点赞记录表
DROP TABLE IF EXISTS `user_like`;
CREATE TABLE `user_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `target_id` bigint(20) NOT NULL COMMENT '目标ID',
  `target_type` varchar(20) NOT NULL COMMENT '目标类型：recipe菜谱 food美食 forum帖子',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`, `target_id`, `target_type`),
  KEY `idx_target` (`target_id`, `target_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞记录表';

-- 初始化数据
-- 管理员账号：admin / 123456
INSERT INTO `admin` (`username`, `password`, `nickname`, `phone`, `email`) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', '13800138000', 'admin@delicious.com');

-- 测试用户：user / 123456
INSERT INTO `user` (`username`, `password`, `nickname`, `phone`, `email`, `gender`, `status`) VALUES
('user', 'e10adc3949ba59abbe56e057f20f883e', '美食爱好者', '13800138001', 'user@delicious.com', 1, 1);

-- 菜谱分类
INSERT INTO `recipe_category` (`name`, `sort`) VALUES
('川菜', 1),
('粤菜', 2),
('湘菜', 3),
('家常菜', 4),
('甜点', 5),
('早餐', 6);

-- 美食分类
INSERT INTO `food_category` (`name`, `sort`) VALUES
('地方小吃', 1),
('特色菜肴', 2),
('传统美食', 3),
('网红美食', 4),
('特色饮品', 5);

-- 轮播图
INSERT INTO `banner` (`title`, `image`, `sort`) VALUES
('地方特色美食', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=delicious%20chinese%20food%20banner&image_size=landscape_16_9', 1),
('美食分享', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=food%20sharing%20banner&image_size=landscape_16_9', 2),
('舌尖上的中国', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=chinese%20cuisine%20banner&image_size=landscape_16_9', 3);

-- 系统公告
INSERT INTO `notice` (`title`, `content`, `author`, `status`) VALUES
('欢迎来到地方特色美食分享系统', '欢迎大家来到地方特色美食分享系统，在这里您可以分享和发现各地的特色美食，交流美食心得，让我们一起开启美食之旅！', '管理员', 1),
('使用说明', '1. 用户可以浏览美食菜谱和美食信息\n2. 注册登录后可以发布菜谱和美食\n3. 可以在论坛交流美食心得\n4. 有问题请联系管理员', '管理员', 1);
