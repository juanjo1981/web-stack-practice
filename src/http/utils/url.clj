(ns http.utils.url
  (:require [clojure.string :as s])
  (:import [java.net  URLDecoder]))

(defn decode [s encoder] 
  (. URLDecoder decode s encoder))
