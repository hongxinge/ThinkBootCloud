package com.thinkboot.codegen.example;

import com.baomidou.mybatisplus.annotation.IdType;
import com.thinkboot.codegen.ThinkBootCodeGenerator;

public class CodeGeneratorExample {

    public static void main(String[] args) {
        ThinkBootCodeGenerator generator = new ThinkBootCodeGenerator();

        generator.url("jdbc:mysql://localhost:3306/thinkboot?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
                .username("root")
                .password("root")
                .tableName("tb_user", "tb_order")
                .moduleName("system")
                .author("YourName")
                .outputPath("D:/project/think-boot-example/src/main/java")
                .parentPackage("com.thinkboot")
                .useBaseEntity(true)
                .useLogicDelete(true)
                .logicDeleteField("deleted")
                .idType(IdType.ASSIGN_ID)
                .ignoreTablePrefix("tb_")
                .generate();
    }
}
