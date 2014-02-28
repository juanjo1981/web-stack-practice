(ns http.utils.url
  (:require [clojure.string :as s])
  (:import [java.net URLDecoder]))

(defn decode [s encoder] 
  (. URLDecoder decode s encoder))

(defn normalize [path]
  (let [trim-slashes (s/replace path #"/+" "/")
        first-filter (if (.startsWith trim-slashes "/") 
                       (.substring trim-slashes 1 (.length trim-slashes))
                       trim-slashes)
        last-filter (if (.endsWith first-filter "/") 
                      (.substring first-filter 0 (- (.length first-filter) 1))
                      first-filter)]
    last-filter))


