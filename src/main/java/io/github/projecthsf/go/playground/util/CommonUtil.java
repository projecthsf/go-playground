package io.github.projecthsf.go.playground.util;

import com.goide.GoFileType;
import com.goide.GoLanguage;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.Nullable;

public class CommonUtil {

    public static EditorEx getEditorEx(@Nullable String text, @Nullable Boolean readOnly) {
        LightVirtualFile virtualFile = new LightVirtualFile("GoPlayground.go", GoLanguage.INSTANCE, text);
        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
        document = document != null ? document : EditorFactory.getInstance().createDocument(text);

        Editor editor = EditorFactory.getInstance().createEditor(document);
        if (Boolean.TRUE.equals(readOnly)) {
            editor = EditorFactory.getInstance().createViewer(document);
        }
        EditorEx editorEx = (EditorEx) editor;
        editorEx.setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(null, GoFileType.INSTANCE));
        return editorEx;
    }
}
