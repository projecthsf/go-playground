// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package io.github.projecthsf.go.playground.runConfigure;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaygroundConfigurationFactory extends ConfigurationFactory {

  public PlaygroundConfigurationFactory(ConfigurationType type) {
    super(type);
  }

  @Override
  public @NotNull String getId() {
    return PlaygroundRunConfigurationType.ID;
  }

  @NotNull
  @Override
  public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
    return new PlaygroundRunConfiguration(project, this, "Demo");
  }

  @Nullable
  @Override
  public Class<? extends BaseState> getOptionsClass() {
    return PlaygroundRunConfigurationOptions.class;
  }

}
