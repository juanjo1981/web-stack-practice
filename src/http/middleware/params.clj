(ns http.middleware.params
  (:require [clojure.string :as s]
            [http.utils.url :as url]
            [http.adapter.grizzly.utils :as utils]))

; THINK REFACTOR --> encoding
(defn parse-params-string [query-string]
  (if  ((complement s/blank?) query-string)
    (let [decoded-q-string (url/decode query-string "UTF-8")
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

