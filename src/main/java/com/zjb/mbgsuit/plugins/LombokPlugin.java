package com.zjb.mbgsuit.plugins;

import com.zjb.mbgsuit.utils.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class LombokPlugin extends PluginAdapter {
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //添加domain的import和注解
        topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Data;"));
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.baomidou.mybatisplus.annotation.TableName"));
        topLevelClass.addAnnotation("@Table(name = \"" + introspectedTable.getFullyQualifiedTableNameAtRuntime() + "\")");

        //添加domain的注释
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine("* Created by Mybatis Generator on " + date2Str(new Date()));
        topLevelClass.addJavaDocLine("*/");

        // 为每个属性添加注释
        List<Field> fields = topLevelClass.getFields();
        for (Field field : fields) {
            StringBuilder fieldSb = new StringBuilder();
            field.addJavaDocLine("/**");
            fieldSb.append(" * ");
            String fieldName = field.getName(); // java字段名是驼峰的，需要转成下划线分割
            String underlineFieldName = StringUtils.toUnderlineName(fieldName);
            Optional<IntrospectedColumn> introspectedColumn = introspectedTable.getColumn(underlineFieldName);
            if (null == introspectedColumn) {
                continue;
            }
            fieldSb.append(introspectedColumn.get().getRemarks());
            field.addJavaDocLine(fieldSb.toString().replace("\n", " "));
            field.addJavaDocLine(" */");
        }
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.baomidou.mybatisplus.annotation.TableId"));
        String name = introspectedColumn.getActualColumnName();
        boolean isID = name.equals("ID");
        if (isID) {
            field.addAnnotation("@TableId");
        }
        FullyQualifiedJavaType type = field.getType();
        boolean isDate = type.getShortName().equalsIgnoreCase("Date");
        if (isDate) {
            field.addAnnotation(" @DateTimeFormat(pattern = \"yyyy-MM-dd\")\n" +
                    "    @JsonFormat(pattern = \"yyyy-MM-dd\", timezone = \"GMT+8\")");
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    //不生成getter
    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成setter
        return false;
    }

    private String date2Str(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(date);
    }
}
