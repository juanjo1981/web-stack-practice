(ns routes.core_test
  (:use [clojure.test]
        [routes.core]))

(def routes
  [{:method "get", :path "/", :params [], :handler (str "-"), :accepts [], :responds []}
  {:method "get", :path "users", :params [], :handler (str "-"), :accepts [], :responds [] }
  {:method "get", :path "users/:id", :params [:id], :handler (str "-"), :accepts [], :responds [] }
  {:method "post", :path "users/:id", :params [:id], :handler (str "-"), :accepts [], :responds [] }
  {:method "update", :path "users/unfollow/:id", :params [:id], :handler (str "-"), :accepts [], :responds []}])



