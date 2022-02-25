package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class Startup {
    @Transactional
    public void onStartup(@Observes StartupEvent evt, LaunchMode mode) {
        PanacheEntityBase.deleteAll();
        new Fruit("Banana", "Yellow").persist();
        new Fruit("Apple", "Red").persist();
    }
}
