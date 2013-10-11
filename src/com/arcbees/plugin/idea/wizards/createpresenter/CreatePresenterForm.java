package com.arcbees.plugin.idea.wizards.createpresenter;

import com.arcbees.plugin.idea.dialogs.ContentSlotDialog;
import com.arcbees.plugin.idea.domain.PresenterConfigModel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreatePresenterForm extends DialogWrapper {
    private final PresenterConfigModel presenterConfigModel;
    private final AnActionEvent sourceEvent;

    private JPanel contentPane;
    private JTextField name;
    private JTextField packageName;
    private JRadioButton radioNestedPresenter;
    private JRadioButton radioPresenterWidget;
    private JRadioButton radioPopupPresenter;
    private JPanel buttonPanel;
    private JPanel chooseTypesPanel;
    private JTabbedPane tabbedPanel;
    private JRadioButton radioContentRoot;
    private JRadioButton radioContentSlot;
    private JTextField contentSlot;
    private JButton selectContentSlot;
    private JRadioButton radioContentRootLayout;
    private JCheckBox usePlace;
    private JTextField nameToken;
    private JCheckBox useCrawlable;
    private JCheckBox useCodesplit;
    private JCheckBox useAddUihandlers;
    private JCheckBox useAddOnbind;
    private JCheckBox useManualReveal;
    private JCheckBox usePrepareFromRequest;
    private JCheckBox useSingleton;
    private JCheckBox useSingleton2;
    private JCheckBox useOverrideDefaultPopup;
    private JCheckBox useAddOnhide;
    private JCheckBox useAddOnreset;
    private JCheckBox useAddOnunbind;
    private JLabel lblQuerystring;

    public CreatePresenterForm(Project project, AnActionEvent sourceEvent) {
        super(project);
        init();

        this.sourceEvent = sourceEvent;

        setTitle("Create GWTP Presenter");

        // model object to transfer vars to the ide-templates
        presenterConfigModel = new PresenterConfigModel(project);

        initHandlers();
        setDefaults();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    private void initHandlers() {
        initRadioHandlers();
        initTabFoldersHandlers();
        initContentEventHandlers();
        initPlaceHandlers();
        initButtonHandlers();
    }

    private void setDefaults() {
        nameToken.setEnabled(false);
        useCrawlable.setEnabled(false);
        nameToken.grabFocus();
    }

    private void initButtonHandlers() {
        selectContentSlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showContentSlotDialog();
            }
        });
    }

    private void initContentEventHandlers() {
        radioContentRoot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radioContentSlot.setSelected(false);
                radioContentRoot.setSelected(true);
                radioContentRootLayout.setSelected(false);
                contentSlot.setEnabled(false);
                selectContentSlot.setEnabled(false);

                presenterConfigModel.setRevealInRoot(true);
                presenterConfigModel.setRevealInRootLayout(false);
                presenterConfigModel.setRevealInSlot(false);
            }
        });

        radioContentRootLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radioContentSlot.setSelected(false);
                radioContentRoot.setSelected(false);
                radioContentRootLayout.setSelected(true);
                contentSlot.setEnabled(false);
                selectContentSlot.setEnabled(false);

                presenterConfigModel.setRevealInRoot(false);
                presenterConfigModel.setRevealInRootLayout(true);
                presenterConfigModel.setRevealInSlot(false);
            }
        });

        radioContentSlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radioContentSlot.setSelected(true);
                radioContentRoot.setSelected(false);
                radioContentRootLayout.setSelected(false);
                contentSlot.setEnabled(true);
                selectContentSlot.setEnabled(true);

                presenterConfigModel.setRevealInRoot(false);
                presenterConfigModel.setRevealInRootLayout(false);
                presenterConfigModel.setRevealInSlot(true);
            }
        });
    }

    private void initPlaceHandlers() {
        usePlace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = usePlace.isSelected();

                nameToken.setEnabled(selected);
                useCrawlable.setEnabled(selected);

                if (selected) {
                    nameToken.grabFocus();
                } else {
                    nameToken.setText("");
                }
            }
        });
    }

    private void initRadioHandlers() {
        radioNestedPresenter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = radioNestedPresenter.isSelected();
                if (selected) {
                    setSelectedIndex(0);
                }
            }
        });

        radioPresenterWidget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = radioPresenterWidget.isSelected();
                if (selected) {
                    setSelectedIndex(1);
                }
            }
        });

        radioPopupPresenter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = radioPopupPresenter.isSelected();
                if (selected) {
                    setSelectedIndex(2);
                }
            }
        });
    }

    private void setSelectedIndex(Integer selectedIndex) {
        tabbedPanel.setSelectedIndex(selectedIndex);
        setPresenterType(selectedIndex);
    }

    private void initTabFoldersHandlers() {
        tabbedPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPanel.getSelectedIndex();

                setPresenterType(selectedIndex);

                if (selectedIndex == 0) {
                    radioNestedPresenter.setSelected(true);
                    radioPresenterWidget.setSelected(false);
                    radioPopupPresenter.setSelected(false);
                } else if (selectedIndex == 1) {
                    radioNestedPresenter.setSelected(false);
                    radioPresenterWidget.setSelected(true);
                    radioPopupPresenter.setSelected(false);
                } else if (selectedIndex == 2) {
                    radioNestedPresenter.setSelected(false);
                    radioPresenterWidget.setSelected(false);
                    radioPopupPresenter.setSelected(true);
                }
            }
        });
    }

    private void setPresenterType(int selectedIndex) {
        if (selectedIndex == 0) {
            presenterConfigModel.setNestedPresenter(true);
            presenterConfigModel.setPresenterWidget(false);
            presenterConfigModel.setPopupPresenter(false);

            if (lblQuerystring != null) {
                lblQuerystring.setVisible(true);
            }

            if (usePrepareFromRequest != null) {
                usePrepareFromRequest.setVisible(true);
            }
        } else if (selectedIndex == 1) {
            presenterConfigModel.setNestedPresenter(false);
            presenterConfigModel.setPresenterWidget(true);
            presenterConfigModel.setPopupPresenter(false);

            if (lblQuerystring != null) {
                lblQuerystring.setVisible(false);
            }

            if (usePrepareFromRequest != null) {
                usePrepareFromRequest.setVisible(false);
            }
        } else if (selectedIndex == 2) {
            presenterConfigModel.setNestedPresenter(false);
            presenterConfigModel.setPresenterWidget(false);
            presenterConfigModel.setPopupPresenter(true);

            if (lblQuerystring != null) {
                lblQuerystring.setVisible(false);
            }

            if (usePrepareFromRequest != null) {
                usePrepareFromRequest.setVisible(false);
            }
        }
    }

    private void showContentSlotDialog() {
        Project project = presenterConfigModel.getProject();

        ContentSlotDialog dialog = new ContentSlotDialog(project, true, sourceEvent);
        dialog.show();

        String contentSlotSelection = dialog.getContentSlot();
        presenterConfigModel.setContentSlot(contentSlotSelection);
        contentSlot.setText(contentSlotSelection);
    }

    public void setData(PresenterConfigModel data) {
        name.setText(data.getName());
        packageName.setText(data.getPackageName());
        contentSlot.setText(data.getContentSlot());
        usePlace.setSelected(data.isUsePlace());
        nameToken.setText(data.getNameToken());
        useCrawlable.setSelected(data.isUseCrawlable());
        useCodesplit.setSelected(data.isUseCodesplit());
        useSingleton.setSelected(data.isUseSingleton());
        useSingleton2.setSelected(data.isUseSingleton());
        useOverrideDefaultPopup.setSelected(data.isUseOverrideDefaultPopup());
        useAddUihandlers.setSelected(data.isSeAddUihandlers());
        useAddOnbind.setSelected(data.isSeAddOnbind());
        useAddOnhide.setSelected(data.isUseAddOnhide());
        useAddOnreset.setSelected(data.isUseAddOnreset());
        useAddOnunbind.setSelected(data.isUseAddOnunbind());
        useManualReveal.setSelected(data.getUseManualReveal());
        usePrepareFromRequest.setSelected(data.getUsePrepareFromRequest());
    }

    public void getData(PresenterConfigModel data) {
        data.setName(name.getText());
        data.setPackageName(packageName.getText());
        data.setContentSlot(contentSlot.getText());
        data.setUsePlace(usePlace.isSelected());
        data.setNameToken(nameToken.getText());
        data.setUseCrawlable(useCrawlable.isSelected());
        data.setUseCodesplit(useCodesplit.isSelected());
        data.setUseSingleton(useSingleton.isSelected());
        data.setUseSingleton(useSingleton2.isSelected());
        data.setUseOverrideDefaultPopup(useOverrideDefaultPopup.isSelected());
        data.setSeAddUihandlers(useAddUihandlers.isSelected());
        data.setSeAddOnbind(useAddOnbind.isSelected());
        data.setUseAddOnhide(useAddOnhide.isSelected());
        data.setUseAddOnreset(useAddOnreset.isSelected());
        data.setUseAddOnunbind(useAddOnunbind.isSelected());
        data.setUseManualReveal(useManualReveal.isSelected());
        data.setUsePrepareFromRequest(usePrepareFromRequest.isSelected());
    }

    public boolean isModified(PresenterConfigModel data) {
        if (name.getText() != null ? !name.getText().equals(data.getName()) : data.getName() != null) return true;
        if (packageName.getText() != null ? !packageName.getText().equals(data.getPackageName()) : data.getPackageName() != null)
            return true;
        if (contentSlot.getText() != null ? !contentSlot.getText().equals(data.getContentSlot()) : data.getContentSlot() != null)
            return true;
        if (usePlace.isSelected() != data.isUsePlace()) return true;
        if (nameToken.getText() != null ? !nameToken.getText().equals(data.getNameToken()) : data.getNameToken() != null)
            return true;
        if (useCrawlable.isSelected() != data.isUseCrawlable()) return true;
        if (useCodesplit.isSelected() != data.isUseCodesplit()) return true;
        if (useSingleton.isSelected() != data.isUseSingleton()) return true;
        if (useSingleton2.isSelected() != data.isUseSingleton()) return true;
        if (useOverrideDefaultPopup.isSelected() != data.isUseOverrideDefaultPopup()) return true;
        if (useAddUihandlers.isSelected() != data.isSeAddUihandlers()) return true;
        if (useAddOnbind.isSelected() != data.isSeAddOnbind()) return true;
        if (useAddOnhide.isSelected() != data.isUseAddOnhide()) return true;
        if (useAddOnreset.isSelected() != data.isUseAddOnreset()) return true;
        if (useAddOnunbind.isSelected() != data.isUseAddOnunbind()) return true;
        if (useManualReveal.isSelected() != data.getUseManualReveal()) return true;
        if (usePrepareFromRequest.isSelected() != data.getUsePrepareFromRequest()) return true;
        return false;
    }
}
