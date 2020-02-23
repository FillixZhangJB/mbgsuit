package com.zjb.mbgsuit.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.plugins.MapperAnnotationPlugin;

import java.util.List;
import java.util.Set;

/**
 * 当使用JPA时，可以自动加上@Repository
 */
public class ClientSuperInterfaceTypeArgumentPlugin extends MapperAnnotationPlugin {
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        FullyQualifiedJavaType idClass = primaryKeyColumns.get(0).getFullyQualifiedJavaType();
        String idClassShortName = idClass.getShortName();

        Set<FullyQualifiedJavaType> superInterfaceTypes = interfaze.getSuperInterfaceTypes();
        for (FullyQualifiedJavaType superInterfaceType : superInterfaceTypes) {
            superInterfaceType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getFullyQualifiedTable().getDomainObjectName()));
            superInterfaceType.addTypeArgument(new FullyQualifiedJavaType(idClassShortName));
        }
        return true;
    }
}
