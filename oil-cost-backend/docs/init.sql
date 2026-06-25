
USE master;
GO

IF DB_ID('zyxt') IS NOT NULL
BEGIN
    ALTER DATABASE zyxt SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE zyxt;
END
GO

CREATE DATABASE zyxt;
GO

USE zyxt;
GO

-- ==========================================
-- 第一部分：基础表（英文命名，与 Spring Boot 实体一致）
-- ==========================================

-- 1. 单位信息表
CREATE TABLE dbo.unit_info
(
    unit_code VARCHAR(20) NOT NULL PRIMARY KEY,
    unit_name NVARCHAR(50) NOT NULL UNIQUE
);
GO

-- 2. 油水井表
CREATE TABLE dbo.well_info
(
    well_no VARCHAR(20) NOT NULL PRIMARY KEY,
    well_type NVARCHAR(10) NOT NULL CHECK (well_type IN (N'油井', N'水井')),
    unit_code VARCHAR(20) NOT NULL REFERENCES unit_info(unit_code)
);
GO

-- 3. 承包商表
CREATE TABLE dbo.contractor
(
    contractor_name NVARCHAR(50) NOT NULL PRIMARY KEY
);
GO

-- 4. 材料编码表
CREATE TABLE dbo.material_code
(
    material_code VARCHAR(20) NOT NULL PRIMARY KEY,
    material_name NVARCHAR(50) NOT NULL UNIQUE,
    material_unit NVARCHAR(20) NOT NULL
);
GO

-- 5. 作业项目表（核心表，移除结算金额强制等于费用之和的检查约束）
CREATE TABLE dbo.work_project
(
    project_no VARCHAR(20) NOT NULL PRIMARY KEY,
    budget_unit VARCHAR(20) NOT NULL REFERENCES unit_info(unit_code),
    well_no VARCHAR(20) NOT NULL REFERENCES well_info(well_no),
    budget_amount DECIMAL(12,2) NOT NULL CHECK (budget_amount >= 0),
    budget_person NVARCHAR(20) NOT NULL,
    budget_date DATE NOT NULL,
    start_date DATE NULL,
    finish_date DATE NULL,
    contractor_name NVARCHAR(50) NULL REFERENCES contractor(contractor_name),
    work_content NVARCHAR(100) NULL,
    material_fee DECIMAL(12,2) NULL,
    labor_fee DECIMAL(12,2) NULL,
    equipment_fee DECIMAL(12,2) NULL,
    other_fee DECIMAL(12,2) NULL,
    settlement_amount DECIMAL(12,2) NULL,           -- 可手动修改，无强制约束
    settlement_person NVARCHAR(20) NULL,
    settlement_date DATE NULL,
    account_amount DECIMAL(12,2) NULL,
    account_person NVARCHAR(20) NULL,
    account_date DATE NULL,
    CONSTRAINT CK_dates CHECK (start_date IS NULL OR finish_date IS NULL OR start_date <= finish_date)
);
GO

-- 6. 材料消耗明细表（外键级联删除）
CREATE TABLE dbo.material_cost
(
    project_no VARCHAR(20) NOT NULL,
    material_code VARCHAR(20) NOT NULL,
    quantity DECIMAL(12,2) NOT NULL CHECK (quantity > 0),
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    amount AS (quantity * price),
    PRIMARY KEY (project_no, material_code),
    FOREIGN KEY (project_no) REFERENCES work_project(project_no) ON DELETE CASCADE,
    FOREIGN KEY (material_code) REFERENCES material_code(material_code)
);
GO

-- 7. 系统用户表（前端登录用）
CREATE TABLE dbo.sys_user
(
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role_name VARCHAR(20) NOT NULL
);
GO

-- ==========================================
-- 第二部分：索引（加速日期查询）
-- ==========================================
CREATE INDEX IX_budget_date ON work_project(budget_date);
CREATE INDEX IX_settlement_date ON work_project(settlement_date);
CREATE INDEX IX_account_date ON work_project(account_date);
GO

-- ==========================================
-- 第三部分：初始数据（与前端测试数据完全一致）
-- ==========================================

-- 单位
INSERT INTO unit_info VALUES
('1122', N'采油厂'),
('112201', N'采油一矿'),
('112202', N'采油二矿'),
('112201001', N'采油一矿一队'),
('112201002', N'采油一矿二队'),
('112201003', N'采油一矿三队'),
('112202001', N'采油二矿一队'),
('112202002', N'采油二矿二队');

-- 油水井
INSERT INTO well_info VALUES
('y001', N'油井', '112201001'),
('y002', N'油井', '112201001'),
('y003', N'油井', '112201002'),
('s001', N'水井', '112201002'),
('y004', N'油井', '112201003'),
('s002', N'水井', '112202001'),
('s003', N'水井', '112202001'),
('y005', N'油井', '112202002');

