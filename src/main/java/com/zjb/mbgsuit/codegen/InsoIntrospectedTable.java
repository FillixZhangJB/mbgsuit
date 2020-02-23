package com.zjb.mbgsuit.codegen;

import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3SimpleImpl;

import java.util.List;

public class InsoIntrospectedTable extends IntrospectedTableMyBatis3SimpleImpl {
    @Override
    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings, ProgressCallback progressCallback) {
        //这个添加自定模板生成方法
        this.xmlMapperGenerator = new MySimpleXMLMapperGenerator();
        this.initializeAbstractGenerator(this.xmlMapperGenerator, warnings, progressCallback);
    }
}
