package com.zjb.mbgsuit.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

public class ModelAnnotationPlugin extends PluginAdapter {
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType(
                new FullyQualifiedJavaType("javax.persistence.Entity"));
        topLevelClass.addAnnotation("@Entity");
        topLevelClass.addImportedType(
                new FullyQualifiedJavaType("javax.persistence.Table"));
        topLevelClass.addAnnotation("@Table(name = \"" + introspectedTable.getFullyQualifiedTableNameAtRuntime() + "\")");
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
//        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.hibernate.annotations.GenericGenerator"));
//        topLevelClass.addImportedType(new FullyQualifiedJavaType("javax.persistence.*"));
//        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.fasterxml.jackson.annotation.JsonFormat"));
//        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.format.annotation.DateTimeFormat"));
        String name = introspectedColumn.getActualColumnName();
        boolean isID = name.equals("ID");
        if (isID) {
            field.addAnnotation(" @GenericGenerator(name = \"idGenerator\", strategy = \"uuid\")\n" +
                    "    @GeneratedValue(generator = \"idGenerator\")");
            field.addAnnotation("@Id");
        }
        FullyQualifiedJavaType type = field.getType();
        boolean isDate = type.getShortName().equalsIgnoreCase("Date");
        if (isDate) {
            field.addAnnotation(" @DateTimeFormat(pattern = \"yyyy-MM-dd\")\n" +
                    "    @JsonFormat(pattern = \"yyyy-MM-dd\", timezone = \"GMT+8\")");
        }
        field.addAnnotation("@Column(name = \"" + introspectedColumn.getActualColumnName() + "\")");
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
