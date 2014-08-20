package com.arcbees.plugin.idea.wizards.createevent;

import com.arcbees.plugin.idea.domain.EventModel;
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
import com.intellij.ui.GuiUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateEventForm extends DialogWrapper {
    private JPanel contentPane;
    private JTextField edName;

    private EventModel eventModel;
    private AnActionEvent anActionEvent;

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
    }

    private void initHandlers() {
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    public void getData(EventModel eventModel) {
        eventModel.setName(edName.getText());
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
}
