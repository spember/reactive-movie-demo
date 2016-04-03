package reactivegroovydemo.modules.movies

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import reactivegroovydemo.modules.movies.services.MovieRepositoryService
import reactivegroovydemo.modules.movies.services.OmdbIngestionService
import reactivegroovydemo.modules.movies.services.ScratchService

/**
 * @author Steve Pember
 */
@Slf4j
@CompileStatic
class MoviesModule extends AbstractModule {
    @Override
    protected void configure() {
        [
                MoviesUrlMappings,
                OmdbIngestionService,
                MovieRepositoryService,
                ScratchService
        ].each {Class c -> bind(c).in(Scopes.SINGLETON)}

    }
}
