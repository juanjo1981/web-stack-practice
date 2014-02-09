(ns http.adapter.grizzly-test
  (:use clojure.test
        http.adapter.grizzly))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(defmacro with-server [handlers port body]
  (let [server (run-grizzly handlers port)]
    (try
      body
      (finally
          (.stop server)))))

(deftest test-run-grizzly
  (testing "HTTP Server"))
