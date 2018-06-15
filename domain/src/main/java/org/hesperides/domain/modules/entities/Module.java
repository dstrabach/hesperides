package org.hesperides.domain.modules.entities;

import lombok.Value;
import org.hesperides.domain.technos.entities.Techno;
import org.hesperides.domain.templatecontainer.entities.Template;
import org.hesperides.domain.templatecontainer.entities.TemplateContainer;

import java.util.List;

@Value
public class Module extends TemplateContainer {

    public static final String KEY_PREFIX = "module";

    List<Techno> technos;
    Long versionId;

    public Module(Key key, List<Template> templates, List<Techno> technos, Long versionId) {
        //TODO Est-ce qu'il faut générer le model ici ?
        super(key, templates, null);
        this.technos = technos;
        this.versionId = versionId;
    }
}
