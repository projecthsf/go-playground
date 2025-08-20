// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package io.github.projecthsf.go.playground.runConfigure;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaygroundRunConfiguration extends RunConfigurationBase<PlaygroundRunConfigurationOptions> {

    public PlaygroundRunConfiguration(Project project,
                                      ConfigurationFactory factory,
                                      String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    protected PlaygroundRunConfigurationOptions getOptions() {
        return (PlaygroundRunConfigurationOptions) super.getOptions();
    }

    public String getScriptName() {
        return getOptions().getScriptName();
    }

    public void setScriptName(String scriptName) {
        getOptions().setScriptName(scriptName);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new PlaygroundSettingsEditor();
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor,
                                    @NotNull ExecutionEnvironment environment) {
        return new CommandLineState(environment) {
            @NotNull
            @Override
            protected ProcessHandler startProcess() throws ExecutionException {
                GeneralCommandLine commandLine =
                        new GeneralCommandLine(getOptions().getScriptName().split(" "));
                commandLine.setWorkDirectory(environment.getProject().getBasePath());
                OSProcessHandler processHandler = ProcessHandlerFactory.getInstance()
                        .createColoredProcessHandler(commandLine);
                ProcessTerminatedListener.attach(processHandler);
                return processHandler;
            }
        };
    }

}
