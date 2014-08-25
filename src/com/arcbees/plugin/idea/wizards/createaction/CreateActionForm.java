package com.arcbees.plugin.idea.wizards.createaction;

import com.arcbees.plugin.idea.dialogs.ParameterEditDialog;
import com.arcbees.plugin.idea.domain.ActionModel;
import com.arcbees.plugin.idea.domain.ParameterModel;
import com.arcbees.plugin.idea.utils.PackageUtilExt;
import com.arcbees.plugin.idea.wizards.ParametersTableModel;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CreateActionForm extends DialogWrapper {

    private JPanel contentPane;
    private JTextField edName;
    private JTextField edClientPackage;
    private JButton buSelClientPkg;
    private JTextField edServerPackage;
    private JButton buSelServerPkg;
    private JTable tblActionFields;
    private JButton buAddActionField;
    private JButton buDelActionField;
    private JTable tblResultFlieds;
    private JButton buAddResField;
    private JButton buDelResField;
    private JCheckBox cbWithoutSecure;

    private ActionModel actionModel;
    private AnActionEvent anActionEvent;
    private ParametersTableModel actionParameterModel = new ParametersTableModel();
    private ParametersTableModel resParameterModel = new ParametersTableModel();

    public CreateActionForm(ActionModel actionModel, AnActionEvent anActionEvent) {
        super(actionModel.getProject());

        this.actionModel = actionModel;
        this.anActionEvent = anActionEvent;

        init();

        setTitle("Create GWTP Action");

        initHandlers();
        setDefaults();
    }

    private void setDefaults() {
        tblActionFields.setModel(actionParameterModel);
        tblResultFlieds.setModel(resParameterModel);
        edClientPackage.setText(PackageUtilExt.getClientPackage(getSelectedPackageRoot()));
        edServerPackage.setText(PackageUtilExt.getServerPackage(getSelectedPackageRoot()));
    }

    private void initHandlers() {
        initButtons();
    }

    private void initButtons() {
        buSelClientPkg.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PackageChooserDialog dialog = new PackageChooserDialog("Select a package", actionModel.getProject());
                dialog.selectPackage(PackageUtilExt.getClientPackage(getSelectedPackageRoot()));
                if (dialog.showAndGet()){
                    edClientPackage.setText(dialog.getSelectedPackage().getQualifiedName());
                }
            }
        });
        buSelServerPkg.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PackageChooserDialog dialog = new PackageChooserDialog("Select a package", actionModel.getProject());
                dialog.selectPackage(PackageUtilExt.getServerPackage(getSelectedPackageRoot()));
                if (dialog.showAndGet()){
                    edServerPackage.setText(dialog.getSelectedPackage().getQualifiedName());
                }
            }
        });

        // Action fields
        buAddActionField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ParameterEditDialog dialog = new ParameterEditDialog(actionModel.getProject(), false);
                if (dialog.showAndGet()){
                    ParameterModel parameterModel = new ParameterModel();
                    dialog.getData(parameterModel);

                    actionModel.getActionFields().add(parameterModel);
                    refreshActionFields();
                }
            }
        });
        buDelActionField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tblActionFields.getSelectedRow() >= 0) {
                    actionModel.getActionFields().remove(tblActionFields.getSelectedRow());
                    refreshActionFields();
                }
            }
        });

        //Result fields
        buAddResField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ParameterEditDialog dialog = new ParameterEditDialog(actionModel.getProject(), false);
                if (dialog.showAndGet()){
                    ParameterModel parameterModel = new ParameterModel();
                    dialog.getData(parameterModel);

                    actionModel.getResultFileds().add(parameterModel);
                    refreshResultFields();
                }
            }
        });
        buDelResField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tblResultFlieds.getSelectedRow() >= 0) {
                    actionModel.getResultFileds().remove(tblResultFlieds.getSelectedRow());
                    refreshResultFields();
                }
            }
        });
    }

    private void refreshActionFields() {
        actionParameterModel.setParameterModels(actionModel.getActionFields());
    }

    private void refreshResultFields() {
        resParameterModel.setParameterModels(actionModel.getResultFileds());
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        contentPane.setPreferredSize(new Dimension(800, 500));
        return contentPane;
    }

    public void getData(ActionModel data) {
        data.setName(edName.getText());
        data.setWithoutSecure(cbWithoutSecure.isSelected());
        data.setPackageName(edClientPackage.getText());
        data.setActionHandlerPkg(edServerPackage.getText());
    }

    public boolean isModified(ActionModel data) {
        if (edName.getText() != null ? !edName.getText().equals(data.getName()) : data.getName() != null) return true;
        if (cbWithoutSecure.isSelected() != data.isWithoutSecure()) return true;
        return false;
    }

    public PsiPackage getSelectedPackageRoot() {
        PsiElement e = anActionEvent.getData(LangDataKeys.PSI_ELEMENT);

        PsiPackage selectedPackage = null;
        if (e instanceof PsiClass) {
            PsiClass clazz = (PsiClass) e;
            PsiJavaFile javaFile = (PsiJavaFile) clazz.getContainingFile();
            selectedPackage = JavaPsiFacade.getInstance(actionModel.getProject()).findPackage(javaFile.getPackageName());

        } else if (e instanceof PsiDirectory) {
            selectedPackage = JavaDirectoryService.getInstance().getPackage((PsiDirectory) e);
        }

        Module module = ModuleUtil.findModuleForPsiElement(e);

        actionModel.setModule(module);

        return selectedPackage;
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (edName.getText().equals(""))
            return new ValidationInfo("Name of action should be entered", edName);

        if (edClientPackage.getText().equals(""))
            return new ValidationInfo("Client package should be selected", edClientPackage);

        if (edServerPackage.getText().equals(""))
            return new ValidationInfo("Server package should be selected", edServerPackage);

        return null;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return edName;
    }
}
