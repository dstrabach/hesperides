package org.hesperides.infrastructure.mongo.workshopproperties;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.hesperides.domain.WorkshopPropertyCreatedEvent;
import org.hesperides.domain.WorkshopPropertyExistsQuery;
import org.hesperides.domain.WorkshopPropertyUpdatedEvent;
import org.hesperides.domain.workshopproperties.WorkshopPropertyProjectionRepository;
import org.hesperides.domain.workshopproperties.queries.views.WorkshopPropertyView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.hesperides.domain.framework.Profiles.FAKE_MONGO;
import static org.hesperides.domain.framework.Profiles.MONGO;

@Profile({MONGO, FAKE_MONGO})
@Repository
public class MongoWorkshopPropertyProjectionRepository implements WorkshopPropertyProjectionRepository {

    private final MongoWorkshopPropertyRepository workshopPropertyRepository;

    @Autowired
    public MongoWorkshopPropertyProjectionRepository(MongoWorkshopPropertyRepository workshopPropertyRepository) {
        this.workshopPropertyRepository = workshopPropertyRepository;
    }

    /*** EVENT HANDLERS ***/
    @EventSourcingHandler
    public void on(WorkshopPropertyCreatedEvent event) {
        WorkshopPropertyDocument workshopPropertyDocument =
                WorkshopPropertyDocument.fromDomainInstance(event.getWorkshopProperty());
        // rappel la vérification d'un éventuel doublon est faite dans WorkshopPropertyUsesCases avec une query.
        workshopPropertyRepository.save(workshopPropertyDocument);
    }

    @EventSourcingHandler
    public void onWorkshopPropertyUpdatedEvent(WorkshopPropertyUpdatedEvent event) {
        WorkshopPropertyDocument workshopPropertyDocument =
                WorkshopPropertyDocument.fromDomainInstance(event.getWorkshopProperty());
        workshopPropertyRepository.save(workshopPropertyDocument);
    }

    /*** QUERY HANDLERS ***/
    @QueryHandler
    public Optional<WorkshopPropertyView> query(WorkshopPropertyExistsQuery query) {
        Optional<WorkshopPropertyView> workshopPropertyView = Optional.empty();

        Optional<WorkshopPropertyDocument> workshopPropertyDocument =
                this.workshopPropertyRepository.findOptionalByKey(query.getKey());
        if (workshopPropertyDocument.isPresent()) {
            workshopPropertyView = Optional.of(workshopPropertyDocument.get().toWorkshopPropertyView());
        }
        return workshopPropertyView;
    }

}
