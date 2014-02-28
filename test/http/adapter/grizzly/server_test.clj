(ns http.adapter.grizzly.server_test
  (:use [clojure.test]
        [adapter.grizzly.server])
  (:require [clj-http.client :as http])
  (:import [org.glassfish.grizzly.http.server HttpServer]))

(defn handler [request]
  {:status  200 
   :headers {"Content-Type" "text/plain"} 
   :body "Hello World"})

(defmacro with-server [handler port & body]
  `(let [server# (run-grizzly ~handler ~port)]
     (try
       ~@body
       (finally 
         (.shutdown server#)))))

(deftest test-run-grizzly
  (testing "HTTP Server"
    (with-server handler 7650
      (let [response (http/get "http://localhost:7650/foo")]
        (is (= 200 (response :status)))))))

