package com.arcbees.plugin.idea.wizards.createevent;

import com.arcbees.plugin.idea.domain.EventModel;
import com.arcbees.plugin.idea.domain.PsiClassModel;
import com.arcbees.plugin.idea.domain.PsiElementModel;
import com.arcbees.plugin.idea.domain.PsiPackageModel;
import com.arcbees.plugin.idea.icons.PluginIcons;
import com.arcbees.plugin.idea.wizards.BaseCreateClassAction;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.logging.Logger;

/**
 * Created by Serg on 20.08.2014.
 */
public class CreateEventAction extends BaseCreateClassAction {

    private final static Logger logger = Logger.getLogger(CreateEventAction.class.getName());

    private EventModel eventModel;
    private PsiClass createdEventPsiClass;

    public CreateEventAction() {
        super("Create event", "Create GWTP event", PluginIcons.GWTPEvent_ICON_16x16);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        project = anActionEvent.getProject();
        eventModel = new EventModel(project);
        CreateEventForm dialog = new CreateEventForm(eventModel, anActionEvent);
        if (!dialog.showAndGet()){
            return;
        }

        dialog.getData(eventModel);

        new Task.Backgroundable(eventModel.getProject(), "Create Event", true) {
            public void run(ProgressIndicator indicator) {
                indicator.setFraction(0.0);
                CreateEventAction.this.run(indicator);
                indicator.setFraction(1.0);
            }
        }.setCancelText("Cancel Event Creation").queue();
    }

    private void run(ProgressIndicator indicator) {
        indicator.setText("Fetch templates");
        fetchTemplates();

        indicator.setFraction(.3);
        indicator.setText("Generating classes");
        createEventClass();
    }

    private void fetchTemplates() {

    }

    private void createEventClass() {
        RenderedTemplate renderedTemplate = null;
        createdEventPsiClass = createPsiClass(eventModel.getSelectedPackageRoot(), renderedTemplate);

        navigateToClass(createdEventPsiClass);
    }

}
