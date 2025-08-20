// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package io.github.projecthsf.go.playground.runConfigure;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PlaygroundSettingsEditor extends SettingsEditor<PlaygroundRunConfiguration> {

  private final JPanel myPanel;
  private final JBTextField scriptPathField;

  public PlaygroundSettingsEditor() {
    scriptPathField = new JBTextField();
    myPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent("Script", scriptPathField)
        .getPanel();
  }

  @Override
  protected void resetEditorFrom(PlaygroundRunConfiguration demoRunConfiguration) {
    scriptPathField.setText(demoRunConfiguration.getScriptName());
  }

  @Override
  protected void applyEditorTo(@NotNull PlaygroundRunConfiguration demoRunConfiguration) {
    demoRunConfiguration.setScriptName(scriptPathField.getText());
  }

  @NotNull
  @Override
  protected JComponent createEditor() {
    return myPanel;
  }

}
