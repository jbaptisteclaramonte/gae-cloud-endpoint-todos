/*global angular */

/**
 * The main controller for the app. The controller:
 * - retrieves and persists the model via the todoStorage service
 * - exposes the model to the template and provides event handlers
 */
angular.module('todomvc')
	.controller('TodoCtrl', function TodoCtrl($scope, $routeParams, $window, $filter, todoStorage) {
		'use strict';

		var todos = $scope.todos = todoStorage.get();

		$scope.newTodo = '';
		$scope.editedTodo = null;

        /**
         * Ajout pour fonctionner avec Google Cloud Endpoint
         * Fonction interceptant l'appel à window.init() effectué dans index.html
         */
        $window.init= function() {
            console.log("$window.init called");
            $scope.$apply($scope.load_gapi_todo_lib);
        };

        /**
         * Charge l'api todos
         */
        $scope.load_gapi_todo_lib = function() {
            console.log("load_todo_lib called");

            var rootApi = 'http://localhost:8080/_ah/api';

            gapi.client.load('todos', 'v2', function() {
                console.log("todos api loaded");
                gapi.client.todos.list().execute(function(resp) {
                    console.log(resp);
                });
                console.log("todos api sucessfully called");
            }, rootApi);
        };

		$scope.$watch('todos', function (newValue, oldValue) {
			$scope.remainingCount = $filter('filter')(todos, { completed: false }).length;
			$scope.completedCount = todos.length - $scope.remainingCount;
			$scope.allChecked = !$scope.remainingCount;
			if (newValue !== oldValue) { // This prevents unneeded calls to the local storage
				todoStorage.put(todos);
			}
		}, true);

		// Monitor the current route for changes and adjust the filter accordingly.
		$scope.$on('$routeChangeSuccess', function () {
			var status = $scope.status = $routeParams.status || '';

			$scope.statusFilter = (status === 'active') ?
				{ completed: false } : (status === 'completed') ?
				{ completed: true } : null;
		});

		$scope.addTodo = function () {
			var newTodo = $scope.newTodo.trim();
			if (!newTodo.length) {
				return;
			}

			todos.push({
				title: newTodo,
				completed: false
			});

			$scope.newTodo = '';
		};

		$scope.editTodo = function (todo) {
			$scope.editedTodo = todo;
			// Clone the original todo to restore it on demand.
			$scope.originalTodo = angular.extend({}, todo);
		};

		$scope.doneEditing = function (todo) {
			$scope.editedTodo = null;
			todo.title = todo.title.trim();

			if (!todo.title) {
				$scope.removeTodo(todo);
			}
		};

		$scope.revertEditing = function (todo) {
			todos[todos.indexOf(todo)] = $scope.originalTodo;
			$scope.doneEditing($scope.originalTodo);
		};

		$scope.removeTodo = function (todo) {
			todos.splice(todos.indexOf(todo), 1);
		};

		$scope.clearCompletedTodos = function () {
			$scope.todos = todos = todos.filter(function (val) {
				return !val.completed;
			});
		};

		$scope.markAll = function (completed) {
			todos.forEach(function (todo) {
				todo.completed = !completed;
			});
		};
	});
