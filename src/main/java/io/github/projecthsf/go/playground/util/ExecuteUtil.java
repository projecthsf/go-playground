package io.github.projecthsf.go.playground.util;

import com.intellij.execution.Executor;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.impl.RunConfigurationLevel;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.openapi.project.Project;
import io.github.projecthsf.go.playground.runConfigure.PlaygroundConfigurationFactory;
import io.github.projecthsf.go.playground.runConfigure.PlaygroundRunConfiguration;
import io.github.projecthsf.go.playground.runConfigure.PlaygroundRunConfigurationType;
import org.jetbrains.annotations.NotNull;

public class ExecuteUtil {

    public static void execute(@NotNull Project project, @NotNull String script, String code) {
        PlaygroundRunConfigurationType type = new PlaygroundRunConfigurationType();
        PlaygroundConfigurationFactory factory = new PlaygroundConfigurationFactory(type);
        PlaygroundRunConfiguration configuration = new PlaygroundRunConfiguration(project, factory, code);
        configuration.setScriptName(script);
        factory.createConfiguration("temporary-config-name", configuration);

        RunnerAndConfigurationSettings settings = new RunnerAndConfigurationSettingsImpl(
                (RunManagerImpl) RunManager.getInstance(project),
                configuration,
                true,
                RunConfigurationLevel.TEMPORARY
        );

        Executor executor = DefaultRunExecutor.getRunExecutorInstance();
        ProgramRunnerUtil.executeConfiguration(settings, executor);
    }
}
