package xinge.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/4/24
 * @Description
 */
public class CodeGenerator {

    // 数据库类型
    private final DbType dbType = DbType.MYSQL;
    // 数据库连结信息
    private final String dbUrl = "jdbc:mysql://192.168.0.199:33060/xinblog_user?useUnicode=true&characterEncoding=UTF8" +
            "&serverTimezone=Asia/Shanghai&useSSL=false&autoReconnect=true&autoReconnectForPools=true&allowMultiQueries=true";
    private final String driver = "com.mysql.cj.jdbc.Driver"; // mysql8.0驱动,mysql5.6需改为com.mysql.jdbc.Driver
    private final String userName = "xinge";
    private final String password = "123456";

    // IntelliJ workSpace 目录，即工程物理根路径
    private final String  workSpacePath = "C:\\dev\\source\\xinblog\\";
    // 项目名, 可以采用父子项目的配法SpringBoot\springboot_mybatisplus_generate，如果无子项目，则直接写项目名称，例如：springboot
    private final String projectName = "xinblog-root\\xinblog-auth";
    // 指定包名,即controll,service,entity,mapper子包的父包
    private final String packageName = "studio.xinge.xinblog.auth";
    // 模块名，则需在模块名前加. 例：.auth, 一般不加，它是在生成的controll,service,entity包后面再加子包
    private final String moduleName = "";
    // 作者名
    private final String author = "xinge";

    // 指定生成的表名多个表之间用逗号,如果要生成所有表，可以不需要配置，执行generateCode中的方案二
    private final String[] tableNames = new String[]{"t_role","t_func"};


