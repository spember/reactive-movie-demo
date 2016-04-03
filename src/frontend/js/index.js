var Grapnel = require('grapnel');
var $ = require('jquery');
var rx = require('rx');
var autocomplete = require('./app/autocomplete');
var router = new Grapnel();

router.get("", function (req) {
    autocomplete.init();

});
router.get("/movies/:id", function (req){

})
