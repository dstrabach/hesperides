package org.hesperides.application.workshopproperties;

import org.hesperides.domain.exceptions.NotFoundException;
import org.hesperides.domain.security.User;
import org.hesperides.domain.workshopproperties.commands.WorkshopPropertyCommands;
import org.hesperides.domain.workshopproperties.entities.WorkshopProperty;
import org.hesperides.domain.workshopproperties.exceptions.DuplicateWorkshopPropertyException;
import org.hesperides.domain.workshopproperties.exceptions.WorkshopPropertyNotFoundException;
import org.hesperides.domain.workshopproperties.queries.WorkshopPropertyQueries;
import org.hesperides.domain.workshopproperties.queries.views.WorkshopPropertyView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WorkshopPropertyUseCases {

    private final WorkshopPropertyCommands commands;
    private final WorkshopPropertyQueries queries;

    @Autowired
    public WorkshopPropertyUseCases(WorkshopPropertyCommands commands, WorkshopPropertyQueries queries) {
        this.commands = commands;
        this.queries = queries;
    }

    public String createWorkshopProperty(WorkshopProperty workshopProperty, User user) {
        String workshopPropertyKey = workshopProperty.getKey();
        if (queries.workshopPropertyExists(workshopPropertyKey)) {
            throw new DuplicateWorkshopPropertyException(workshopPropertyKey);
        }
        return this.commands.createWorkshopProperty(workshopProperty, user);
    }

    public WorkshopPropertyView getWorkshopProperty(String workshopPropertyKey) {
        Optional<WorkshopPropertyView> optionalWorkshopPropertyView =
                this.queries.getOptionalWorkshopPropertyView(workshopPropertyKey);
        if (!optionalWorkshopPropertyView.isPresent()) {
            throw new WorkshopPropertyNotFoundException(workshopPropertyKey);
        }
        return optionalWorkshopPropertyView.get();
    }

    public void updateWorkshopProperty(WorkshopProperty workshopProperty, User user) {
        String workshopPropertyKey = workshopProperty.getKey();
        if (! queries.workshopPropertyExists(workshopPropertyKey)) {
            throw new WorkshopPropertyNotFoundException(workshopPropertyKey);
        }
        commands.updateWorkshopProperty(workshopProperty, user);
    }
}
