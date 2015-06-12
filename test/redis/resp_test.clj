(ns redis.resp_test
  (:use [clojure.test]
        [redis.resp])
  (:import [java.io InputStream ByteArrayInputStream]))

(defn mock-response [resp]
  (get-tokens (ByteArrayInputStream. (.getBytes resp))))

(deftest test-parse-resp
  (testing "testing simple strings"
    (let [response (mock-response "+OK\r\n")]
      (is (= (parse-resp response) "OK"))))
   (testing "testing erros"
    (let [response (mock-response "-ERROR\r\n")]
      (is (= (parse-resp response) "ERROR"))))
    (testing "testing bulk strings"
      (let [bulk        (mock-response "$6\r\nfoobar\r\n")
            bulk-empty  (mock-response "$0\r\n\r\n")
            bulk-nil    (mock-response "$-1\r\n")]
        (is (= (parse-resp bulk) "foobar"))
        (is (= (parse-resp bulk-empty) ""))
        (is (= (parse-resp bulk-nil) nil))))
 (testing "testing arrays"
    (let [
          array-empty (mock-response "*0\r\n")
          array-nil (mock-response "*-1\r\n")
          array-integer (mock-response "*4\r\n:1\r\n:2\r\n:3\r\n:4\r\n")
          ]
      (is (= (parse-resp array-empty) []))
      (is (= (parse-resp array-nil) nil))
      (is (= (parse-resp array-integer) [1 2 3 4]))

      ))
   ) 

