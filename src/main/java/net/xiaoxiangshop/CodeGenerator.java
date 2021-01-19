package net.xiaoxiangshop;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://rm-wz9fk95923wr55upvmo.mysql.rds.aliyuncs.com:3306/xiaoxiangshop-b2b2c?useUnicode=true&characterEncoding=UTF-8";
    private static final String username = "root";
    private static final String password = "hycxLI#228";
    private static final String packageName = "net.xiaoxiangshop";
    private static final String[] tables = new String[]{"yoshop_region"};

    public static void main(String[] args) {
        // 数据库配置
        DataSourceConfig dbConfig = new DataSourceConfig();
        dbConfig.setUrl(url);
        dbConfig.setUsername(username);
        dbConfig.setPassword(password);
        dbConfig.setDriverName(driver);

        // 策略配置项
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true);
        strategyConfig.setEntityLombokModel(false);
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setInclude(tables);//修改替换成你需要的表名，多个表名传数组
        strategyConfig.setSuperEntityClass("net.xiaoxiangshop.entity.BaseEntity");
        strategyConfig.setSuperEntityColumns(new String[] { "id", "createdDate", "lastModifiedDate", "version" });
        strategyConfig.setSuperMapperClass("net.xiaoxiangshop.dao.BaseDao");
        strategyConfig.setSuperServiceClass("net.xiaoxiangshop.service.BaseService");
        strategyConfig.setSuperServiceImplClass("net.xiaoxiangshop.service.BaseServiceImpl");
        strategyConfig.setSuperControllerClass("net.xiaoxiangshop.controller.admin.BaseController");

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setActiveRecord(false);
        gc.setAuthor("");
        gc.setOutputDir("/Users/liyuan/code");
        gc.setFileOverride(true);
        gc.setServiceName("%sService");
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setMapperName("%sDao");
        gc.setXmlName("%sDao");

        // 跟包相关的配置项
        PackageConfig packageConfig =  new PackageConfig();
        packageConfig.setParent(packageName);
        packageConfig.setController("controller");
        packageConfig.setEntity("entity");
        packageConfig.setMapper("dao");

        // 生成文件
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(gc);
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.setDataSource(dbConfig);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setPackageInfo(packageConfig).execute();
    }

}