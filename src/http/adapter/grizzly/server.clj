(ns http.adapter.grizzly.server
  (:import [org.glassfish.grizzly.http.server HttpServer]
           [org.glassfish.grizzly.http.server HttpHandler])
  (:require [http.adapter.grizzly.utils :as utils]))

(defn grizzly-handler-for [handler]
  (proxy [HttpHandler] [] 
    (service [request response]
      (let [request-map (utils/build-request-map request)
            response-map (handler request-map)]
        (utils/build-response response response-map)))))

(defn ^HttpServer run-grizzly [handler port]
  (let [server (. HttpServer createSimpleServer "/", port)
        config (.getServerConfiguration server)]
    (.addHttpHandler config 
      (grizzly-handler-for handler) 
      (into-array String ["/"]))
    (.start server)
    server))


