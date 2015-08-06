(ns http.utils.request
  (:require [clojure.string :as s])
  (:import [java.net URI]))

(defn compare-method [op1 op2]
  (if (or (= op1 :any) (= op2 :any))
    true
    (= op1 op2)))

(defn prepare-path [path]
  (if (s/blank? path) 
    ""
    (if (= 0 (.indexOf path "/"))
      (.replaceFirst path "/" "")
      path)))

(defn prepare-uri [uri]
  (.normalize (. URI create uri)))