    @Test
    public void generateCode() {
//             serviceNameStartWithI：user -> UserService, 设置成true: user -> IUserService
        /* 方案一：只生成指定表 */
            generateByTables(false, packageName, tableNames);
        /* 方案二：.所有表生成 */
//        generateByTables(false, packageName);
        System.out.println("当前目录：" + System.getProperty("user.dir"));
    }
    /**
     * 根据表自动生成
     * @param serviceNameStartWithI 默认为false
     * @param packageName      包名
     * @param tableNames      表名
     * @author theonly
     */
    private void generateByTables(boolean serviceNameStartWithI, String packageName, String... tableNames) {
        //配置数据源
        DataSourceConfig dataSourceConfig = getDataSourceConfig();
        // 策略配置
        StrategyConfig strategyConfig = getStrategyConfig(tableNames);
        // 全局变量配置
        GlobalConfig globalConfig = getGlobalConfig(serviceNameStartWithI);
        // 包名配置
        PackageConfig packageConfig = getPackageConfig(packageName);
        // 自定义模板配置文件及输出
        InjectionConfig injectionConfig = getInjectionConfig();

        //自动生成
        atuoGenerator(dataSourceConfig, strategyConfig, globalConfig, packageConfig,injectionConfig);
    }
    /**
     * 集成
     * @param dataSourceConfig 配置数据源
     * @param strategyConfig  策略配置
     * @param config      全局变量配置
     * @param packageConfig  包名配置
     * @author theonly
     */
    private void atuoGenerator(DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig, GlobalConfig config,
                               PackageConfig packageConfig, InjectionConfig injectionConfig) {
        // 代码生成器
        new AutoGenerator()
                .setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig)
                .setCfg(injectionConfig)
                .setTemplateEngine(new FreemarkerTemplateEngine()) // 缺省Velocity不需要配这句
                .setTemplate(
                        // 关闭默认 xml 生成，调整生成 至 根目录
                        new TemplateConfig().setXml(null))
                .execute();
    }
    /**
     * 设置包名
     * @param packageName 父路径包名
     * @param packageName 模块名
     * @return PackageConfig 包名配置
     * @author theonly
     */
    private PackageConfig getPackageConfig(String packageName) {
        return new PackageConfig()
                .setParent(packageName)
                .setXml("mapper.xml" + moduleName)
                .setMapper("mapper" + moduleName)
                .setController("controller" + moduleName)
                .setEntity("entity" + moduleName);
    }
    /**
     * 全局配置
     * @param serviceNameStartWithI false
     * @return GlobalConfig
     * @author theonly
     */
    private GlobalConfig getGlobalConfig(boolean serviceNameStartWithI) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig
                // XML columList
                .setBaseColumnList(true)
                // XML ResultMap
                .setBaseResultMap(true)
                // 开启 activeRecord 模式
                .setActiveRecord(false)
                // XML 二级缓存
                .setEnableCache(false)
                //作者
                .setAuthor(author)
                //设置输出路径
                .setOutputDir(getOutputDir(projectName))
                // 主键自增
                .setIdType(AUTO)  // IdType 枚举类中有多种主键生成方式
                // 是否覆盖文件
                .setFileOverride(true);
        if (!serviceNameStartWithI) {
            //设置service名
            globalConfig.setServiceName("%sService");
        }
        /*其它设置可选项*/
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        //          .setXmlName("%sMapper").setMapperName("%sMapper")
        // .setServiceName("MP%sService")
        // .setServiceImplName("%sServiceImpl")
        // .setControllerName("%sAction")

        return globalConfig;
    }
    /**
     * 返回项目路径
     * @param projectName 项目名
     * @return 项目路径
     * @author theonly
     */
    private String getOutputDir(String projectName) {
        return workSpacePath + projectName + "/src/main/java/";

    }
    /**
     * 策略配置
     * @param tableNames 表名
     * @return StrategyConfig
     * @author theonly
     */
    private StrategyConfig getStrategyConfig(String... tableNames) {
        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<TableFill>();
        // 如 每张表都有一个创建时间、修改时间
        // 而且这基本上就是通用的了，新增时，创建时间和修改时间同时修改
        // 修改时，修改时间会修改，
        // 虽然像Mysql数据库有自动更新机制，但像ORACLE的数据库就没有了，
        // 使用公共字段填充功能，就可以实现，自动按场景更新了。
        // 如下是配置
        /*
            TableFill createField = new TableFill("gmt_create", FieldFill.INSERT);
            TableFill modifiedField = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
            tableFillList.add(createField);
            tableFillList.add(modifiedField);
        */
        return new StrategyConfig()
                // 全局大写命名 ORACLE 注意
                // .setCapitalMode(true)
                // 此处可以修改为您的表前缀，即生成的实体不要带这个前缀
                .setTablePrefix(new String[]{"tb_"})
                //从数据库表到文件的命名策略，数据库下划线命名转为java代码中驼峰命名
                .setNaming(NamingStrategy.underline_to_camel)
                // 需要生成的表,多个英文逗号分割,不加则全部生成,也可以.setInclude(new String[] {"tb_user, "tb_role"})
//                    .setInclude(tableNames)
                // 排除生成的表
                // .setExclude(new String[]{"tb_test"})
                // 时间字段缺省有值的处理，主要针对Oracle，mysql可以用数据库本身缺省设置
                // .setTableFillList(tableFillList)
                //公共父类
                // .setSuperControllerClass("com.baomidou.demo.TestController")
                // .setSuperEntityClass("com.baomidou.demo.base.BsBaseEntity")
                // .setSuperServiceClass("com.baomidou.demo.base.BsBaseService")
                // .setSuperServiceImplClass("com.baomidou.demo.base.BsBaseServiceImpl")

                // 写于父类中的公共字段
                // .setSuperEntityColumns("id") // 如果设置，那么：生成的实体类当中没有id
                // 【实体】是否生成字段常量（默认 false）
                // public static final String ID = "test_id";
                //使用lombok
                .setEntityLombokModel(true)
                // 逻辑删除属性名称
                .setLogicDeleteFieldName("deleted")
                //rest风格 Controller
                .setRestControllerStyle(true);
    }
    /**
     * 配置数据源
     * @return 数据源配置 DataSourceConfig
     * @author theonly
     */
    private DataSourceConfig getDataSourceConfig() {
        return new DataSourceConfig().setDbType(dbType)
                .setUrl(dbUrl)
                .setUsername(userName)
                .setPassword(password)
                .setDriverName(driver);
    }
    /**
     * 根据表自动生成
     * @param packageName 包名
     * @param tableNames 表名
     * @author theonly
     */
    @SuppressWarnings("unused")
    private void generateByTables(String packageName, String... tableNames) {
        generateByTables(true, packageName, tableNames);
    }

    /**
     * @Description: 自定义模板配置文件及输出
     * @Param:  无
     * @return: InjectionConfig
     * @author theonly
     **/
    private InjectionConfig   getInjectionConfig(){
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
//            String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return  workSpacePath +  projectName + "/src/main/resources/mapper/"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
    /*
    cfg.setFileCreate(new IFileCreate() {
        @Override
        public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
            // 判断自定义文件夹是否需要创建
            checkDir("调用默认方法创建的目录，自定义目录用");
            if (fileType == FileType.MAPPER) {
                // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                return !new File(filePath).exists();
            }
            // 允许生成模板文件
            return true;
        }
    });
    */
        cfg.setFileOutConfigList(focList);
        return cfg;
    }

}
