drop database if exists 'blue-girl';
create database `blue-girl` default character set utf8mb4;
use `blue-girl`;

# 用户记录表
drop table if exists `t_user`;
create table `t_user` (
    `id` int not null auto_increment primary key ,
    `nick` varchar(1023) default null,
    `open_id`  varchar(255) default null comment '微信用户openId',
    `union_id`  varchar(255) default null comment '微信用户unionId',
    `head_img_url`  varchar(255) default null comment '微信用户头像',
    `sex` int default null comment '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
    `city` varchar(255) default null,
    `subscribe` int default null comment '用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号',
    `create_time` timestamp not null default NOW() comment '初次登陆时间'
)ENGINE=InnoDB auto_increment=1 default charset=utf8mb4;

# 上传文件记录表
drop table if exists `t_file_record`;
create table `t_file_record` (
    `id` int not null auto_increment primary key,
    `uid` int null comment '用户uid',
    `file_name` varchar(255) not null comment '文件名',
    `file_url` varchar(255)  comment '文件链接地址',
    `local_address` varchar(255)  comment '文件在本地服务的地址',
    `upload_time` timestamp not null default NOW() comment '上传时间'
)ENGINE=InnoDB auto_increment=1 default charset=utf8mb4;