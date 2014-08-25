package com.arcbees.plugin.idea.wizards.createaction;

import com.arcbees.plugin.idea.domain.ActionModel;
import com.arcbees.plugin.idea.domain.ParameterModel;
import com.arcbees.plugin.idea.icons.PluginIcons;
import com.arcbees.plugin.idea.utils.PackageUtilExt;
import com.arcbees.plugin.idea.wizards.BaseCreateClassAction;
import com.arcbees.plugin.template.create.action.CreateAction;
import com.arcbees.plugin.template.domain.ParameterOptions;
import com.arcbees.plugin.template.domain.action.ActionOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;

/**
 * Created by serg on 22.08.2014.
 */
public class CreateActionAction extends BaseCreateClassAction {

    private ActionModel actModel;
    private RenderedTemplate actionTemplate;
    private RenderedTemplate resultTemplate;
    private RenderedTemplate handlerTemplate;

    public CreateActionAction() {
        super(PluginIcons.GWTPAction_ICON_16x16);
    }

    public void actionPerformed(AnActionEvent e) {
        project = e.getProject();
        actModel = new ActionModel(project);
        CreateActionForm dialog = new CreateActionForm(actModel, e);
        if (!dialog.showAndGet()){
            return;
        }

        dialog.getData(actModel);

        new WriteCommandAction.Simple(project){

            @Override
            protected void run() throws Throwable {
                new Task.Backgroundable(actModel.getProject(), "Create Action", true) {
                    public void run(ProgressIndicator indicator) {
                        indicator.setFraction(0.0);
                        CreateActionAction.this.run(indicator);
                        indicator.setFraction(1.0);
                    }
                }.setCancelText("Cancel action Creation").queue();
            }
        }.execute();

    }

    private void run(ProgressIndicator indicator) {
        indicator.setText("Fetch templates");
        try {
            fetchTemplates();
        } catch (Exception e) {
            error("Could not fetch action templates: Error: " + e.toString());
            e.printStackTrace();
            return;
        }

        indicator.setFraction(.5);
        indicator.setText("Generating classes");
        createActionClasses();

        indicator.setText("Action class was generated");
    }

    private void createActionClasses() {
        PsiPackage pkg = PackageUtilExt.findLongestExistingPackage(actModel.getModule(), actModel.getPackageName());
        PsiClass actionPsiClass = createPsiClass(pkg, actionTemplate);
        navigateToClass(actionPsiClass);

        PsiClass resultPsiClass = createPsiClass(pkg, resultTemplate);
        navigateToClass(resultPsiClass);

        pkg = PackageUtilExt.findLongestExistingPackage(actModel.getModule(), actModel.getActionHandlerPkg());
        PsiClass handlerPsiClass = createPsiClass(pkg, handlerTemplate);
        navigateToClass(handlerPsiClass);
    }

    private void fetchTemplates() {
        ActionOptions actionOptions = new ActionOptions();

        actionOptions.setName(actModel.getName());
        actionOptions.setPackageName(actModel.getPackageName());
        actionOptions.setActionHandlerPkg(actModel.getActionHandlerPkg());
        actionOptions.setWithoutSecure(actModel.isWithoutSecure());

        for (ParameterModel parameterModel: actModel.getActionFields()){
            ParameterOptions parameterOptions = new ParameterOptions();

            parameterOptions.setName(parameterModel.getName());
            parameterOptions.setType(parameterModel.getType());

            actionOptions.getActionFields().add(parameterOptions);
        }

        for (ParameterModel parameterModel: actModel.getResultFileds()){
            ParameterOptions parameterOptions = new ParameterOptions();

            parameterOptions.setName(parameterModel.getName());
            parameterOptions.setType(parameterModel.getType());

            actionOptions.getResultFileds().add(parameterOptions);
        }

        CreateAction createAction = new CreateAction(actionOptions);
        try {
            createAction.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        actionTemplate = createAction.getCreated().getRequest();
        resultTemplate = createAction.getCreated().getResult();
        handlerTemplate = createAction.getCreated().getActionHandler();
    }

    private void error(String message) {
        Messages.showErrorDialog(message, "Error");
    }


}
