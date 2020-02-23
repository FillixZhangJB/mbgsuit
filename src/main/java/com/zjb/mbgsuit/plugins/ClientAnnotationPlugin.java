package com.zjb.mbgsuit.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.plugins.MapperAnnotationPlugin;

/**
 * 当使用JPA时，可以自动加上@Repository
 */
public class ClientAnnotationPlugin extends MapperAnnotationPlugin {
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            // don't need to do this for MYBATIS3_DSQL as that runtime already adds this annotation
            interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Repository")); //$NON-NLS-1$
            interfaze.addAnnotation("@Repository"); //$NON-NLS-1$
        }
        return true;
    }
}
