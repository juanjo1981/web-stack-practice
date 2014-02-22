(ns middleware.params_test
  (:use clojure.test
        middleware.params)
  (:require [clojure.string :as string]
             [clojure.java.io :as io]))

(deftest test-parse-params-string
  (let [encoded-str "En%2Fun%25lugar%24de·la%21mancha"
        decoded-str "En/un%lugar$de·la!mancha"]
    (is (= {:param1 "value1" :param2 "value2"} (parse-params-string "param1=value1&param2=value2")))
  (is (= {:param1 "value1" :param2 decoded-str} (parse-params-string (str "param1=value1&param2=" encoded-str))))
  (is (= {:param1 "value1" :param2 nil} (parse-params-string "param1=value1&param2=")))
  (is (= {} (parse-params-string nil)))
  (is (= {} (parse-params-string "")))))

(def apply-wrap-params (wrap-params identity))

(deftest test-wrap-params-query-string
  (let [request {:query-string "param1=value1&param2=value2"
                 :body (io/input-stream (.getBytes "param3=value3&param4=value4"))}
        response (apply-wrap-params request)]
    (is (= {:param1 "value1", :param2 "value2"} 
           (response :query-params)))
    (is (= {:param3 "value3", :param4 "value4"} 
           (response :form-params)))
    (is (= {:param1 "value1", :param2 "value2", :param3 "value3", :param4 "value4"} 
           (response :params)))))

