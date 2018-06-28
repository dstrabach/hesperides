package org.hesperides.domain.workshopproperties;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.hesperides.domain.WorkshopPropertyCreatedEvent;
import org.hesperides.domain.WorkshopPropertyExistsQuery;
import org.hesperides.domain.workshopproperties.queries.views.WorkshopPropertyView;

import java.util.Optional;

public interface WorkshopPropertyProjectionRepository {

    /*** EVENT HANDLERS ***/

    @EventSourcingHandler
    void on(WorkshopPropertyCreatedEvent event);

    /*** QUERY HANDLERS ***/

    @QueryHandler
    Optional<WorkshopPropertyView> query(WorkshopPropertyExistsQuery query);
}