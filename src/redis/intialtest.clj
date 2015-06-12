(ns redis.intialtest
  (:import [java.net Socket]
           [java.io InputStream
                    OutputStream]))

(def CR (byte 0x0d))
(def LF (byte 0x0a))
(def CRLF (byte-array 2 [CR LF]))

(def server-opts {:host "localhost" 
                  :port 6379
                  :timeout 0})
(def ^:dynamic *redis-connection*)

(defn encode [string]
  (.getBytes string))


(defn write [^OutputStream out output]
  (let [encoded-output (encode output)]
  (.write out encoded-output 0 (alength encoded-output))))

(defn send-command [command]
  (let [out (.getOutputStream *redis-connection*)]
    (write out command)
    (.write out CRLF 0 (alength CRLF))))

(defn wait-response [^InputStream in]
  (apply 
    str 
    (loop [c (.read in)
           result []]
      (println (char c))
      (if (= c -1)
        result
        (recur (.read in)
               (conj result (char c)))))))

(defn send-command-return [command]
  (let [in (.getInputStream *redis-connection*)
        out (.getOutputStream *redis-connection*)]
    (write out command)
    (.write out CRLF 0 (alength CRLF))
    (wait-response in)))

(defn new-connection [server-opts]
  (let [socket (Socket. (server-opts :hosts) (server-opts :port))]
   (doto socket
     (.setTcpNoDelay true)
     (.setKeepAlive true)
     (.setSoTimeout (or (:timeout server-opts) 0)))))

(defmacro with-redis [server-opts & exprs]
  `(with-open [connection# (new-connection ~server-opts)]
     (binding [*redis-connection* connection#]
     (do ~@exprs))))

(defn test-parallel [n]
  (let [sets (range n)]
    (doall (pmap #(with-redis 
                    server-opts 
                    (send-command (str "SADD SET_TEST_P "  %))) 
                 sets))))

(defn test-lineal [n]
  (let [sets (range n)]
    (doall (map #(with-redis 
                    server-opts 
                    (send-command (str "SADD SET_TEST_L "  %))) 
                 sets))))

