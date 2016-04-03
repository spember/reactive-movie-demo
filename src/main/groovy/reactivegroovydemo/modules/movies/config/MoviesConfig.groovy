package reactivegroovydemo.modules.movies.config

/**
 * @author Steve Pember
 */
class MoviesConfig {

    boolean buildRepo = false
    List<YearlyBreakdown> breakdown
}

class YearlyBreakdown {
    int year
    List<String> titles
}