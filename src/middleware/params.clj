(ns middleware.params
  (:import [org.glassfish.grizzly.http.util URLDecoder])
  (:require [clojure.string :as s]
            [util.codec :as codec]
            [http.adapter.grizzly.utils :as utils]))

(defn parse-params-string [query-string]
  (if  ((complement s/blank?) query-string)
    (let [decoded-q-string (codec/decode query-string "UTF-8")
          q-string-map (s/split decoded-q-string #"&")
          parse-kv-str (fn [kv-str] 
                         (let [[k v] (s/split kv-str #"=")] 
                           [(keyword k) v]))]
    (into {} (map parse-kv-str q-string-map)))
    {}))  

(defn params-request [request & [opts]]
  (let [q-string-params (parse-params-string (request :query-string))
        f-string-params (parse-params-string (utils/get-body request))]
    (conj request 
          {:query-params q-string-params}
          {:form-params f-string-params}
          {:params (merge f-string-params q-string-params)}
          {:params (merge f-string-params q-string-params)})
    ))

(defn wrap-params [handler & [opts]]
  (fn [request]
    (-> request
        (params-request opts)
        handler)))
