(ns http.adapter.grizzly.utils
  (:import [java.io InputStream]
           [org.glassfish.grizzly.http.server Request]
           [org.glassfish.grizzly.http.server Response]
           [java.net URI])
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [http.utils.request :as req]))

(defn- get-headers
    "Creates a name/value map of all the request headers."
    [^Request request]
  (let [header_names (iterator-seq (.iterator (.getHeaderNames request)))
        get_header (fn [name] [ name (.getHeader request name)])]
    (into {} (map get_header header_names))))

(defn- get-content-length
    "Returns the content length, or nil if there is no content."
    [^Request request]
    (let [length (.getContentLength request)]
          (if (>= length 0) length)))

(defn build-request-map [^Request request]
  (let [uri (req/prepare-uri (.getRequestURI request))
        uri-path (req/prepare-path (.getPath uri))]
    {:server-port        (.getServerPort request)
     :server-name        (.getServerName request)
     :remote-addr        (.getRemoteAddr request)
     :uri                (.toString uri)
     :path               uri-path
     :query-string       (.getQueryString request)
     :scheme             (keyword (.getScheme request))
     :request-method     (-> request .getMethod .toString .toLowerCase keyword) 
     :headers            (get-headers request)
     :content-type       (.getContentType request)
     :content-length     (get-content-length request)
     :character-encoding (.getCharacterEncoding request)
     ;:ssl-client-cert    (get-client-cert request)
     :body               (.getInputStream request)}))

(defn set-body [^Response response body]
  (.write (.getWriter response) body))

(defn set-status [^Response response status]
  (.setStatus response status))

(defn set-headers [^Response response headers]
  (doseq [[key value] headers]
    (.setHeader response key value)))

(defn ^Response build-response 
  [^Response response response-map]
  (let [{status :status, headers :headers, body :body} response-map]
    (doto response
      (set-status status)
      (set-body body)
      (set-headers headers))))


; POSOBLEMENTE ESTA FUNCION TENGA QUE MOVERLA EN CADA MIDDLEWARE
; THINK REFACTOR --> 
;                     Call multimethod that accept sevaral types + default
;                     Careful with performance
;                     Take into account encoding
(defn get-body [request]
  (let [body (:body request)]
    (cond
      (nil? body)
        nil
      (instance? String body) body
      (instance? InputStream body)
        (with-open [b (io/input-stream body)]
          (loop [c (.read b)
                 result []]
            (if (not= c -1)
              (recur (.read b)
                     (conj result (char c)))
              (apply str result))))
      :else "")))

