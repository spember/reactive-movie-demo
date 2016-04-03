package reactivegroovydemo.modules.movies.pogos

import groovy.transform.CompileStatic

/**
 * Wraps the various items we'll be returning to the front end
 *
 * @author Steve Pember
 */
@CompileStatic
class QueryResponse {
    List<Movie> movies = []
    Map<String, Integer> yearBreakdown = [:]
    Map<String, Integer> genreBreakdown = [:]
}
