let $ = require('jquery'),
    rx = require('rx'),
    rxDom = require('rx-dom'),
    MovieManager = new (require('./moviemanager'))(),
    getJSON = rx.DOM.getJSON;

module.exports = {
    init: function () {
        let $auto = $(".autocomplete");
        let searchThreshold = 2;


        // begin work
        let stream = rx.Observable.fromEvent($auto, 'keyup')
            .throttle(300); // also could use .debounce(() => rx.Observable.timer(300))

        stream
            .filter(() => $auto.val().length > searchThreshold)
            .map(() => getJSON("/api/movies?search=" + $auto.val())
                .takeUntil(stream)
            )
            .concatAll()
            .subscribe(MovieManager.renderFullResults);
        // and that's the end. We have basic autocomplete.

        // additional nice things: cleanup the movies if the user types less than the threshold
        stream
            .filter(() => $auto.val().length <= searchThreshold)
            .subscribe(MovieManager.clearMovies);
    }
};