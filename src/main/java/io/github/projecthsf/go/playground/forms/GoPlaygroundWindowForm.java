package io.github.projecthsf.go.playground.forms;

import com.goide.sdk.GoSdkUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.EditorCoreUtil;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.EditorUIUtil;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.fileEditor.impl.EditorEmptyTextPainter;
import com.intellij.openapi.fileEditor.impl.EditorsSplitters;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.FormBuilder;
import io.github.projecthsf.go.playground.util.CommonUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;


public class GoPlaygroundWindowForm extends JPanel {
    private EditorEx goPlaygroundCode;
    public GoPlaygroundWindowForm() {
        setLayout(new BorderLayout());
        add(getCenterPanel(), BorderLayout.CENTER);

    }


    private JPanel getCenterPanel() {
        goPlaygroundCode = CommonUtil.getEditorEx("// You can edit this code!\n" +
                "// Click here and start typing.\n" +
                "package main\n\n" +
                "import \"fmt\"\n\n" +
                "func main() {\n" +
                "\tfmt.Println(\"Hello, 世界\")\n" +
                "}", false);

        JPanel editorPanel = new JPanel(new BorderLayout());
        editorPanel.add(goPlaygroundCode.getComponent(), BorderLayout.CENTER);
        editorPanel.setPreferredSize(new Dimension(100, 400));


        return FormBuilder.createFormBuilder()
                .addComponent(editorPanel)
                .getPanel();
    }

    public void addListener(@NotNull Project project) {
        PsiDirectory directory = PsiManager.getInstance(project).findDirectory(GoSdkUtil.getDefaultGoPath());
        goPlaygroundCode.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                Runnable run = () -> {
                    String fileName = DigestUtils.md2Hex(event.getDocument().getText());
                    PsiFile file = PsiFileFactory.getInstance(project).createFileFromText(PlainTextLanguage.INSTANCE, event.getDocument().getText());
                    directory.copyFileFrom(String.format("%s.go", fileName), file);
                };
                ApplicationManager.getApplication().runWriteAction(run);
            }
        });
    }

    public String getGoPlaygroundCode() {
        return goPlaygroundCode.getDocument().getText();
    }
}
