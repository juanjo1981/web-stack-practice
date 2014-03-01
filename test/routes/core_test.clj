(ns routes.core_test
  (:use [clojure.test]
        [routes.core]))

(defn get-users [] {:status 200 :body "get-users"})

(defn get-user [{{name :name} :params}] {:status 200 :body (str "get-user " name)})

(defn create-user [{{name :name} :params}] {:status 200 :body (str "create-user 11 " name)})
(defn unfollow-user [] {:status 200 :body "unfollow-users"})

(def request {:status 200, :request-method "post" :uri "users/1" :params {:name "John" :surname "Doe"}})

(def routes
  [{:method "get", :path "users", :params [], :handler get-users, :accepts [], :responds [] }
  {:method "get", :path "users/:id", :params [:id], :handler get-user, :accepts [], :responds [] }
  {:method "post", :path "users/:id", :params [:id], :handler create-user, :accepts [], :responds [] }
  {:method "update", :path "users/unfollow/:id", :params [:id], :handler unfollow-user, :accepts [], :responds []}])

(deftest test-get-route-params
  )

