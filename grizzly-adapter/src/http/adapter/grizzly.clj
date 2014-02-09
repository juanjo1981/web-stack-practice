(ns http.adapter.grizzly
  (import (org.glassfish.grizzly.http.server HttpServer))
  (import (org.glassfish.grizzly.http.server HttpHandler))
  (import (org.glassfish.grizzly.http.server Request))
  (import (org.glassfish.grizzly.http.server Response))
  
  )



;----------------FAKE TESTING ------------------

(defn foo []
  "this is the foo handler")

(defn bar []
  "this is the bar handler")

(def routes {"/foo" foo, "/bar" bar})

(def routes_map [{:method "GET" :route "/foo" :handler foo}
                 {:method "GET" :route "/bar" :handler bar}
                 ])

(defn start-server []
  (let [http-server (. HttpServer createSimpleServer)] 
    (.start http-server)))

;---------------END FAKE TESTING ----------

(defn get-app-routes [routes_map]
  (map :route routes_map))

(defn grizzly-handler-for [routes-handler-map]
  (proxy [HttpHandler] [] 
    (service [request response]
      (let [handler (routes-handler-map (.getRequestURI request) )
            ;request-route (.getRequestURI request)
            response-text (handler)]
        (.setContentType response "text/plain")
        (.setContentLength response (.length response-text))
        (.write (.getWriter response) response-text)))))


(defn start-server-with-adapter [port routes-handler-map]
  (let [server (. HttpServer createSimpleServer "/", port)
        config (.getServerConfiguration server)]
    (.addHttpHandler config (grizzly-handler-for routes-handler-map) (into-array String (get-app-routes routes)))
    (.start server)))


            
