/**
 * @preserve Main Mobile Number Portability Application
 *
 * @author Victor Polischuk
 * @since 22.03.2015
 * @copyright 2015
 * @license MIT
 */
(function () {
    var app = angular.module("moodmeterApp", ["remoteService", "ui.bootstrap"]);

    app.controller("StatisticsController", ["remote", function (remote) {
        var self = this;

        self.vote = {"good": 0, "ok": 0, "bad": 0};

        remote.getStatistics().success(function (data) {
            self.vote = data;
        });
    }]);

    app.controller("TabController", function () {
        var self = this;

        self.active = 0;

        self.switch = function () {

        };
    });

    app.controller("PresentationController", ["remote", function (remote) {
        var self = this;
        var cleanProposal = function () {
            return {id: "00000000-0000-0000-0000-000000000000"}
        };

        self.list = [];
        self.proposal = cleanProposal();

        self.submitTalk = function () {
            remote.registerPresentation(self.proposal).success(function (key) {
                self.proposal.key = key;
                self.list.push(self.proposal);
                self.proposal = cleanProposal();
            });
        };

        remote.getPresentations().success(function (data) {
            self.list = data;
        });
    }]);

    app.controller("DatePickerController", ["$scope", function ($scope) {
        $scope.durationOptions = [
            {millis: 60 * 60 * 1000, name: "an hour"},
            {millis: 24 * 60 * 60 * 1000, name: "a day"},
            {millis: 7 * 24 * 60 * 60 * 1000, name: "a week"}
        ];

        $scope.dateOptions = {
            formatDay: 'dd',
            formatMonth: 'MM',
            formatYear: 'yyyy',
            formatDayHeader: 'EEE',
            formatDayTitle: 'MMMM yyyy',
            formatMonthTitle: 'yyyy',
            datepickerMode: 'day',
            minMode: 'day',
            maxMode: 'year',
            showWeeks: true,
            yearRange: 20,
            startingDay: 1
        };
    }]);

})();