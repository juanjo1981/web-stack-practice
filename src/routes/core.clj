(ns routes.core
  (:require [clojure.string :as s])
  (import [java.lang.String]))



(defn normalize-url [path]
  (let [trim-slashes (s/replace path #"/+" "/")
        first-filter (if (.startsWith trim-slashes "/") 
                       (.substring trim-slashes 1 (.length trim-slashes))
                       trim-slashes)
        last-filter (if (.endsWith first-filter "/") 
                      (.substring first-filter 0 (- (.length first-filter) 1))
                      first-filter)]
    last-filter))

(defn path-regex [path]
  (-> path normalize-url (s/replace #":\w+" "\\\\w+") (str "$") re-pattern))

(defn compare-method [op1 op2]
  (= op1 op2)) 

(defn match-route [route request]
  (let [{route-path :path, route-method :method} route
        {request-uri :uri, request-method :request-method} request
        url-match (re-find  (path-regex route-path) request-uri)
        method-match (compare-method route-method request-method)]
    (println (str "route-path "route-path " request-uri " request-uri " request-uri " url-match " method-match " method-match))
    (and method-match ((complement s/blank?) url-match))))

(defn get-handler [routes request]
  (let [match-path  (filter #(match-route % request) routes)]
  (:handler (first match-path))))

(defn wrap-routes [routes]
  (fn [request]
    ((get-handler routes request) request)))



