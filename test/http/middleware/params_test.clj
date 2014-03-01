(ns http.middleware.params_test
  (:use clojure.test
        http.middleware.params)
  (:require [clojure.string :as string]
            [http.mocks.request :refer [request]]
             [clojure.java.io :as io]))

(deftest test-parse-params-string
  (let [encoded-str "En%2Fun%25lugar%24de·la%21mancha"
        decoded-str "En/un%lugar$de·la!mancha"]
    (are [x y] (= x y)
         {:param1 "value1" :param2 "value2"} (parse-params-string "param1=value1&param2=value2")
         {:param1 "value1" :param2 decoded-str} (parse-params-string (str "param1=value1&param2=" encoded-str))
         {:param1 "value1" :param2 nil} (parse-params-string "param1=value1&param2=")
         {} (parse-params-string nil)
         {} (parse-params-string ""))))

(def apply-wrap-params (wrap-params identity))

(deftest test-wrap-params-query-string
  (let [req (dissoc (request :get 
                     "http://localhost:3000?param1=value1&param2=value2" 
                     {:param3 "value3" :param4 "value4"}) 
                    :query-params :form-params :params)
        response (apply-wrap-params req)]
    (are [x y] (= x y)
         {:param1 "value1", :param2 "value2"} (response :query-params)
         {:param3 "value3", :param4 "value4"} (response :form-params)
         {:param1 "value1", :param2 "value2", :param3 "value3", :param4 "value4"} (response :params))))

