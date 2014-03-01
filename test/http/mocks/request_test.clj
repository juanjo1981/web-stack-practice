(ns http.mocks.request_test
  (:use [clojure.test]
        [http.mocks.request]))

(deftest test-params-to-query-s
 (are [x y] (= x y)
      "" (params-to-query-s nil)
      "" (params-to-query-s "")
      "param1=value1" (params-to-query-s {:param1 "value1"})
      "param1=value1&param2=value2" (params-to-query-s 
                                      {:param1 "value1", :param2 "value2"})))
{:method "post", :path "users/:id", :params [:id], :handler identity, :accepts [], :responds [] }

(deftest test-mock-request
  (testing "values are correct"
    (let [req (mock-request 
              :post 
              "http://192.168.1.35:3000?sort=10" 
              {:param1 "value1" :param2 "value2"})]
      (are [x y] (= x y)
           (:server-port req)   3000
           (:server-name req)   "192.168.1.35"
           (:query-string req)  "sort=10"
           (:body req)          "param1=value1&param2=value2"
           (:content-length req) (count (:body req)))))
  (testing "url without server-name, server-port and scheme"
    (let [req (mock-request :get "/users/1")]
      (are [x y] (= x y)
           (:server-port req) 80
           (:server-name req) "localhost"
           (:scheme req)      :http
           )))
  (testing "empty url"
    (let [req (mock-request :get "")]
     (are [x y] (= x y)
           (:server-port req) 80
           (:uri req) "/"
           (:server-name req) "localhost"
           (:scheme req)      :http
           ))))


