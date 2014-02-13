(ns examples.example1
  (:require [http.adapter.grizzly.server :as server]
            [middleware.params :as params]))

(defn index [request]
    "this is the index handler")
(defn foo [request]
    "this is the foo handler")

(defn bar [request]
    "this is the bar handler")

(defn juanjo [request]
    "this is the juanjo handler")

(def routes {"" index, "/foo" foo, "/bar" bar, "/juanjo" juanjo})

(defn handler [request]
  (let [func (routes (request :uri))]
    (println request)
    (func request)))

(defn main [port]
  (server/run-grizzly handler port))


