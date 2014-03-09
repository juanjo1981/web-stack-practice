(ns http.utils.url
  (:require [clojure.string :as s])
  (:import [java.net  URLDecoder]
           [java.net URI]))

(defn decode [s encoder] 
  (. URLDecoder decode s encoder))

(defn normalize [path]
  (let [uri (. URI create path)] (.toString (.normalize uri))))

