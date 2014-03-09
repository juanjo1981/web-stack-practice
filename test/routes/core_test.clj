(ns routes.core_test
  (:use [clojure.test]
        [routes.core])
  (:require [http.mocks.request :refer [mock-request]]))

(deftest test-match-route
  (testing "testing request-method match criterion"
    (let [req (mock-request :post "users/1" {:name "John" :surname "Doe"})
          route-post (POST "users/:id" [:id] identity)
          route-get (GET "users/:id" [:id] identity)
          route-any (ANY "users/:id" [:id] identity)]
      (is (= true (match-route route-post req)))
      (is (= true (match-route route-any req)))
      (is (= false (match-route route-get req)))))
   (testing "testing uri match criterion"
     (let [
           req (mock-request :post "users/1")
           req-more (mock-request :post "users/1/unfollow")
           req-less (mock-request :post "users")
           req-end (mock-request :post "other-path/users/1")
           route-post (POST "users/:id" [:id] identity)
           ]
       (is (= true (match-route route-post req)))
       (is (= false (match-route route-post req-more)))
       (is (= false (match-route route-post req-less)))
       (is (= false (match-route route-post req-end)))))
  (testing "testing complete uri = url + path"
    (let [req     (mock-request :post "http://localhost:3000/users/1")
          req-end (mock-request :post "http://localhost:3000/path/users/1")
          route-post (POST "users/:id" [:id] identity)]
       (is (= true (match-route route-post req)))
       (is (= false (match-route route-post req-end)))
      )))

(deftest test-add-route-params
 (testing "testing route params are correctly added to the request"
    (let [req (mock-request :post "users/1/contacts/10" {:name "John" :surname "Doe"})
          route-post (POST "users/:id/contacts/:cid" [:id, :cid] :handler identity)
          result-req (add-route-params route-post req)
          params (:params result-req)]
      (is (= "1" (:id params)))
      (is (= "10" (:cid params)))))
   (testing "testing route params are correctly added to the request"
    (let [req (mock-request :post "http://localhost:7654/users/unfollow/2")
          route-post (POST "users/unfollow/:id" [:id] :handler identity)
          result-req (add-route-params route-post req)
          params (:params result-req)]
      (is (= "2" (:id params))))))
   

