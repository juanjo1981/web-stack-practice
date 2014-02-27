(ns http.adapter.grizzly.utils
  (:import [org.glassfish.grizzly.http.server Request]
           [org.glassfish.grizzly.http.server Response]
           [org.glassfish.grizzly.http.io NIOWriter]
           [java.io InputStream])
  (:require [clojure.java.io :as io]))

(defn- get-headers
    "Creates a name/value map of all the request headers."
    [^Request request]
  (let [header_names (iterator-seq (.iterator (.getHeaderNames request)))
        get_header (fn [name] [(keyword name) (.getHeader request name)])]
    (into {} (map get_header header_names))))

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
    :request-method    (-> request .getMethod .toString .toLowerCase) 
    :headers            (get-headers request)
    :content-type       (.getContentType request)
    :content-length     (get-content-length request)
    :character-encoding (.getCharacterEncoding request)
    ;:ssl-client-cert    (get-client-cert request)
    :body               (.getInputStream request)})

(defn ^Response build-response [response response-map]
  (let [body (:body response-map)]
    (.setContentType response "text/plain")
    (.setContentLength response (.length body))
    (.setStatus response (:status response-map))
    (.write (.getWriter response) body)))


; POSOBLEMENTE ESTA FUNCION TENGA QUE MOVERLA EN CADA MIDDLEWARE
; ESTUDIAR LA MACRO WITH-OPEN
(defn get-body [request]
  (let [body (request :body)]
    (cond
      (nil? body)
        nil
      (instance? InputStream body)
        (with-open [b (io/input-stream body)]
          (loop [c (.read b)
                 result []]
            (if (not= c -1)
              (recur (.read b)
                     (conj result (char c)))
              (apply str result))))
      :else "")))

