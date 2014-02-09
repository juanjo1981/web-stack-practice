(ns http.adapter.grizzly
  (import (org.glassfish.grizzly.http.server HttpServer))
  (import (org.glassfish.grizzly.http.server HttpHandler))
  (import (org.glassfish.grizzly.http.server Request))
  (import (org.glassfish.grizzly.http.server Response))
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

;----------------FAKE TESTING ------------------

(defn foo []
  "this is the foo handler")

(defn bar []
  "this is the bar handler")

(def routes {"/foo" foo, "/bar" bar})


;---------------END FAKE TESTING ----------

;---------- UTILIDADES REQUEST ----------------
;---------- VA EN OTRO ARCHIVO TOCARA REFACTOR -----

(defn- get-headers
    "Creates a name/value map of all the request headers."
    [^Request request]
    (reduce
          (fn [headers, ^String name]
                  (assoc headers
                                 (.toLowerCase name)
                                 (->> (.getHeaders request name)
                                                   (enumeration-seq)
                                                   (string/join ","))))
          {}
          (enumeration-seq (.getHeaderNames request))))

(defn- get-content-length
    "Returns the content length, or nil if there is no content."
    [^Request request]
    (let [length (.getContentLength request)]
          (if (>= length 0) length)))

(defn build-request-map [^Request request]
  {:server-port        (.getServerPort request)
    :server-name        (.getServerName request)
    :remote-addr        (.getRemoteAddr request)
    :uri                (.getRequestURI request)
    :query-string       (.getQueryString request)
    :scheme             (keyword (.getScheme request))
    ;:request-method     (keyword (.toLowerCase (.getMethod request)))
    :request-method     (keyword (.getMethod request))
    ;:headers            (get-headers request)
    :content-type       (.getContentType request)
    :content-length     (get-content-length request)
    :character-encoding (.getCharacterEncoding request)
    ;:ssl-client-cert    (get-client-cert request)
    :body               (.getInputStream request)})
;------------

(defn get-app-routes [routes-handler-map]
  (keys routes-handler-map))

(defn grizzly-handler-for [routes-handler-map]
  (proxy [HttpHandler] [] 
    (service [request response]
      (let [request-map (build-request-map request)
            handler (routes-handler-map (request-map :uri))
            response-text (.toString request-map)]
        (.setContentType response "text/plain")
        (println request-map :body)
        (.setContentLength response (.length response-text))
        (.write (.getWriter response) response-text)))))

(defn ^HttpServer run-grizzly [routes-handler-map port]
  (let [server (. HttpServer createSimpleServer "/", port)
        config (.getServerConfiguration server)]
    (.addHttpHandler config 
      (grizzly-handler-for routes-handler-map) 
      (into-array String (get-app-routes routes)))
    (.start server)))

