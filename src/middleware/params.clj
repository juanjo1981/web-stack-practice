(ns middleware.params
  (:require [clojure.string :as string]))


(defn form-param [string-param]
  (let [[param-name value] (string/split string-param #"=")]
    [(keyword param-name) value]))

;(defn- parse-query-string [query-string]
;  (let [params ((string/split query-string #"&"))]
;    (map form-param params)))

;AÑADIR PARÁMETROS AL REQUEST
;(defn wrap-params [request]
;  (let [query-params (request :query-string)
;        form-params (request :body)]
;    (request)))
