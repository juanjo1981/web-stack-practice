(ns samples.crud
    (:require [http.adapter.grizzly.server :as server]
            [http.middleware.params :as params :refer [wrap-params]]
            [routes.core :as routes :refer [defroutes GET POST PUT DELETE]]))

(defn retrieve-user [{{id :id}:params}]
  (let [user (select :users
                     (where {:id id}))]
  {:status 200
   :user user
   :body (str "<html><head> </head><p>You have requested USER with ID: "  "ddd"  "</p></html>")}))

(defroutes app-routes
  ;(GET "users" [] retrieve-users)
  (GET "users/:id" [:id] retrieve-user)
  ;(POST "users/:id" [:id] create-user)
  ;(PUT "users/:id" [:id] update-user)
  ;(DELETE "users/:id" [:id] delete-user)
  )

(def app-crud (wrap-params app-routes))

(defn ex-routes-macros [port]
  (server/run-grizzly app-crud port))

