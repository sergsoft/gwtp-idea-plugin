package com.arcbees.plugin.idea.wizards.createevent;

import com.arcbees.plugin.idea.domain.EventModel;
import com.arcbees.plugin.idea.domain.ParameterModel;
import com.arcbees.plugin.idea.icons.PluginIcons;
import com.arcbees.plugin.idea.wizards.BaseCreateClassAction;
import com.arcbees.plugin.template.create.event.CreateEvent;
import com.arcbees.plugin.template.domain.ParameterOptions;
import com.arcbees.plugin.template.domain.event.CreatedEvent;
import com.arcbees.plugin.template.domain.event.EventOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;

/**
 * Created by Serg on 20.08.2014.
 */
public class CreateEventAction extends BaseCreateClassAction {

    private EventModel eventModel;
    private PsiClass createdEventPsiClass;

    private RenderedTemplate createdEventTemplate;

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

        new WriteCommandAction.Simple(project){

            @Override
            protected void run() throws Throwable {
                new Task.Backgroundable(eventModel.getProject(), "Create Event", true) {
                    public void run(ProgressIndicator indicator) {
                        indicator.setFraction(0.0);
                        CreateEventAction.this.run(indicator);
                        indicator.setFraction(1.0);
                    }
                }.setCancelText("Cancel Event Creation").queue();
            }
        }.execute();

    }

    private void run(ProgressIndicator indicator) {
        indicator.setText("Fetch templates");
        try {
            fetchTemplates();
        } catch (Exception e) {
            error("Could not fetch event templates: Error: " + e.toString());
            e.printStackTrace();
            return;
        }

        indicator.setFraction(.5);
        indicator.setText("Generating classes");
        createEventClass();

        indicator.setText("Event class was generated");
    }

    private void fetchTemplates() throws Exception {
        EventOptions eventOptions = new EventOptions();

        eventOptions.setName(eventModel.getName());
        eventOptions.setPackageName(eventModel.getSelectedPackageRoot().getQualifiedName());

        for (ParameterModel parameterModel: eventModel.getParameters()){
            ParameterOptions parameterOptions = new ParameterOptions();

            parameterOptions.setName(parameterModel.getName());
            parameterOptions.setType(parameterModel.getType());

            eventOptions.getParameters().add(parameterOptions);
        }

        CreatedEvent createdEvent = CreateEvent.run(eventOptions);
        createdEventTemplate = createdEvent.getEvent();
    }

    private void createEventClass() {
        createdEventPsiClass = createPsiClass(eventModel.getSelectedPackageRoot(), createdEventTemplate);
        navigateToClass(createdEventPsiClass);
    }

    private void warn(String message) {
        Messages.showWarningDialog(message, "Warning");
    }

    private void error(String message) {
        Messages.showErrorDialog(message, "Error");
    }
}
