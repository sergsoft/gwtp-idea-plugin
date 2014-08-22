package com.arcbees.plugin.idea.wizards.createevent;

import com.arcbees.plugin.idea.dialogs.ParameterEditDialog;
import com.arcbees.plugin.idea.domain.EventModel;
import com.arcbees.plugin.idea.domain.ParameterModel;
import com.arcbees.plugin.idea.wizards.ParametersTableModel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CreateEventForm extends DialogWrapper {

    private JPanel contentPane;
    private JTextField edName;
    private JTable parameters;
    private JButton buAddParameter;
    private JButton buDelParameter;

    private EventModel eventModel;
    private AnActionEvent anActionEvent;
    private ParametersTableModel parametersTableModel = new ParametersTableModel();

    public CreateEventForm(EventModel eventModel, AnActionEvent anActionEvent) {
        super(eventModel.getProject());

        this.eventModel = eventModel;
        this.anActionEvent = anActionEvent;

        init();

        setTitle("Create GWTP Event");

        initHandlers();
        setDefaults();
    }

    private void setDefaults() {
        eventModel.setSelectedPackageRoot(getSelectedPackageRoot());
        edName.requestFocusInWindow();

        parameters.setModel(parametersTableModel);
    }

    private void initHandlers() {
        initButtonHandlers();
    }

    private void initButtonHandlers() {
        buAddParameter.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ParameterEditDialog dialog = new ParameterEditDialog(eventModel.getProject(), false);
                if (dialog.showAndGet()){
                    ParameterModel parameterModel = new ParameterModel();
                    dialog.getData(parameterModel);
                    eventModel.getParameters().add(parameterModel);
                    refreshParams();
                }
            }
        });
        buDelParameter.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (parameters.getSelectedRow() >= 0) {
                    eventModel.getParameters().remove(parameters.getSelectedRow());
                    refreshParams();
                }
            }
        });
    }

    private void refreshParams() {
        parametersTableModel.setParameterModels(eventModel.getParameters());
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    public PsiPackage getSelectedPackageRoot() {
        PsiElement e = anActionEvent.getData(LangDataKeys.PSI_ELEMENT);

        PsiPackage selectedPackage = null;
        if (e instanceof PsiClass) {
            PsiClass clazz = (PsiClass) e;
            PsiJavaFile javaFile = (PsiJavaFile) clazz.getContainingFile();
            selectedPackage = JavaPsiFacade.getInstance(eventModel.getProject()).findPackage(javaFile.getPackageName());

        } else if (e instanceof PsiDirectory) {
            selectedPackage = JavaDirectoryService.getInstance().getPackage((PsiDirectory) e);
        }

        Module module = ModuleUtil.findModuleForPsiElement(e);

        eventModel.setModule(module);

        return selectedPackage;
    }

    public void getData(EventModel data) {
        data.setName(edName.getText());
    }

    public boolean isModified(EventModel data) {
        if (edName.getText() != null ? !edName.getText().equals(data.getName()) : data.getName() != null) return true;
        return false;
    }
}
