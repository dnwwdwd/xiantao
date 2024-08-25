create database if not exists xiantao;

use xiantao;

-- 用户表
create table xiantao.user
(
    id           bigint auto_increment comment 'id'
        primary key,
    username     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           null comment '账户',
    avatarUrl    varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '用户性别',
    profile      varchar(512)                           null comment '个人简介',
    userPassword varchar(512)                           not null comment '用户密码',
    phone        varchar(128)                           null comment '电话',
    email        varchar(512)                           null comment '邮箱',
    userStatus   int          default 0                 not null comment '状态 0 - 正常 1-禁止 2-封号',
    createTime   datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    userRole     varchar(128) default 'user'            not null comment '用户角色 0 - 普通用户 1 - 管理员',
    likedTags    varchar(1024)                          null comment '爱好标签列表(json)'
)
    comment '用户';

-- 商品表
create table xiantao.good
(
    id           bigint auto_increment comment 'id'
        primary key,
    description  varchar(2048)                      null comment '描述',
    images       varchar(1024)                      null comment '图片列表（json数组）',
    price        decimal                            null comment '价格',
    deliveryType int      default 0                 not null comment '运送类型（0 - 不包邮 1 - 包邮 2- 自提）',
    tags         varchar(256)                       null comment '标签（json数组）',
    userId       bigint                             null comment '用户id',
    brand        varchar(64)                        null comment '品牌',
    `condition`  tinyint                            null comment '成色',
    goodStatus   varchar(128)                       null comment '表示商品的状态（完美、有损耗）',
    viewNum      bigint   default 0                 not null comment '浏览数',
    exposureNum  bigint   default 0                 not null comment '曝光数',
    wantNum      int                                null comment '想要数',
    starNum      int                                null comment '收藏数',
    type         int                                null comment '商品类型（ 0- 数码 1- 零食 2 - 家具 3 - 图书 4 - 汽车 5 - 衣服）',
    status       tinyint  default 0                 not null comment '上架状态（0 - 上架 1 - 下架）',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
);


-- 订单表
create table xiantao.`order`
(
    id         bigint auto_increment comment 'id'
        primary key,
    goodId     bigint                             null comment '商品id',
    userId     bigint                             null comment '用户id',
    type       tinyint                            null comment '类型（0 - 已购买的订单 1 - 已卖出的订单）',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '订单表';

