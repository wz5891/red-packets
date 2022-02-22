CREATE TABLE red_record
(
    id          INT NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id     INT COMMENT '用户id',
    red_packet  VARCHAR(255) COMMENT '红包标识',
    total       INT COMMENT '人数',
    amount      DECIMAL(8, 2) COMMENT '总金额（单位为分）',
    is_active   INT DEFAULT 1 COMMENT '是否有效(1-是，0否)',
    create_time DATETIME COMMENT '创建时间',
    PRIMARY KEY (id)
) COMMENT = '发红包记录';

CREATE TABLE red_detail
(
    id          INT NOT NULL AUTO_INCREMENT COMMENT 'id',
    record_id   INT COMMENT '红包记录id',
    amount      DECIMAL(8, 2) COMMENT '',
    is_active   INT DEFAULT 1 COMMENT '是否有效(1-是，0否)',
    create_time DATETIME COMMENT '创建时间',
    PRIMARY KEY (id)
) COMMENT = '红包明细';

CREATE TABLE red_rob_record
(
    id         INT NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id    INT COMMENT '用户id',
    red_packet VARCHAR(255) COMMENT '红包标识串',
    amount     DECIMAL(8, 2) COMMENT '红包金额（单位为分）',
    rob_time   DATETIME COMMENT '时间',
    is_active  INT DEFAULT 1 COMMENT '是否有效(1-是，0否)',
    PRIMARY KEY (id)
) COMMENT = '抢红包记录';
