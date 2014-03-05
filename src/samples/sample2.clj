(ns samples.sample2
  (:require [http.adapter.grizzly.server :as server]
            [http.middleware.params :as params :refer [wrap-params]]
            [routes.core :as routes :refer [GET POST defroutes]]))


(defn get-users [] {:status 200 :body "get-users"})

(defn get-user [{{name :name} :params}] {:status 200 :body (str "get-user " name)})

(defn create-user [{{name :name} :params}] {:status 200 :body (str "create-user 11 " name)})
(defn unfollow-user  []  {:status 200 :body "unfollow-users"})

(def request {:status 200, :request-method "post" :uri "users/1" :params {:name "John" :surname "Doe"}})

(defroutes app-routes
  (GET "users" [] get-users)
  (GET "users/:id" [:id] get-user)
  (POST "users/:id" [:id] create-user)
  (POST "users/unfollow/:id" [:id] unfollow-user))

(def app-with-routes (wrap-params (routes/wrap-routes routes)))

(def app-with-routes-macros (wrap-params app-routes))

(defn ex-routes [port]
  (server/run-grizzly app-with-routes port))

(defn ex-routes-macros [port]
  (server/run-grizzly app-with-routes-macros port))

