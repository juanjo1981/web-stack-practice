(ns examples.example1
  (:require [http.adapter.grizzly.server :as server]
            [middleware.params :as params :refer [wrap-params ]]))

(defn index [request]
    "this is the index handler")
(defn foo [request]
    "this is the foo handler")

(defn bar [request]
    "this is the bar handler")

(defn juanjo [request]
    "this is the juanjo handler")


(def routes {"/" index, "/foo" foo, "/bar" bar, "/juanjo" juanjo})

(defn handler [request]
  {:status  200 
   :headers {"Content-Type" "text/plain"} 
   :body "Hello World"})


(defn simple-handler [request]
  (let [func (routes (request :uri))]
    (println request)
    (func request)))

(defn ex-simple-handler [port]
  (server/run-grizzly handler port))


(defn handler-with-params [{params :params}]
  (str "Esto son los parámetros " params))

(def app-with-params (wrap-params handler-with-params))

(defn ex-handler-with-params [port]
  (server/run-grizzly app-with-params port ))


