package reactivegroovydemo.modules.movies.services

import com.google.inject.Inject
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import ratpack.service.Service
import ratpack.service.StartEvent
import reactivegroovydemo.modules.movies.config.MoviesConfig
import reactivegroovydemo.modules.movies.config.YearlyBreakdown

/**
 * Fetches the
 *
 * @author Steve Pember
 */
@Slf4j
@CompileStatic
class OmdbIngestionService implements Service {
    @Inject MoviesConfig config
    JsonSlurper slurper

    /*
    Response:
        {"Search":[{"Title":"The Revenant","Year":"2015","imdbID":"tt1663202","Type":"movie","Poster":"http://ia.media-imdb.com/images/M/MV5BMjU4NDExNDM1NF5BMl5BanBnXkFtZTgwMDIyMTgxNzE@._V1_SX300.jpg"},{"Title":"The Apostate: Call of the Revenant","Year":"2015","imdbID":"tt3113466","Type":"movie","Poster":"http://ia.media-imdb.com/images/M/MV5BMTQ3MjM1MjM5OV5BMl5BanBnXkFtZTgwODAyMzYxMjE@._V1_SX300.jpg"},{"Title":"Visigoth: The Revenant King","Year":"2015","imdbID":"tt4396774","Type":"movie","Poster":"N/A"}],"totalResults":"3","Response":"True"}

     */

    @Override
    void onStart(StartEvent event) throws Exception {
        slurper = new JsonSlurper()
        if (config.buildRepo) {
            log.info("Hello. Configuration is set to build repo, so here we go")
            buildRepo()
        }
    }

    private void buildRepo() {
        List movies = []
        Map movieData
        config.breakdown.each { YearlyBreakdown it->
            println "$it.year -> "
            it.titles.each {String title->
                println title
                movieData = queryForData(it.year, title)
                if (movieData) {
                    movies.add(JsonOutput.toJson(movieData))
                }
                Thread.sleep(250)
            }

        }

        new File('data.json').delete()
        File output = new File('data.json')
        output << "[" + movies.join(",\n") +"]"
    }

    private Map queryForData(int year, String title) {
        String result = "http://www.omdbapi.com/?y=$year&type=movie&t=${URLEncoder.encode(title, "utf-8")}".toURL().text
        println result
        Map json  = (Map)slurper.parseText(result)
        if (json.containsKey("Response") && json.containsKey("Error")) {
            null
        } else {
            json
        }
    }
}
