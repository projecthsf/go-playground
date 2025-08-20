// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package io.github.projecthsf.go.playground.runConfigure;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.NotNullLazyValue;

public final class PlaygroundRunConfigurationType extends ConfigurationTypeBase {

  static final String ID = "VcAutomationTestRunConfig";

  public PlaygroundRunConfigurationType() {
    super(ID, "VC Automation Test", "VC Automation Test cases", NotNullLazyValue.createValue(() -> AllIcons.Nodes.Console));
    addFactory(new PlaygroundConfigurationFactory(this));
  }

}
