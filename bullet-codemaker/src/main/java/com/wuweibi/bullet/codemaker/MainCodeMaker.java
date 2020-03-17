package com.wuweibi.bullet.codemaker;
/**
 * Created by marker on 2017/12/7.
 */

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring + Mybatis  代码生成工具
 *
 *  项目结构为maven结构，但不生成pom.xml文件
 *
 *
 * 通过数据库表结构生成代码，开发模式可先进行PD建模。
 *
 *
 * @author marker
 * 2017-12-07 下午3:37
 **/
public final class MainCodeMaker {


    private MainCodeMaker() { }


    /**
     *
     * @param args 参数
     */
    public static void main(String[] args) {

        /** 生成输出目录 */
        final String projectPath  = "/Users/marker/Desktop/code";


        /** 包路径  */
        final String basePackage = "com.wuweibi.bullet";
        final String moduleName  = "";

        /** 开发者 */
        final String author      = "marker";


        /** 数据库配置 */

        String username = "root";
        String password = "123";
        String jdbcUrl  = "jdbc:mysql://localhost:3307/db_bullet?useUnicode=true&characterEncoding=utf-8&useSSL=false";


        AutoGenerator mpg = new AutoGenerator();


        // set freemarker engine
        FreemarkerTemplateEngine engine = new FreemarkerTemplateEngine();



        mpg.setTemplateEngine(engine);
        // 全局配置
        final GlobalConfig gc = new GlobalConfig();

        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(author);
        gc.setOpen(false);



        // .setKotlin(true) 是否生成 kotlin 代码
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);


        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
//        dsc.setTypeConvert(new MySqlTypeConvert() {
//            // 自定义数据库表字段类型转换【可选】
//            @Override
//            public DbColumnType processTypeConvert(String fieldType) {
//                System.out.println("转换类型：" + fieldType);
//                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
//                return super.processTypeConvert(gc, fieldType);
//            }
//        });
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(username);
        dsc.setPassword(password);
        dsc.setUrl(jdbcUrl);
        mpg.setDataSource(dsc);



        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setTablePrefix(new String[] {"t_"}); // 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel); // 表名生成策略
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setCapitalMode(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setLogicDeleteFieldName("deleted");

        // strategy.setInclude(new String[] { "user" }); // 需要生成的表
//         strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
//         strategy.setSuperEntityClass("com.wuweibi.cloud.common.core.entity.po.BasePo");
        // 自定义实体，公共字段
//         strategy.setSuperEntityColumns(new String[] {
//                 "id", "created_by", "updated_by", "updated_user_id", "created_user_id", "created_time", "updated_time"
//         });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);

        mpg.setStrategy(strategy);




        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(basePackage);
        pc.setModuleName(moduleName);
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };



        // 自定义 xxList.jsp 生成
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();



        String templatePath = "/templates/mapper.xml.ftl";
        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mybatis/mapper/" + tableInfo.getEntityName() + ".xml";

            }

        });




        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);



        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();
//        tc.setServiceImpl()
        tc.setXml(null);

        mpg.setTemplate(tc);


        mpg.execute();

        // 打印注入设置【可无】
        System.err.println(mpg.getCfg().getMap().get("abc"));


    }
}
