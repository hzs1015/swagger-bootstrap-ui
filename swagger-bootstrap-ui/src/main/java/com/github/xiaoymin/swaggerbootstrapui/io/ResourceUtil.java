/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.swaggerbootstrapui.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 * 添加自定义Swagger Models
 * @since:swagger-bootstrap-ui 1.9.4
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/04/30 10:59
 */
public class ResourceUtil{

    private static final Logger logger= LoggerFactory.getLogger(ResourceUtil.class);

    Set<Class<?>> classes=new HashSet<>();

    private ClassLoader classLoader;

    public Set<Class<?>> getClasses() {
        return classes;
    }

    public ClassLoader getClassLoader() {
        return classLoader==null?Thread.currentThread().getContextClassLoader():classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /***
     * find resource by packageNames
     * @param packageNames
     * @return
     */
    public ResourceUtil find(String... packageNames){
        if (packageNames==null){
            throw new IllegalArgumentException("packageName can't be Empty!");
        }
        for (String packageName:packageNames){
            String packagePath=getPackagePath(packageName);
            try {
                List<String> children=VFS.getInstance().list(packagePath);
                for (String file:children){
                    if (file.endsWith(".class")){
                        String name=file.substring(0,file.indexOf('.')).replace('/','.');
                        try {
                            classes.add(getClassLoader().loadClass(name));
                        } catch (Throwable t) {
                            logger.warn("Could not examine class '" + file + "'" + " due to a " +
                                    t.getClass().getName() + " with message: " + t.getMessage());
                        }

                    }
                }
            } catch (IOException e) {
                logger.error("Could not read package: " + packageName, e);
            }

        }
        return this;
    }

    protected String getPackagePath(String packageName){
        return packageName==null?null:packageName.replace('.','/');
    }



}