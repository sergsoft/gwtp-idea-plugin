package com.arcbees.plugin.idea.domain;

import com.arcbees.plugin.idea.utils.IdUtils;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by serg on 22.08.2014.
 */
public class ActionModel {

    private Project project;

    private String name;

    private String packageName;

    private PsiClass superClass;

    private List<ParameterModel> actionFields = new ArrayList<ParameterModel>();

    private List<ParameterModel> resultFileds = new ArrayList<ParameterModel>();

    private String actionHandlerPkg;

    private PsiClass actionValidator;

    private Module handlerModule;

    private boolean withoutSecure = false;

    private Module module;

    public ActionModel(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public PsiClass getSuperClass() {
        return superClass;
    }

    public void setSuperClass(PsiClass superClass) {
        this.superClass = superClass;
    }

    public List<ParameterModel> getActionFields() {
        return actionFields;
    }

    public void setActionFields(List<ParameterModel> actionFields) {
        this.actionFields = actionFields;
    }

    public List<ParameterModel> getResultFileds() {
        return resultFileds;
    }

    public void setResultFileds(List<ParameterModel> resultFileds) {
        this.resultFileds = resultFileds;
    }

    public String getActionHandlerPkg() {
        return actionHandlerPkg;
    }

    public void setActionHandlerPkg(String actionHandlerPkg) {
        this.actionHandlerPkg = actionHandlerPkg;
    }

    public PsiClass getActionValidator() {
        return actionValidator;
    }

    public void setActionValidator(PsiClass actionValidator) {
        this.actionValidator = actionValidator;
    }

    public Module getHandlerModule() {
        return handlerModule;
    }

    public void setHandlerModule(Module handlerModule) {
        this.handlerModule = handlerModule;
    }

    public boolean isWithoutSecure() {
        return withoutSecure;
    }

    public void setWithoutSecure(boolean withoutSecure) {
        this.withoutSecure = withoutSecure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = IdUtils.capitalizeFirst(name);
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }
}
