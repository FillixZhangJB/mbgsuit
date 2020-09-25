package com.zjb.mbgsuit.config;

public class FileUrl {
    private String controllerUrl;
    private String daoUrl;
    private String serviceUrl;
    private String serviceImplUrl;
    private String beanUrl;
    private String targetProject;
    private String templateUrl;


    public String getControllerUrl() {
        return controllerUrl;
    }

    public void setControllerUrl(String controllerUrl) {
        this.controllerUrl = controllerUrl;
    }

    public String getDaoUrl() {
        return daoUrl;
    }

    public void setDaoUrl(String daoUrl) {
        this.daoUrl = daoUrl;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getServiceImplUrl() {
        return serviceImplUrl;
    }

    public void setServiceImplUrl(String serviceImplUrl) {
        this.serviceImplUrl = serviceImplUrl;
    }

    public String getBeanUrl() {
        return beanUrl;
    }

    public void setBeanUrl(String beanUrl) {
        this.beanUrl = beanUrl;
    }


    public String getTargetProject() {
        return targetProject;
    }

    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }
}
