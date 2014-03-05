(ns routes.core
  (:require [clojure.string :as s]
            [http.utils.request :as req]
            [http.utils.url :as url])
  (import [java.lang.String]))

(defn- path-regex [path]
  (re-pattern 
    (str "^/?" 
         (-> path url/normalize 
             (s/replace #":\w+" "\\\\w+") 
             (str "$")))))

(defn match-route [route request]
  (let [{route-path :path, route-method :method} route
        request-path (req/get-path request)
        request-method (:request-method request)
        url-match (re-find  (path-regex route-path) request-path)
        method-match (req/compare-method route-method request-method)]
    ;(println " route-path " route-path " request-uri " request-path " url-match " url-match " method-match " method-match)
    (and method-match ((complement s/blank?) url-match))))

(defn- get-route [routes request]
  (let [match-path  (filter #(match-route % request) routes)]
  (first match-path)))

(defn add-route-params [route request]
  (let [req-parts     (s/split (req/get-path request) #"/")
        req-params    (:params request)
        ro-parts      (s/split (:path route) #"/")
        form-param    (fn [op1 op2] (if (re-find #":\w+" op1) [(-> op1 (s/replace-first  #":" "") keyword) op2] []))
        route-params  (into {} (filter (complement empty?) (map form-param ro-parts req-parts)))]
        (conj request
              {:route-params route-params}
              {:params (merge req-params route-params)})))

(defn wrap-routes [routes]
  (fn [request]
    (let [route (get-route routes request)
          handler (:handler route)
          params (:params route)
          req-ruote-params (add-route-params route request)]
      (if (empty? params)
        (handler)
        (handler req-ruote-params)))))

(defn compile-route [method path params & body]
  (when body
    `{:method ~method :path ~path :params ~params :handler ~@body}))

(defmacro defroutes [name & routes]
  `(def ~name
    (wrap-routes ~(vec routes))))

(defmacro GET [path params & body]
    (compile-route :get path params (first body)))

(defmacro POST [path params & body]
    (compile-route :post path params (first body)))

(defmacro DELETE [path params & body]
    (compile-route :delete path params (first body)))

(defmacro UPDATE [path params & body]
    (compile-route :update path params (first body)))

(defmacro PUT [path params & body]
    (compile-route :put path params (first body)))

(defmacro HEAD [path params & body]
    (compile-route :head path params (first body)))

(defmacro ANY [path params & body]
    (compile-route :any path params (first body)))


