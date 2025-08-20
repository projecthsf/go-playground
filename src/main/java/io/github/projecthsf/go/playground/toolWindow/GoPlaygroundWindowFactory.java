package io.github.projecthsf.go.playground.toolWindow;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import io.github.projecthsf.go.playground.toolWindow.controller.GoPlaygroundWindowController;
import org.jetbrains.annotations.NotNull;

public class GoPlaygroundWindowFactory implements ToolWindowFactory, DumbAware {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        Content order = ContentFactory.getInstance().createContent(new GoPlaygroundWindowController(toolWindow), "GoPlayground", false);
        toolWindow.getContentManager().addContent(order);
    }
}