package io.github.projecthsf.go.playground.toolWindow.controller;

import com.goide.sdk.GoSdk;
import com.goide.sdk.GoSdkUtil;
import com.goide.sdk.combobox.GoSdkList;
import com.intellij.icons.AllIcons;
import com.intellij.ide.HelpTooltip;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import io.github.projecthsf.go.playground.forms.GoPlaygroundWindowForm;
import io.github.projecthsf.go.playground.util.ExecuteUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class GoPlaygroundWindowController extends JBPanel {
    @NotNull ToolWindow toolWindow;
    GoPlaygroundWindowForm form = new GoPlaygroundWindowForm();
    ComboBox<String> versions = new ComboBox<>();
    public GoPlaygroundWindowController(@NotNull ToolWindow toolWindow) {
        form.addListener(toolWindow.getProject());
        this.toolWindow = toolWindow;
        setLayout(new BorderLayout());
        add(form, BorderLayout.NORTH);
        add(getControlButtons(), BorderLayout.CENTER);

        refreshGoVersions();
    }

    private JPanel getControlButtons() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> toolWindow.hide());

        JButton runBtn = new JButton("Run", AllIcons.Actions.Execute);
        runBtn.addActionListener(new ExecuteActionListener(this));

        JButton refreshBtn = new JButton(AllIcons.Actions.Refresh);
        refreshBtn.addActionListener(e -> refreshGoVersions());
        refreshBtn.setPreferredSize(new Dimension(20, 20));
        JLabel label = new JBLabel("Go", AllIcons.General.Information, 0);
        new HelpTooltip().setTitle("Go Versions").setDescription("Check Settings > Go > GOROOT").installOn(label);
        panel.add(label);
        panel.add(versions);
        panel.add(refreshBtn);
        panel.add(closeBtn);
        panel.add(runBtn);



        return panel;
    }

    void refreshGoVersions() {
        Collection<VirtualFile> sdks = GoSdkUtil.getGoPathBins(toolWindow.getProject(), null, false);
        if (sdks.isEmpty()) {
            return;
        }

        VirtualFile goSdk = sdks.iterator().next();
        versions.removeAllItems();
        GoSdkList.getInstance().reloadSdks(toolWindow.getProject(), (consumers) -> {
            for (GoSdk sdk: consumers) {
                versions.addItem(sdk.getVersion());

                if (sdk.getSdkRoot() == null) {
                    continue;
                }

                if (goSdk.getPath().equals(sdk.getSdkRoot().getPath() + "/bin")) {
                    versions.setSelectedItem(sdk.getVersion());
                }
            }
        });
    }

    static class ExecuteActionListener implements ActionListener {
        private final String FILE_NAME = "playground.go";
        private GoPlaygroundWindowController controller;
        public ExecuteActionListener(GoPlaygroundWindowController controller) {
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Collection<Module> modules = GoSdkUtil.getGoModules(controller.toolWindow.getProject());
            if (modules.isEmpty()) {
                Messages.showErrorDialog("GOROOT is not setting, pls check Settings > Go > GOROOT", "Error");
                return;
            }

            VirtualFile goRoot = GoSdkUtil.getDefaultGoPath();
            if (goRoot == null) {
                Messages.showErrorDialog("GOPATH is not setting, pls check Settings > Go > GOPATH", "Error");
                return;
            }

            Runnable run = () -> {
                PsiDirectory directory = PsiManager.getInstance(controller.toolWindow.getProject()).findDirectory(goRoot);
                deletePlaygroundFileIfExisted(goRoot);

                PsiFile file = PsiFileFactory.getInstance(controller.toolWindow.getProject()).createFileFromText(PlainTextLanguage.INSTANCE, controller.form.getGoPlaygroundCode());
                directory.copyFileFrom(FILE_NAME, file);
                ExecuteUtil.execute(controller.toolWindow.getProject(), String.format("%s/go%s/bin/go run %s/%s",  goRoot.getPath(), controller.versions.getSelectedItem(), goRoot.getPath(), FILE_NAME), "Playground");
            };

            ApplicationManager.getApplication().runWriteAction(run);


        }

        private void deletePlaygroundFileIfExisted(VirtualFile goRoot) {
            VirtualFile playgroundFile = goRoot.findChild(FILE_NAME);
            if (playgroundFile == null) {
                return;
            }

            try {
                playgroundFile.delete(null);
            } catch (Exception e) {
                System.out.printf("=========== delete file: %s failed: %s", playgroundFile.getName(), e.getMessage());
            }
        }
    }
}
