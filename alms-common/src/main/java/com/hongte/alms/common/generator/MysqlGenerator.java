package com.hongte.alms.common.generator;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hongte.alms.common.service.BaseService;

import java.io.File;
import java.util.*;

/**
 *code is far away from bug with the animal protecting
 *  ┏┓　　　┏┓
 *┏┛┻━━━┛┻┓
 *┃　　　　　　　┃ 　
 *┃　　　━　　　┃
 *┃　┳┛　┗┳　┃
 *┃　　　　　　　┃
 *┃　　　┻　　　┃
 *┃　　　　　　　┃
 *┗━┓　　　┏━┛
 *　　┃　　　┃神兽保佑
 *　　┃　　　┃代码无BUG！
 *　　┃　　　┗━━━┓
 *　　┃　　　　　　　┣┓
 *　　┃　　　　　　　┏┛
 *　　┗┓┓┏━┳┓┏┛
 *　　　┃┫┫　┃┫┫
 *　　　┗┻┛　┗┻┛
 *　　
 *   @Description : MybatisPlus代码生成器
 *   ---------------------------------
 *   @Author : Liang.Guangqing
 *   @Date : Create in 2017/9/19 14:48　
 */
public class MysqlGenerator {
    public static void main(String[] args) {

        String dburl = "jdbc:mysql://172.16.200.111:3306/hongte_alms?characterEncoding=utf8";
//        String tableName="tb_basic_company";//本次需要生成的表名
//        String tableName="tb_collection_status";//本次需要生成的表名
        String tableName="tb_derate_use_log";//本次需要生成的表名
        //1 设定生成配置
        GeneratorConfig inputConfig = new GeneratorConfig(
//                "黄咏康",
                "王继光",
                //{自定义}包模块名
                "com.hongte.alms.",
                //父类包路径(指SuperMapper，BaseService的包路径)
                "com.hongte.alms.common",
                 //{自定义}
//                "base",
                "base",
                //{自定义}项目名称
//                "alms-generator",
                "alms-generator",
                //表前缀，不需修改
                "tb_");
        //2 设定数据源账号密码
        DataSourceConfig dataSourceConfig = GeneratorUtil.setDataSorceConfig("hongte_alms","EfjsfHM5",dburl);
        //3 设定需要生成的表名
        GeneratorUtil.execute(inputConfig,dataSourceConfig,tableName);
        //4 为了避免覆盖代码，输出目录设置为{path}/alms-genertor/src/main/java ，自行复制到微服务项目后手工删除
    }

}