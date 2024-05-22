-- auto-generated definition
create table user
(
    id          bigint auto_increment comment '用户主键'
        primary key,
    username    varchar(256)                       not null comment '用户名',
    password    varchar(512)                       not null comment '密码',
    avatar_url  varchar(1024)                      null comment '头像图片地址',
    gender      tinyint                            null comment '性别',
    phone       varchar(128)                       null comment '手机号',
    email       varchar(512)                       null comment '电子邮箱',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    status      int      default 0                 not null comment '用户状态 0 正常',
    deleted     tinyint  default 0                 not null comment '逻辑删除 0 正常',
    user_role   int      default 0                 not null comment '用户身份 0-用户 1-管理员',
    constraint user_pk
        unique (username)
)
    comment '用户';

insert into user_center.user (id, username, password, avatar_url, gender, phone, email, create_time, update_time, status, deleted, user_role)
values  (1, 'swshenyun', '12345678sw', 'https://shenyunmomie.oss-cn-beijing.aliyuncs.com/imags/202307051931160.png', 1, '18732566535', '18731548870@163.com', '2024-05-19 02:16:19', '2024-05-19 02:16:19', 0, 0, 1);