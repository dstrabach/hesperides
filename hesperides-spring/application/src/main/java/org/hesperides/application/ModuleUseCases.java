package org.hesperides.application;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.hesperides.domain.modules.entities.Module;
import org.hesperides.domain.modules.entities.Template;
import org.hesperides.domain.modules.commands.*;
import org.hesperides.domain.modules.queries.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class ModuleUseCases {

    private final CommandGateway commandGateway;

    private final AsyncModuleQueries queryGateway;

    public ModuleUseCases(CommandGateway commandGateway, AsyncModuleQueries queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    public Module.Key createWorkingCopy(Module.Key newModuleKey) {

        try {
            return (Module.Key) commandGateway.send(new CreateModuleCommand(newModuleKey)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Module.Key createWorkingCopyFrom(Module.Key from, Module.Key to) {
        try {
            return (Module.Key) commandGateway.send(new CopyModuleCommand(to, from)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTemplateInWorkingCopy(Module.Key key, Template template) throws Throwable {

        try {
            commandGateway.send(new CreateTemplateCommand(key, template)).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    public CompletableFuture<Object> updateTemplateInWorkingCopy(Module.Key key, Template template) throws Throwable {
        return commandGateway.send(new UpdateTemplateCommand(key, template));
    }

    public void deleteTemplate(Module.Key key, String templateName) throws Throwable {
        try {
            commandGateway.send(new DeleteTemplateCommand(key, templateName)).get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    public Optional<ModuleView> getModule(Module.Key moduleKey) {
        return queryGateway.query(new ModuleByIdQuery(moduleKey));
    }

    public List<String> getModulesNames() {
      return queryGateway.queryAllModuleNames(new ModulesNamesQuery());
    }

    public Optional<TemplateView> getTemplate(Module.Key moduleKey, String templateName) {
        return queryGateway.queryTemplateByName(new TemplateByNameQuery(moduleKey, templateName));
    }
}