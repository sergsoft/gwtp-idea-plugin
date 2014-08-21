package com.arcbees.plugin.idea.domain;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serg on 20.08.2014.
 */
public class EventModel {

    private String name;

    private PsiPackage selectedPackageRoot;

    private Project project;

    private Module module;

    private List<ParameterModel> parameters = new ArrayList<ParameterModel>();

    public EventModel(Project project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PsiPackage getSelectedPackageRoot() {
        return selectedPackageRoot;
    }

    public void setSelectedPackageRoot(PsiPackage selectedPackageRoot) {
        this.selectedPackageRoot = selectedPackageRoot;
    }

    public Project getProject() {
        return project;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<ParameterModel> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterModel> parameters) {
        this.parameters = parameters;
    }
}
