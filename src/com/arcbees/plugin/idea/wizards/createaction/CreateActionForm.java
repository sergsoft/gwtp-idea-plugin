package com.arcbees.plugin.idea.wizards.createaction;

import com.arcbees.plugin.idea.dialogs.ParameterEditDialog;
import com.arcbees.plugin.idea.domain.ActionModel;
import com.arcbees.plugin.idea.domain.ParameterModel;
import com.arcbees.plugin.idea.wizards.ParametersTableModel;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
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
    }

    private void initHandlers() {
        initButtons();
    }

    private void initButtons() {
        buSelClientPkg.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PackageChooserDialog dialog = new PackageChooserDialog("Select a package", actionModel.getProject());
                dialog.selectPackage("*.client");
                if (dialog.showAndGet()){
                    actionModel.setPackageName(dialog.getSelectedPackage());
                    edClientPackage.setText(dialog.getSelectedPackage().getText());
                }
            }
        });
        buSelServerPkg.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PackageChooserDialog dialog = new PackageChooserDialog("Select a package", actionModel.getProject());
                dialog.selectPackage("*.server");
                if (dialog.showAndGet()){
                    actionModel.setActionHandlerPkg(dialog.getSelectedPackage());
                    edServerPackage.setText(dialog.getSelectedPackage().getText());
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
        return contentPane;
    }

    public void setData(ActionModel data) {
        edName.setText(data.getName());
        cbWithoutSecure.setSelected(data.isWithoutSecure());
    }

    public void getData(ActionModel data) {
        data.setName(edName.getText());
        data.setWithoutSecure(cbWithoutSecure.isSelected());
    }

    public boolean isModified(ActionModel data) {
        if (edName.getText() != null ? !edName.getText().equals(data.getName()) : data.getName() != null) return true;
        if (cbWithoutSecure.isSelected() != data.isWithoutSecure()) return true;
        return false;
    }
}
