(ns http.utils.request)

(defn compare-method [op1 op2]
  (if (or (= op1 :any) (= op2 :any))
    true
    (= op1 op2)))

