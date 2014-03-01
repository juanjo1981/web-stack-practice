(ns http.mocks.request
  (:require [clojure.string :as s]
            [http.middleware.params :as p])
  (:import [java.net URI]))

(defn params-to-query-s [params]
  (let [pair-to-s  #(reduce (fn [op1 op2] (str (name op1)"="op2 )) %)]
    (cond ((complement map?) params) "" 
          (empty? params) ""
          (= 1 (count params)) (pair-to-s (apply vec params))
          :else (reduce #(str (pair-to-s %1) "&" (pair-to-s %2)) params))))

(defn mock-request 
  ([method url]
   (mock-request method url nil))
  ([method url params]
   (let [uri (.normalize (. URI create (if (empty? url) "" url)))
         port (if (= (.getPort uri) -1) 80 (.getPort uri))
         request {
                  :server-port       (or port 80)
                  :server-name       (or (.getHost uri) "localhost")
                  :remote-addr       "localhost" 
                  :uri               (if (s/blank? url) "/" url)
                  :query-string      (.getRawQuery uri)
                  :scheme            (or (keyword (.getScheme uri)) :http)
                  :request-method    method
                  :content-type       "text/plain"
                  :headers            ""
                  :content-length     ""
                  :character-encoding  ""
                  :ssl-client-cert    ""}]
   (if  (empty? params)
     (p/params-request request)
     (let [body (params-to-query-s params)]
       (p/params-request (assoc request :body body :content-length (count body))))))))

