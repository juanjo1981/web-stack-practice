(ns util.codec
  (:import [org.glassfish.grizzly.http.util URLDecoder]))

(defn decode [s encoder] 
  (. URLDecoder decode s true encoder))
