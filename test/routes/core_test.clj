(ns routes.core_test
  (:use [clojure.test]
        [routes.core])
  (:require [http.mocks.request :refer [mock-request]]))

(defn get-users [] {:status 200 :body "get-users"})

(defn get-user [{{name :name} :params}] {:status 200 :body (str "get-user " name)})

(defn create-user [{{name :name} :params}] {:status 200 :body (str "create-user 11 " name)})
(defn unfollow-user [] {:status 200 :body "unfollow-users"})

;(def request {:status 200, :request-method "post" :uri "users/1" :params {:name "John" :surname "Doe"}})

(def req (mock-request :post "user/1" {:name "John" :surname "Doe"}))

(def routes
  [{:method "get", :path "users", :params [], :handler get-users, :accepts [], :responds [] }
  {:method "get", :path "users/:id", :params [:id], :handler get-user, :accepts [], :responds [] }
  {:method "post", :path "users/:id", :params [:id], :handler create-user, :accepts [], :responds [] }
  {:method "update", :path "users/unfollow/:id", :params [:id], :handler unfollow-user, :accepts [], :responds []}])

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
           route-post {:method :post, :path "users/:id", :params [:id], :handler identity, :accepts [], :responds [] }]
       (is (= true (match-route route-post req)))
       (is (= false (match-route route-post req-more)))
       (is (= false (match-route route-post req-less)))
       (is (= false (match-route route-post req-end))))))

