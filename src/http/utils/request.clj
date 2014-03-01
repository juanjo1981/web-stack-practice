(ns http.utils.request
  (import [java.net URI]))

(defn compare-method [op1 op2]
  (if (or (= op1 :any) (= op2 :any))
    true
    (= op1 op2)))

(defn get-path [request]
  (.getPath (.normalize (. URI create (:uri request)))))


