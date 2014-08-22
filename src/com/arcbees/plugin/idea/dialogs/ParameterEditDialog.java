package com.arcbees.plugin.idea.dialogs;

import com.arcbees.plugin.idea.domain.ParameterModel;
import com.intellij.ide.util.ClassFilter;
import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.impl.file.PsiPackageImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PackageScope;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ParameterEditDialog extends DialogWrapper {

    private JPanel contentPane;
    private JTextField edName;
    private JTextField edType;
    private JButton buSelType;
    @Nullable
    private Project project;

    public ParameterEditDialog(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        this.project = project;

        init();
        setTitle("Add parameter");
        setSize(500, 600);

        addHandlers();
    }

    private void addHandlers() {
        buSelType.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreeClassChooser classChooser = TreeClassChooserFactory.getInstance(project).createNoInnerClassesScopeChooser("Select parameter type", getGlobalScope(), getClassFilter(), null);
                classChooser.showDialog();
                if (classChooser.getSelected() != null){
                    edType.setText(classChooser.getSelected().getName());
                }
            }
        });
    }

    private GlobalSearchScope getGlobalScope() {
        Module
        return new PackageScope(new PsiPackageImpl());
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    public void setData(ParameterModel data) {
        edName.setText(data.getName());
        edType.setText(data.getType());
    }

    public void getData(ParameterModel data) {
        data.setName(edName.getText());
        data.setType(edType.getText());
    }

    public boolean isModified(ParameterModel data) {
        if (edName.getText() != null ? !edName.getText().equals(data.getName()) : data.getName() != null) return true;
        if (edType.getText() != null ? !edType.getText().equals(data.getType()) : data.getType() != null) return true;
        return false;
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (edName.getText() == null || edName.getText().length() < 1)
            return new ValidationInfo("You should enter parameter name", edName);
        if (edType.getText() == null || edType.getText().length() < 1)
            return new ValidationInfo("You should enter parameter type", edName);

        return null;
    }

    public ClassFilter getClassFilter() {
        return ClassFilter.ALL;
    }
}
