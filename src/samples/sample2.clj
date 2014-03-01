(ns samples.sample2
  (:require [http.adapter.grizzly.server :as server]
            [http.middleware.params :as params :refer [wrap-params]]
            [routes.core :as routes]))


(defn get-users [] {:status 200 :body "get-users"})

(defn get-user [{{name :name} :params}] {:status 200 :body (str "get-user " name)})

(defn create-user [{{name :name} :params}] {:status 200 :body (str "create-user 11 " name)})
(defn unfollow-user [] {:status 200 :body "unfollow-users"})

(def request {:status 200, :request-method "post" :uri "users/1" :params {:name "John" :surname "Doe"}})

(def routes
  [{:method :get, :path "users", :params [], :handler get-users, :accepts [], :responds [] }
  {:method :get, :path "users/:id", :params [:id], :handler get-user, :accepts [], :responds [] }
  {:method :post, :path "users/:id", :params [:id], :handler create-user, :accepts [], :responds [] }
  {:method :post, :path "users/unfollow/:id", :params [:id], :handler unfollow-user, :accepts [], :responds []}])


(def app-with-routes (wrap-params (routes/wrap-routes routes)))

(defn ex-with-routes [port]
  (server/run-grizzly app-with-routes port))




