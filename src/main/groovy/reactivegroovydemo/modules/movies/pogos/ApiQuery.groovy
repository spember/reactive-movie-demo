package reactivegroovydemo.modules.movies.pogos

import groovy.transform.CompileStatic
import groovy.transform.ToString
import ratpack.util.MultiValueMap

/**
 * @author Steve Pember
 */
@ToString
@CompileStatic
class ApiQuery {
    String search

    ApiQuery() {}
    ApiQuery(MultiValueMap params) {
        if (params.containsKey("search")) {
            search = params["search"]
        }
    }
}


