/**
 * Main Mobile Number Portability Module
 *
 * @author Victor Polischuk
 * @since 22.03.2015
 */
(function () {
    var app = angular.module("remoteService", []);

    app.factory("remote", ["$http", "$log", function ($http, $log) {
        var self = {};

        self.getStatistics = function (presentationKey) {
            $log.info("Call remote server to get statistics for " + presentationKey);

            return $http.get("/rs/stat/" + presentationKey, {});
        };

        self.registerPresentation = function (presentation) {
            $log.info("Call remote server to register a presentation");

            return $http.post("/rs/register/presentation", presentation);
        };

        self.getPresentations = function () {
            $log.info("Call remote server to get all presentations");

            return $http.get("/rs/presentations", {});
        };

        return self;
    }]);
})();