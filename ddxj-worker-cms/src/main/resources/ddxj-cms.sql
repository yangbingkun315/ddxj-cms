/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : ddxj-cms

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2018-08-10 11:02:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for zn_cms_resource
-- ----------------------------
DROP TABLE IF EXISTS `zn_cms_resource`;
CREATE TABLE `zn_cms_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL COMMENT '上级ID',
  `resource_name` varchar(255) DEFAULT NULL COMMENT '资源名称',
  `resource_key` varchar(255) DEFAULT NULL COMMENT '资源唯一标识',
  `resource_type` int(11) DEFAULT NULL COMMENT '资源类型,0:目录;1:菜单;2:按钮',
  `resource_url` varchar(255) DEFAULT NULL COMMENT '资源url',
  `resource_level` int(11) DEFAULT NULL COMMENT '层级',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `resource_description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `flag` int(11) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zn_cms_resource
-- ----------------------------
INSERT INTO `zn_cms_resource` VALUES ('1', null, '系统管理', 'AAAA', '0', null, '1', '1', null, null, '2018-08-09 10:41:00', '2018-08-09 10:41:00', '1');
INSERT INTO `zn_cms_resource` VALUES ('2', null, '用户管理', 'BBBB', '1', null, '1', '1', null, null, '2018-08-09 10:41:00', '2018-08-09 10:41:00', '1');
INSERT INTO `zn_cms_resource` VALUES ('3', null, '角色管理', 'CCCC', '2', null, '1', '1', null, null, '2018-08-09 10:41:00', '2018-08-09 10:41:00', '1');
INSERT INTO `zn_cms_resource` VALUES ('4', null, '资源管理', 'DDDD', '3', null, '1', '1', null, null, '2018-08-09 10:42:48', '2018-08-09 10:41:00', '1');

-- ----------------------------
-- Table structure for zn_cms_role
-- ----------------------------
DROP TABLE IF EXISTS `zn_cms_role`;
CREATE TABLE `zn_cms_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `role_key` varchar(255) DEFAULT NULL COMMENT '角色key',
  `role_description` varchar(255) DEFAULT NULL COMMENT ' 角色描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `flag` varchar(255) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zn_cms_role
-- ----------------------------
INSERT INTO `zn_cms_role` VALUES ('1', '系统管理员', 'DSSD123FSADAS', '针对于平台全部', '2018-08-09 10:41:00', '2018-08-09 10:41:00', '1');

-- ----------------------------
-- Table structure for zn_cms_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `zn_cms_role_resource`;
CREATE TABLE `zn_cms_role_resource` (
  `role_id` int(11) DEFAULT NULL,
  `resource_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zn_cms_role_resource
-- ----------------------------
INSERT INTO `zn_cms_role_resource` VALUES ('1', '1');
INSERT INTO `zn_cms_role_resource` VALUES ('1', '2');
INSERT INTO `zn_cms_role_resource` VALUES ('1', '3');
INSERT INTO `zn_cms_role_resource` VALUES ('1', '4');

-- ----------------------------
-- Table structure for zn_cms_user
-- ----------------------------
DROP TABLE IF EXISTS `zn_cms_user`;
CREATE TABLE `zn_cms_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(255) DEFAULT NULL COMMENT ' 昵称',
  `user_name` varchar(255) DEFAULT NULL COMMENT '账户名',
  `password` varchar(255) DEFAULT NULL COMMENT '用户密码',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别 G 女 M 男',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `telphone` varchar(11) DEFAULT NULL COMMENT '电话',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) DEFAULT NULL COMMENT '住址',
  `locked` int(255) DEFAULT 0 COMMENT '0 未锁定 1 锁定',
  `description` varchar(255) DEFAULT NULL COMMENT '用户描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `flag` int(11) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of zn_cms_user
-- ----------------------------
INSERT INTO `zn_cms_user` VALUES ('1', '点点小匠', 'admin', 'admin', '1', 'G', '1989-08-09 10:17:31', '17621328341', 'junhuihe@diandxj.com', '上海市杨浦区杨树浦路1088号东方渔人码头701室', '0', '点点小匠', '2018-08-09 10:18:33', '2018-08-09 10:18:35', '1');
