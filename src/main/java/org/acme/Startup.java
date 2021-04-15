package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class Startup {
    public void onStartup(@Observes StartupEvent evt, LaunchMode mode) {
        Panache.withTransaction(() -> {
            return Fruit.deleteAll()
                    .flatMap(v -> new Fruit("Banana", "Yellow").persist())
                    .flatMap(v -> new Fruit("Apple", "Red").persist());
        }).await().indefinitely();
    }
}
