-- 助学帮扶管理系统 数据库初始化脚本
-- 使用前请确保MySQL已启动且拥有创建数据库权限

DROP DATABASE IF EXISTS funding_db;
CREATE DATABASE funding_db DEFAULT CHARACTER SET utf8mb4;
USE funding_db;

DROP TABLE IF EXISTS score_detail;
DROP TABLE IF EXISTS score;
DROP TABLE IF EXISTS score_item;
DROP TABLE IF EXISTS visit;
DROP TABLE IF EXISTS fund;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50),
    role VARCHAR(20) NOT NULL,
    phone VARCHAR(20),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    id_card VARCHAR(20),
    school VARCHAR(100),
    grade VARCHAR(50),
    family_address VARCHAR(255),
    family_situation TEXT,
    phone VARCHAR(20),
    sponsor_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE fund (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    sponsor_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    fund_date DATE,
    purpose VARCHAR(255),
    remark VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE visit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    sponsor_id BIGINT NOT NULL,
    visit_date DATE,
    content TEXT,
    situation VARCHAR(255),
    problem TEXT,
    visitor VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE score_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    weight DECIMAL(5,2) NOT NULL,
    description VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE score (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    sponsor_id BIGINT NOT NULL,
    score_date DATE,
    total_score DECIMAL(6,2),
    remark VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE score_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    score_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    item_score DECIMAL(5,2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 预置用户数据（Demo简化：密码明文存储）
INSERT INTO sys_user (id, username, password, real_name, role, phone) VALUES
(1, 'admin', '123456', '系统管理员', 'ADMIN', '13800000001'),
(2, 'sponsor', '123456', '张资助', 'SPONSOR', '13800000002');

-- 评分指标（权重总和100）
INSERT INTO score_item (id, name, weight, description) VALUES
(1, '学习情况', 30.00, '学习成绩与学习态度'),
(2, '生活改善', 25.00, '日常生活条件改善情况'),
(3, '心理状态', 20.00, '心理健康与情绪状态'),
(4, '家庭帮扶成效', 25.00, '家庭整体帮扶成效');

-- 学生数据（资助人 sponsor_id=2）
INSERT INTO student (id, name, gender, id_card, school, grade, family_address, family_situation, phone, sponsor_id) VALUES
(1, '李明', '男', '110101201001010015', '北京第一中学', '高二', '北京市朝阳区幸福路10号', '父亲因病去世，母亲打零工收入微薄，家庭经济困难', '13800000011', 2),
(2, '王红', '女', '110101200901010028', '北京第二中学', '高一', '北京市海淀区希望路5号', '单亲家庭，母亲务农，收入不稳定', '13800000012', 2),
(3, '陈强', '男', '110101200801010037', '北京第三中学', '高三', '北京市西城区阳光胡同3号', '父母双残，无劳动能力，靠低保维持生活', '13800000013', 2);

-- 资金流水
INSERT INTO fund (id, student_id, sponsor_id, amount, fund_date, purpose, remark) VALUES
(1, 1, 2, 2000.00, '2024-03-01', '学费', '春季学期学费'),
(2, 1, 2, 1500.00, '2024-05-10', '生活费', '五月生活费补助'),
(3, 2, 2, 1800.00, '2024-03-05', '学费', '春季学期学费'),
(4, 2, 2, 1200.00, '2024-06-01', '书本费', '购买教辅资料'),
(5, 3, 2, 3000.00, '2024-03-10', '学费', '高考冲刺学期学费');

-- 回访记录
INSERT INTO visit (id, student_id, sponsor_id, visit_date, content, situation, problem, visitor) VALUES
(1, 1, 2, '2024-04-15', '入户走访，了解学习和生活情况', '学习态度积极，成绩稳步提升', '家庭仍需持续帮扶', '张资助'),
(2, 2, 2, '2024-04-20', '电话回访及家访', '性格开朗，适应高中生活', '母亲身体欠佳需关注', '张资助'),
(3, 3, 2, '2024-04-25', '高考前心理疏导走访', '压力较大但状态稳定', '需加强心理辅导', '张资助');

-- 评分记录
-- 李明: 90/85/80/88 -> 90*0.3+85*0.25+80*0.2+88*0.25 = 27+21.25+16+22 = 86.25
INSERT INTO score (id, student_id, sponsor_id, score_date, total_score, remark) VALUES
(1, 1, 2, '2024-06-30', 86.25, '整体表现良好，学习稳步提升'),
-- 王红: 88/92/85/90 -> 88*0.3+92*0.25+85*0.2+90*0.25 = 26.4+23+17+22.5 = 88.90
(2, 2, 2, '2024-06-30', 88.90, '进步明显，生活改善显著'),
-- 陈强: 75/80/70/78 -> 75*0.3+80*0.25+70*0.2+78*0.25 = 22.5+20+14+19.5 = 76.00
(3, 3, 2, '2024-06-30', 76.00, '需继续努力，关注心理状态');

-- 评分明细
INSERT INTO score_detail (id, score_id, item_id, item_score) VALUES
(1, 1, 1, 90.00),
(2, 1, 2, 85.00),
(3, 1, 3, 80.00),
(4, 1, 4, 88.00),
(5, 2, 1, 88.00),
(6, 2, 2, 92.00),
(7, 2, 3, 85.00),
(8, 2, 4, 90.00),
(9, 3, 1, 75.00),
(10, 3, 2, 80.00),
(11, 3, 3, 70.00),
(12, 3, 4, 78.00);
