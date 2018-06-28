package org.hesperides.domain.workshopproperties.entities;

import lombok.Value;

@Value
public class WorkshopProperty {
    String key;
    String value;
    String keyValue;

    public WorkshopProperty concatKeyValue () {
        String concat = this.getKey() + this.getValue();
        return new WorkshopProperty(this.getKey(), this.getValue(), concat);
    }
}
