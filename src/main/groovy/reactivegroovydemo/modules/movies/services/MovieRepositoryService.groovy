package reactivegroovydemo.modules.movies.services

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import ratpack.exec.Promise
import ratpack.handling.Context
import ratpack.server.Service
import ratpack.server.StartEvent
import reactivegroovydemo.modules.movies.pogos.ApiQuery
import reactivegroovydemo.modules.movies.pogos.Movie
import reactivegroovydemo.modules.movies.config.MoviesConfig
import reactivegroovydemo.modules.movies.pogos.QueryResponse
import rx.Observable
import rx.functions.Func1
import rx.functions.Func2
import rx.functions.Func3
import rx.observables.GroupedObservable

/**
 * @author Steve Pember
 */
@Slf4j
@CompileStatic
class MovieRepositoryService implements Service {
    @Inject MoviesConfig config
    @Inject ObjectMapper mapper

    private List<Movie> movies = []
    Observable<Movie> movieStream

    @Override
    void onStart(StartEvent event) throws Exception {
        if (!config.buildRepo) {
            // here we load all of the movies from the data.json flat file as part of the initialization step
            log.info("Loading movies from data.json")
            File input = new File("data.json")
            movies = (List<Movie>)mapper.readValue(input.text, new TypeReference<List<Movie>>(){});
            log.info("Loaded ${movies.size()} movies")
            // this is very much a shortcut, caching this on the service is likely not the right way to go
            movieStream = Observable.from(movies)
        }
    }


    Promise<ApiQuery> parseQuery(Context ctx) {
        Promise.value(new ApiQuery(ctx.request.queryParams))
    }

    private Observable<Movie> filteredList(ApiQuery query) {

    }

    private boolean compareMovieToQuery(Movie movie, ApiQuery query) {
        query.search ? movie.title.toLowerCase().contains(query.search.toLowerCase()) : false
    }



    /**
     * Returns an Observable which emits the total movies seen during streaming
     *
     * @param query
     * @return
     */
    Observable<Integer> total(ApiQuery query) {
        movieStream
        .count()
    }

    /**
     * The base query which emits a list of all movies found
     *
     * @param query
     * @return
     */
    Observable<Movie> query(ApiQuery query) {
        movieStream.filter({Movie movie-> compareMovieToQuery(movie, query)})
    }

    Observable<List<Movie>> reducedMovies(Observable<Movie> data) {
        data.reduce([], { List movies, Movie movie ->
            movies.add(movie)
            movies
        })
    }


    Observable<QueryResponse> processResults(Observable<Movie> movies) {
        // an example of zip to execute several streams in parallel
        // the the nth emitted item of each stream is passed to the closure. This can be an issue if you have uneven (in terms
        // of response times), but here, luckily, we have quick streams that only emit 1 item
        Observable.zip(reducedMovies(movies), yearBreakdown(movies), genreBreakdown(movies), { List<Movie> data, Map years, Map genres ->
            new QueryResponse(movies: data, yearBreakdown: years, genreBreakdown: genres)
        } as Func3)
    }

    private Observable<Map> genreBreakdown(Observable<Movie> movies) {

        movies
        .flatMap({Movie m ->
            // split the movie genre and create an observable, emitting the genre name
            Observable.from(m.genre.split(","))
        } as Func1)
        .map({String genre -> genre.trim()})
        .groupBy({String genre -> genre})
        .flatMap({GroupedObservable genreGroup ->
            genreGroup
            .count()
            .map({count -> [(genreGroup.key): count]})
        } as Func1)
        .reduce([:], {Map result, Map row -> result + row})
    }

    /**
     * counts the movie list by
     *
     * @param movies
     * @return
     */
    private Observable<Map> yearBreakdown(Observable<Movie> movies) {
        movies
            .groupBy({Movie m -> m.year})
            .flatMap({GroupedObservable yearGroup ->
                yearGroup
                .count()
                .map({int sum -> [(yearGroup.key.toString()): sum]} as Func1)
            })
            .reduce([:], {Map result, Map row ->
                result + row
            })
    }
}