-- 承包商
INSERT INTO contractor VALUES
(N'作业公司作业一队'),
(N'作业公司作业二队'),
(N'作业公司作业三队');

-- 材料
INSERT INTO material_code VALUES
('wm001', N'材料一', N'吨'),
('wm002', N'材料二', N'米'),
('wm003', N'材料三', N'桶'),
('wm004', N'材料四', N'袋');

-- 默认管理员
INSERT INTO sys_user VALUES ('admin', '123456', 'ADMIN');

-- 5个初始作业项目（包含历史数据）
INSERT INTO work_project
    (project_no, budget_unit, well_no, budget_amount, budget_person, budget_date,
     start_date, finish_date, contractor_name, work_content,
     material_fee, labor_fee, equipment_fee, other_fee,
     settlement_amount, settlement_person, settlement_date,
     account_amount, account_person, account_date)
VALUES
('zy2018001', '112201001', 'y001', 10000.00, N'张三', '2018-05-01',
 '2018-05-04', '2018-05-25', N'作业公司作业一队', N'堵漏',
 7000.00, 2500.00, 1000.00, 1400.00,
 11900.00, N'李四', '2018-05-26',
 11900.00, N'王五', '2018-05-28'),
('zy2018002', '112201002', 'y003', 11000.00, N'张三', '2018-05-01',
 '2018-05-04', '2018-05-23', N'作业公司作业二队', N'检泵',
 6000.00, 1500.00, 1000.00, 2400.00,
 10900.00, N'李四', '2018-05-26',
 10900.00, N'王五', '2018-05-28'),
('zy2018003', '112201002', 's001', 10500.00, N'张三', '2018-05-01',
 '2018-05-06', '2018-05-23', N'作业公司作业二队', N'调剖',
 6500.00, 2000.00, 500.00, 1400.00,
 10400.00, N'李四', '2018-05-26',
 10400.00, N'王五', '2018-05-28'),
('zy2018004', '112202001', 's002', 12000.00, N'张三', '2018-05-01',
 '2018-05-04', '2018-05-24', N'作业公司作业三队', N'解堵',
 6000.00, 2000.00, 1000.00, 1600.00,
 10600.00, N'李四', '2018-05-26',
 10600.00, N'赵六', '2018-05-28'),
('zy2018005', '112202002', 'y005', 12000.00, N'张三', '2018-05-01',
 '2018-05-04', '2018-05-28', N'作业公司作业三队', N'防砂',
 7000.00, 1000.00, 2000.00, 1300.00,
 11300.00, N'李四', '2018-06-01',
 0, NULL, NULL);

-- 材料消耗明细
INSERT INTO material_cost (project_no, material_code, quantity, price) VALUES
('zy2018001', 'wm001', 200, 10.00),
('zy2018001', 'wm002', 200, 10.00),
('zy2018001', 'wm003', 200, 10.00),
('zy2018001', 'wm004', 100, 10.00),
('zy2018002', 'wm001', 200, 10.00),
('zy2018002', 'wm002', 200, 10.00),
('zy2018002', 'wm003', 200, 10.00),
('zy2018003', 'wm001', 200, 10.00),
('zy2018003', 'wm002', 200, 10.00),
('zy2018003', 'wm003', 250, 10.00),
('zy2018004', 'wm001', 200, 10.00),
('zy2018004', 'wm002', 200, 10.00),
('zy2018004', 'wm004', 200, 10.00),
('zy2018005', 'wm001', 200, 10.00),
('zy2018005', 'wm002', 200, 10.00),
('zy2018005', 'wm004', 300, 10.00);
GO


-- 视图：项目材料概览（无害）
CREATE VIEW dbo.v_project_material AS
SELECT p.*, mc.material_code, mc.quantity, mc.price, mc.amount,
       m.material_name, m.material_unit
FROM work_project p
LEFT JOIN material_cost mc ON p.project_no = mc.project_no
LEFT JOIN material_code m ON mc.material_code = m.material_code;
GO

-- 存储过程：成本统计（无害）
CREATE PROCEDURE dbo.usp_cost_summary
    @unit_code VARCHAR(20),
    @start_date DATE,
    @end_date DATE
AS
BEGIN
    SET NOCOUNT ON;
    SELECT 
        @unit_code AS unit_code,
        ISNULL(SUM(budget_amount),0) AS total_budget,
        ISNULL(SUM(settlement_amount),0) AS total_settlement,
        ISNULL(SUM(account_amount),0) AS total_account
    FROM work_project
    WHERE budget_unit LIKE @unit_code + '%'
      AND budget_date BETWEEN @start_date AND @end_date;
END;
GO

PRINT '===== 数据库 zyxt 创建成功，前后端完全兼容 =====';
GO
