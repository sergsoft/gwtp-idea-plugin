package com.arcbees.plugin.idea.wizards;

import com.arcbees.plugin.idea.domain.PsiClassModel;
import com.arcbees.plugin.idea.domain.PsiElementModel;
import com.arcbees.plugin.idea.domain.PsiPackageModel;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import javax.swing.*;

/**
 * Created by Serg on 20.08.2014.
 */
public abstract class BaseCreateClassAction extends AnAction {
    protected Project project;

    public BaseCreateClassAction(String text, String description, Icon icon) {
        super(text, description, icon);
    }

    public BaseCreateClassAction(Icon icon) {
        super(icon);
    }

    protected void navigateToClass(final PsiClass psiClass) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        psiClass.navigate(true);
                    }
                });
            }
        }, ModalityState.NON_MODAL);
    }

    protected PsiClass createPsiClass(final PsiPackage createInPsiPackage, RenderedTemplate renderedTemplate) {
        final String className = renderedTemplate.getNameAndNoExt();
        final String contents = renderedTemplate.getContents();

        final PsiPackageModel psiPackageModel = new PsiPackageModel();
        ApplicationManager.getApplication().runReadAction(new Runnable() {
            @Override
            public void run() {
                PsiDirectory[] directoriesInPackage = createInPsiPackage.getDirectories();
                PsiDirectory dir = directoriesInPackage[0];
                psiPackageModel.set(dir);
            }
        });

        final PsiElementModel elementModel = new PsiElementModel();
        ApplicationManager.getApplication().runReadAction(new Runnable() {
            @Override
            public void run() {
                PsiFile element = PsiFileFactory.getInstance(project).createFileFromText(
                        className, JavaFileType.INSTANCE, contents);
                elementModel.set(element);
            }
        });

        final PsiElementModel createdJavaFileModel = new PsiElementModel();
        ApplicationManager.getApplication().invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        PsiElement element = elementModel.get();
                        PsiDirectory dir = psiPackageModel.get();
                        PsiElement createdElement = dir.add(element);
                        // TODO fail
                        createdJavaFileModel.set(createdElement);
                    }
                });
            }
        }, ModalityState.NON_MODAL);

        final PsiClassModel psiClassModelModel = new PsiClassModel();
        ApplicationManager.getApplication().invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        PsiClass[] createdClasses = createdJavaFileModel.getJavaFile().getClasses();
                        psiClassModelModel.set(createdClasses[0]);
                        CodeStyleManager.getInstance(project).reformat(createdClasses[0]);
                    }
                });
            }
        }, ModalityState.NON_MODAL);

        ApplicationManager.getApplication().invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        JavaCodeStyleManager.getInstance(project).optimizeImports(psiClassModelModel.get().getContainingFile());
                    }
                });
            }
        }, ModalityState.NON_MODAL);

        return psiClassModelModel.get();
    }
}
