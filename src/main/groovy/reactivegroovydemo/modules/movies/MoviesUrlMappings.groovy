package reactivegroovydemo.modules.movies

import com.google.inject.Inject
import groovy.util.logging.Slf4j
import ratpack.exec.Promise
import ratpack.groovy.handling.GroovyChainAction
import ratpack.handling.Context
import ratpack.handling.internal.DefaultContext
import ratpack.http.internal.DefaultRequest
import reactivegroovydemo.modules.movies.pogos.ApiQuery
import reactivegroovydemo.modules.movies.pogos.Movie
import reactivegroovydemo.modules.movies.services.MovieRepositoryService
import reactivegroovydemo.modules.movies.services.ScratchService
import rx.Observable
import rx.schedulers.Schedulers

import static ratpack.jackson.Jackson.json

/**
 * @author Steve Pember
 */
@Slf4j
class MoviesUrlMappings extends GroovyChainAction {
    @Inject MovieRepositoryService repositoryService
    @Inject ScratchService scratchService

    @Override
    void execute() throws Exception {
        // api
        path("test") {Context ctx ->
            render json(scratchService.test())

        }

        path("movies") {Context ctx ->

            byMethod {
                get {
                    Date start = new Date()
                    repositoryService.parseQuery(ctx).observe()
                    //ctx.parse(ApiQuery).observe()
                    .single()
                    .map({ApiQuery query ->
                        repositoryService.query(query)
                    })

                    .flatMap({Observable<Movie> movies->
                        repositoryService.processResults(movies)
                    })
                    .subscribe({
                        ctx.render(json(it))
                    }, {log.error("Error: ", it)}, {log.debug("Done: ${new Date().time-start.time} ms")})

                }
            }
        }

    }
}
