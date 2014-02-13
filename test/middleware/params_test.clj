(ns middleware.params_test
  (:use clojure.test)
  (:require [middleware.params :as params]))

(deftest test-form-param
  (let [query-string "param1=value1"]
    (is (= [:param1 "value1"] 
           (params/form-param query-string)))))


