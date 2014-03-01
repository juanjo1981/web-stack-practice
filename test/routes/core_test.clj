(ns routes.core_test
  (:use [clojure.test]
        [routes.core])
  (:require [http.mocks.request :refer [mock-request]]))

(deftest test-match-route
  (testing "testing request-method match criterion"
    (let [req (mock-request :post "users/1" {:name "John" :surname "Doe"})
          route-post {:method :post, :path "users/:id", :params [:id], :handler identity, :accepts [], :responds [] }
          route-get (assoc-in route-post [:method] :get)
          route-any (assoc-in route-post [:method]:any)]
      (is (= true (match-route route-post req)))
      (is (= true (match-route route-any req)))
      (is (= false (match-route route-get req)))))
   (testing "testing uri match criterion"
     (let [req (mock-request :post "users/1" {:name "John" :surname "Doe"})
           req-more (assoc-in req [:uri] "users/1/unfollow")
           req-less (assoc-in req [:uri] "users")
           req-end (assoc-in req [:uri] "other-path/users/1")
           route-post {:method :post, :path "users/:id", :params [:id], :handler identity, :accepts [], :responds [] }
           ]
       (is (= true (match-route route-post req)))
       (is (= false (match-route route-post req-more)))
       (is (= false (match-route route-post req-less)))
       (is (= false (match-route route-post req-end)))))
  (testing "testing complete uri = url + path"
    (let [req (mock-request :post "http://localhost:3000/users/1" {:name "John" :surname "Doe"})
           req-end (assoc-in req [:uri] "http://localhost:3000/path/users/1")
           route-post {:method :post, :path "users/:id", :params [:id], :handler identity, :accepts [], :responds [] }
          ]
       (is (= true (match-route route-post req)))
       (is (= false (match-route route-post req-end)))
      )))

(deftest test-add-route-params
  (testing "testing route params are correctly added to the request"
    (let [req (mock-request :post "users/1/contacts/10" {:name "John" :surname "Doe"})
          route-post {:method :post, :path "users/:id/contacts/:cid", :params [:id, :cid], :handler identity, :accepts [], :responds [] }
          result-req (add-route-params route-post req)
          params (:params result-req)]
      (is (= "1" (:id params)))
      (is (= "10" (:cid params))))))

