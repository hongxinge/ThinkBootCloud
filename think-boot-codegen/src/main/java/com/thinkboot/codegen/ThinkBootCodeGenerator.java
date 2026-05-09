package com.thinkboot.codegen;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Arrays;
import java.util.Collections;

public class ThinkBootCodeGenerator {

    private String url = "jdbc:mysql://localhost:3306/thinkboot?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
    private String username = "root";
    private String password = "root";
    private String[] tableNames = {};
    private String moduleName = "";
    private String author = "thinkboot";
    private String outputPath = System.getProperty("user.dir") + "/src/main/java";
    private boolean enableSwagger = true;
    private boolean enableLombok = true;
    private boolean enableRestStyle = true;
    private boolean enableEntityFile = true;
    private boolean enableMapperFile = true;
    private boolean enableServiceFile = true;
    private boolean enableServiceImplFile = true;
    private boolean enableControllerFile = true;
    private String parentPackage = "com.thinkboot";
    private String entityBaseClass = "BaseEntity";
    private boolean useBaseEntity = true;
    private String[] ignoreTablePrefix = {"tb_"};
    private IdType idType = IdType.ASSIGN_ID;
    private boolean useLogicDelete = true;
    private String logicDeleteField = "deleted";
    private String templatePath = "";

    public ThinkBootCodeGenerator url(String url) {
        this.url = url;
        return this;
    }

    public ThinkBootCodeGenerator username(String username) {
        this.username = username;
        return this;
    }

    public ThinkBootCodeGenerator password(String password) {
        this.password = password;
        return this;
    }

    public ThinkBootCodeGenerator tableName(String... tableNames) {
        this.tableNames = tableNames;
        return this;
    }

    public ThinkBootCodeGenerator moduleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public ThinkBootCodeGenerator author(String author) {
        this.author = author;
        return this;
    }

    public ThinkBootCodeGenerator outputPath(String outputPath) {
        this.outputPath = outputPath;
        return this;
    }

    public ThinkBootCodeGenerator parentPackage(String parentPackage) {
        this.parentPackage = parentPackage;
        return this;
    }

    public ThinkBootCodeGenerator enableSwagger(boolean enable) {
        this.enableSwagger = enable;
        return this;
    }

    public ThinkBootCodeGenerator enableLombok(boolean enable) {
        this.enableLombok = enable;
        return this;
    }

    public ThinkBootCodeGenerator enableRestStyle(boolean enable) {
        this.enableRestStyle = enable;
        return this;
    }

    public ThinkBootCodeGenerator entityBaseClass(String entityBaseClass) {
        this.entityBaseClass = entityBaseClass;
        return this;
    }

    public ThinkBootCodeGenerator useBaseEntity(boolean use) {
        this.useBaseEntity = use;
        return this;
    }

    public ThinkBootCodeGenerator ignoreTablePrefix(String... prefixes) {
        this.ignoreTablePrefix = prefixes;
        return this;
    }

    public ThinkBootCodeGenerator idType(IdType idType) {
        this.idType = idType;
        return this;
    }

    public ThinkBootCodeGenerator useLogicDelete(boolean use) {
        this.useLogicDelete = use;
        return this;
    }

    public ThinkBootCodeGenerator logicDeleteField(String logicDeleteField) {
        this.logicDeleteField = logicDeleteField;
        return this;
    }

    public ThinkBootCodeGenerator templatePath(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    public ThinkBootCodeGenerator disableEntity() {
        this.enableEntityFile = false;
        return this;
    }

    public ThinkBootCodeGenerator disableMapper() {
        this.enableMapperFile = false;
        return this;
    }

    public ThinkBootCodeGenerator disableService() {
        this.enableServiceFile = false;
        return this;
    }

    public ThinkBootCodeGenerator disableServiceImpl() {
        this.enableServiceImplFile = false;
        return this;
    }

    public ThinkBootCodeGenerator disableController() {
        this.enableControllerFile = false;
        return this;
    }

    public void generate() {
        if (tableNames == null || tableNames.length == 0) {
            throw new IllegalArgumentException("请设置至少一个表名");
        }

        String packagePath = parentPackage + (moduleName.isEmpty() ? "" : "." + moduleName);

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author)
                            .outputDir(outputPath)
                            .commentDate("yyyy-MM-dd")
                            .disableOpenDir();
                })
                .packageConfig(builder -> {
                    builder.parent(packagePath)
                            .entity("domain.entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, outputPath + "/" + packagePath.replace(".", "/") + "/mapper/xml"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableNames)
                            .addTablePrefix(ignoreTablePrefix)
                            .entityBuilder()
                            .enableLombok()
                            .logicDeleteColumnName(useLogicDelete ? logicDeleteField : null)
                            .idType(idType)
                            .enableTableFieldAnnotation()
                            .enableChainModel()
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel);

                    if (useBaseEntity) {
                        builder.entityBuilder()
                                .superClass(parentPackage + ".mybatis.base." + entityBaseClass)
                                .addSuperEntityColumns("id", "create_time", "update_time", "create_by", "update_by", "deleted");
                    }

                    if (enableEntityFile) {
                        builder.entityBuilder().disableSerialVersionUID();
                    }

                    builder.controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle();

                    builder.serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl");

                    builder.mapperBuilder()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper");

                    if (!enableEntityFile) {
                        builder.entityBuilder().disable();
                    }

                    if (!enableMapperFile) {
                        builder.mapperBuilder().disable();
                    }

                    if (!enableServiceFile) {
                        builder.serviceBuilder().disableService();
                    }

                    if (!enableServiceImplFile) {
                        builder.serviceBuilder().disableServiceImpl();
                    }

                    if (!enableControllerFile) {
                        builder.controllerBuilder().disable();
                    }
                })
                .templateEngine(new VelocityTemplateEngine())
                .execute();

        System.out.println("========================================");
        System.out.println("代码生成完成！");
        System.out.println("输出路径: " + outputPath);
        System.out.println("生成的表: " + Arrays.toString(tableNames));
        System.out.println("========================================");
    }
}
