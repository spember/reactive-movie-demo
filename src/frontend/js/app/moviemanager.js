var $ = require('jquery');

module.exports = function () {
    var $container = $("#moviesContainer"),
        $meta = $("#stats");

    function renderMovies(movies) {
        clearMovies();
        if (movies.length == 0) {
            noResults();
        }
        movies.forEach(function (movie) {
            $container.append('<div class="movie"><img src="' + movie.Poster + '" alt="' +movie.Title +'"/><p>' +movie.Title +'</p></div>')
        });
    }

    function listStats(title, stats) {
        var html = "<p>" + title +":";
        for (var key in stats) {
            html += "<span class='stat-label'>" + key +"</span><span class='stat'>(" + stats[key] + ")</span>";
        }
        return html +"</p>";
    }

    function renderStats(queryResponse) {
        var years = listStats("Years", queryResponse.yearBreakdown),
            genres = listStats("Genres", queryResponse.genreBreakdown);
        $meta.append(years);
        $meta.append(genres);
    }

    function renderFullResults(queryResponse) {
        renderMovies(queryResponse.movies);
        renderStats(queryResponse);
    }

    function clearMovies() {
        $container.html("");
        $meta.html("");
    }

    function noResults() {
        $container.html('<p class="warning">No results were found</p>')
    }

    return {
        renderMovies: renderMovies,
        clearMovies: clearMovies,
        noResults: noResults,
        renderFullResults: renderFullResults
    }

};