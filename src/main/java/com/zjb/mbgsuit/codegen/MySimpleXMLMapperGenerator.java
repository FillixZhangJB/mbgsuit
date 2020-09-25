package com.zjb.mbgsuit.codegen;

import com.zjb.mbgsuit.config.FileUrl;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.SimpleXMLMapperGenerator;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.messages.Messages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MySimpleXMLMapperGenerator extends SimpleXMLMapperGenerator {
    static FileUrl fileUrl;

    @Override
    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        Context context = this.introspectedTable.getContext();
        Properties properties = context.getProperties();
        this.progressCallback.startTask(Messages.getString("Progress.12", String.valueOf(new String[]{table.toString()})));
        XmlElement answer = new XmlElement("mapper");
        String namespace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));
        this.context.getCommentGenerator().addRootComment(answer);
        this.addResultMapElement(answer);
        this.addDeleteByPrimaryKeyElement(answer);
        this.addInsertElement(answer);
        this.addUpdateByPrimaryKeyElement(answer);
        this.addSelectByPrimaryKeyElement(answer);
        this.addSelectAllElement(answer);
        //这个读取配置文件  controller，service和serviceImpl 生成的路径配置
        getProperties(properties);
        fileUrl.setBeanUrl(introspectedTable.getBaseRecordType());

        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        FullyQualifiedJavaType idClass = primaryKeyColumns.get(0).getFullyQualifiedJavaType();
        String idClassShortName = idClass.getShortName();

        try {
            genDAO(introspectedTable.getTableConfiguration().getDomainObjectName(), idClassShortName, "id");
            genController(introspectedTable.getTableConfiguration().getDomainObjectName(), idClassShortName, "id");
            genService(introspectedTable.getTableConfiguration().getDomainObjectName(), idClassShortName, "id");
            genServiceImpl(introspectedTable.getTableConfiguration().getDomainObjectName(), idClassShortName, "id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }

    //Controller生成
    public static void genController(String name, String primaryType, String primaryKey) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("controllerUrl", fileUrl.getControllerUrl());
        paramMap.put("serviceUrl", fileUrl.getServiceUrl());
        paramMap.put("url", fileUrl.getBeanUrl());
        paramMap.put("name", name);
        paramMap.put("primaryType", primaryType);
        paramMap.put("primaryKey", primaryKey);
        paramMap.put("capitalName", name);
        genFile(paramMap, fileUrl.getControllerUrl(), "Controller", "controller.vm");
    }

    //Service生成
    public static void genService(String name, String primaryType, String primaryKey) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("serviceUrl", fileUrl.getServiceUrl());
        paramMap.put("url", fileUrl.getBeanUrl());
        paramMap.put("name", name);
        paramMap.put("primaryType", primaryType);
        paramMap.put("primaryKey", primaryKey);
        genFile(paramMap, fileUrl.getServiceUrl(), "Service", "service.vm");
    }

    //ServiceImpl生成
    public static void genServiceImpl(String name, String primaryType, String primaryKey) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("serviceImplUrl", fileUrl.getServiceImplUrl());
        paramMap.put("serviceUrl", fileUrl.getServiceUrl());
        paramMap.put("mapperUrl", fileUrl.getDaoUrl());
        paramMap.put("url", fileUrl.getBeanUrl());
        paramMap.put("name", name);
        paramMap.put("primaryType", primaryType);
        paramMap.put("primaryKey", primaryKey);
        genFile(paramMap, fileUrl.getServiceImplUrl(), "ServiceImpl", "serviceImpl.vm");
    }

    //genDAO生成
    public static void genDAO(String name, String primaryType, String primaryKey) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("mapperUrl", fileUrl.getDaoUrl());
        paramMap.put("url", fileUrl.getBeanUrl());
        paramMap.put("name", name);
        paramMap.put("primaryType", primaryType);
        paramMap.put("primaryKey", primaryKey);
        genFile(paramMap, fileUrl.getDaoUrl(), "Mapper", "mapper.vm");
    }

    //获取模板
    public static void genFile(Map<String, Object> paramMap, String fileurl, String filesuffix, String filetemplate) throws Exception {
        //创建一个合适的Configration对象
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(Configuration.VERSION_2_3_23);
        if (fileUrl.getTemplateUrl() == null || fileUrl.getTemplateUrl().length() == 0) {
            configuration.setDirectoryForTemplateLoading(new File(fileUrl.getTemplateUrl()));
        } else {
            configuration.setDirectoryForTemplateLoading(new File(MySimpleXMLMapperGenerator.class.getResource("/").getPath() + "/template"));
        }

        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
        configuration.setDefaultEncoding("UTF-8");   //这个一定要设置，不然在生成的页面中 会乱码
        //获取或创建一个模版。
        Template template = configuration.getTemplate(filetemplate);
        File file = new File(fileUrl.getTargetProject() + fileurl.replace(".", "\\\\"));
        if (!file.exists()) {
            file.mkdirs();
        }
        Writer writer = new OutputStreamWriter(new FileOutputStream(fileUrl.getTargetProject() + fileurl.replace(".", "\\\\") + "\\" + paramMap.get("name") + filesuffix + ".java"), "UTF-8");
        template.process(paramMap, writer);
        System.out.println("恭喜，生成成功~~");
    }


    public static void getProperties(Properties properties) {
        fileUrl = new FileUrl();
        fileUrl.setControllerUrl(properties.getProperty("controller.package"));
        fileUrl.setDaoUrl(properties.getProperty("dao.package"));
        fileUrl.setServiceImplUrl(properties.getProperty("serviceImpl.package"));
        fileUrl.setServiceUrl(properties.getProperty("service.package"));
        fileUrl.setTargetProject(properties.getProperty("targetProject"));
        fileUrl.setTemplateUrl(properties.getProperty("templateUrl"));
    }
}

