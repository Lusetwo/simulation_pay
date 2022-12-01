/*
 Navicat Premium Data Transfer

 Source Server         : spaymen_test
 Source Server Type    : MySQL
 Source Server Version : 50616
 Source Host           : rm-wz9d27c48r7g6no6deo.mysql.rds.aliyuncs.com:3306
 Source Schema         : spaymen_test

 Target Server Type    : MySQL
 Target Server Version : 50616
 File Encoding         : 65001

 Date: 19/08/2021 21:27:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员编号',
  `user_uuid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户外键uuid',
  `admin_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员名称',
  `admin_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员密码',
  `admin_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员头像',
  `admin_role` int(255) NOT NULL DEFAULT 1 COMMENT '角色标识',
  `create_date` date NULL DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `admin_role`(`admin_role`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'fdaf26459', '张三丰', 'deep0001', NULL, 1, NULL);
INSERT INTO `admin` VALUES (2, '0f4a52ffc', '毛不易', 'deep0002', NULL, 1, NULL);
INSERT INTO `admin` VALUES (3, '922223669', '周深', 'deep0003', NULL, 1, NULL);
INSERT INTO `admin` VALUES (4, '543b6ff15', '华晨宇', 'deep0004', NULL, 1, NULL);
INSERT INTO `admin` VALUES (5, 'bf608a573', '隔壁老樊', 'deep0005', NULL, 1, NULL);
INSERT INTO `admin` VALUES (6, '6955890ff', '胡夏', 'deep0006', NULL, 1, NULL);
INSERT INTO `admin` VALUES (7, '842918b2f', '萧敬腾', 'deep0007', NULL, 1, NULL);
INSERT INTO `admin` VALUES (8, '55dd5c314', '徐佳莹', 'deep0008', NULL, 1, NULL);
INSERT INTO `admin` VALUES (9, '1a299a8a5', '袁娅维', 'deep0009', NULL, 1, NULL);
INSERT INTO `admin` VALUES (10, 'e04981d10', '张韶涵', 'deep0010', NULL, 1, NULL);
INSERT INTO `admin` VALUES (11, '030f587c4', '赵雷', 'depp0011', NULL, 1, NULL);
INSERT INTO `admin` VALUES (12, '4611f367c', '李建', 'depp0012', NULL, 1, NULL);
INSERT INTO `admin` VALUES (13, '010938c4a', '王峰', 'deep0012', NULL, 1, NULL);
INSERT INTO `admin` VALUES (14, '010938c4a', '刘凯宏', '789', NULL, 1, NULL);

-- ----------------------------
-- Table structure for classes
-- ----------------------------
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `class_uuid` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级主键ID',
  `class_director` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班主任',
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '班级名称',
  `del_flag` int(10) NOT NULL DEFAULT 1 COMMENT '是否可用(0/1)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of classes
-- ----------------------------

-- ----------------------------
-- Table structure for friend_apply_for
-- ----------------------------
DROP TABLE IF EXISTS `friend_apply_for`;
CREATE TABLE `friend_apply_for`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '好友申请表编号',
  `uuid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请唯一标识',
  `user_uuid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被申请人ID',
  `apply_for_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请人ID',
  `apply_for_struts` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '申请状态 0为未申请 1为已申请',
  `apply_date` date NULL DEFAULT NULL COMMENT '申请时间',
  `del_flag` int(1) NULL DEFAULT 1 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 174 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '好友申请表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of friend_apply_for
-- ----------------------------

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品编号',
  `goods_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `goods_sort` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品分类',
  `goods_picture` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品图片',
  `goods_store` int(11) NULL DEFAULT NULL COMMENT '商品库存',
  `goods_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '商品价格',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
  `goods_barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品条形码',
  `del_flag` int(1) NULL DEFAULT 1 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 84 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of goods
-- ----------------------------

-- ----------------------------
-- Table structure for goods_sort
-- ----------------------------
DROP TABLE IF EXISTS `goods_sort`;
CREATE TABLE `goods_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品分类编号',
  `goods_sort_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品分类名称',
  `goods_sort_alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品分类别名',
  `goods_sort_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品分类描述',
  `is_show` int(1) NULL DEFAULT 0 COMMENT '商品分类是否显示',
  `goods_sort_rank` int(11) NULL DEFAULT 1 COMMENT '商品分类排序',
  `del_flag` int(1) NULL DEFAULT 1 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `goods_sort_name`(`goods_sort_name`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品分类表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of goods_sort
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色名称',
  `del_flag` int(10) NOT NULL DEFAULT 1 COMMENT '是否可用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '管理员', 1);
INSERT INTO `role` VALUES (2, '教师', 1);
INSERT INTO `role` VALUES (3, '学生', 1);
INSERT INTO `role` VALUES (4, '班主任', 1);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '学生编号',
  `user_uuid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户外键uuid',
  `student_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生名称',
  `sex` int(10) NULL DEFAULT NULL COMMENT '学生性别',
  `student_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生登录密码',
  `pay_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生支付密码',
  `student_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生头像',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生昵称',
  `loose_change` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '学生零钱',
  `student_phone` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生手机号',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '家庭住址',
  `create_date` date NOT NULL COMMENT '注册时间',
  `role` int(10) NOT NULL DEFAULT 3 COMMENT '角色',
  `status` int(10) NOT NULL DEFAULT 1 COMMENT '是否(0/1)停用',
  `update_date` date NULL DEFAULT NULL COMMENT '最新修改时间',
  `del_flag` int(20) NOT NULL DEFAULT 1 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_uuid`(`user_uuid`) USING BTREE,
  INDEX `student`(`role`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1516 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学生表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of student
-- ----------------------------

-- ----------------------------
-- Table structure for student_bank_card
-- ----------------------------
DROP TABLE IF EXISTS `student_bank_card`;
CREATE TABLE `student_bank_card`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '银行卡编号',
  `user_uuid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户外键UUID',
  `bank_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行卡卡号',
  `is_untie` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '银行卡是否解绑',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 144 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '银行卡表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of student_bank_card
-- ----------------------------

-- ----------------------------
-- Table structure for student_class
-- ----------------------------
DROP TABLE IF EXISTS `student_class`;
CREATE TABLE `student_class`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_uuid` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '学生uuid',
  `class_uuid` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '班级uuid',
  `del_flag` int(10) NOT NULL DEFAULT 1 COMMENT '是否(1/0)可用',
  `create_date` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1102 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of student_class
-- ----------------------------

-- ----------------------------
-- Table structure for student_friend
-- ----------------------------
DROP TABLE IF EXISTS `student_friend`;
CREATE TABLE `student_friend`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '学生好友编号',
  `user_uuid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `friend_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '好友ID',
  `is_friend` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否好友 1是 0否',
  `del_flag` int(1) NULL DEFAULT 1 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 161 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学生好友表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of student_friend
-- ----------------------------

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '教师编号',
  `user_uuid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户外键uuid',
  `teacher_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师名称',
  `teacher_phone` char(17) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师手机号',
  `teacher_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师登录密码',
  `loose_change` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '教师金额',
  `teacher_nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师昵称',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `role` int(10) NULL DEFAULT 2 COMMENT '教师角色绑定',
  `update_time` date NULL DEFAULT NULL COMMENT '最新修改信息时间',
  `del_flag` int(1) NULL DEFAULT 1 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_uuid`(`user_uuid`) USING BTREE,
  INDEX `teacher_role`(`role`) USING BTREE,
  CONSTRAINT `teacher_role` FOREIGN KEY (`role`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 109 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教师表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of teacher
-- ----------------------------

-- ----------------------------
-- Table structure for teacher_class
-- ----------------------------
DROP TABLE IF EXISTS `teacher_class`;
CREATE TABLE `teacher_class`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `teacher_uuid` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '教师绑定',
  `class_uuid` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '班级绑定',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '是否(1/0)可用',
  `create_date` date NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 128 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of teacher_class
-- ----------------------------

-- ----------------------------
-- Table structure for user_order
-- ----------------------------
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单编号',
  `user_uuid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户外键uuid',
  `order_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单金额',
  `create_time` date NULL DEFAULT NULL COMMENT '订单创建时间',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `goods_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '订单中的商品信息',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` int(1) NULL DEFAULT 1 COMMENT '删除标记',
  `goods_sum` int(10) NULL DEFAULT NULL COMMENT '商品总量',
  `goods_barcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品条形码',
  `goods_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `status` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '是否完成支付',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 408 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_order
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
