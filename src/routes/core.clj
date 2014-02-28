(ns routes.core
  (:require [clojure.string :as s]
            [http.utils.url :as url]
            [http.utils.request :as req])
  (import [java.lang.String]))

(defn- path-regex [path]
  (-> path url/normalize (s/replace #":\w+" "\\\\w+") (str "$") re-pattern))

(defn- match-route [route request]
  (let [{route-path :path, route-method :method} route
        {request-uri :uri, request-method :request-method} request
        url-match (re-find  (path-regex route-path) request-uri)
        method-match (req/compare-method route-method request-method)]
    (println (str "route-path "route-path " request-uri " request-uri " request-uri " url-match " method-match " method-match))
    (and method-match ((complement s/blank?) url-match))))

(defn- get-handler [routes request]
  (let [match-path  (filter #(match-route % request) routes)]
  (:handler (first match-path))))

(defn wrap-routes [routes]
  (fn [request]
    ((get-handler routes request) request)))

(defn get-route-params [route request])
