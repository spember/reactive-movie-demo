package reactivegroovydemo.modules.movies.pogos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic
import groovy.transform.ToString

/**
 * @author Steve Pember
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
@ToString
class Movie {

    @JsonProperty("Poster")
    String poster

    @JsonProperty("Title")
    String title

    String imdbID

    @JsonProperty("Year")
    int year

    @JsonProperty("Genre")
    String genre
}
