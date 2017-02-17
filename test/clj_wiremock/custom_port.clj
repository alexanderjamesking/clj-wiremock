(ns clj-wiremock.custom-port
  (:require [clojure.test :refer [deftest use-fixtures is]]
            [clj-wiremock.core :refer [config server start stop reset stub count-requests find-requests]]
            [clj-http.client :as client]))

(def wiremock-server (server (config {:port 11111})))

(def server-url "http://localhost:11111")

; start the server at the start of the test suite, stop when done
(use-fixtures :once (fn [test-suite] (start wiremock-server) (test-suite) (stop wiremock-server)))

; reset mappings before each test 
(use-fixtures :each (fn [test-to-run] (reset wiremock-server) (test-to-run)))

(deftest stub-with-server-url
  (stub {:request  {:method "GET" :url "/hello"}
         :response {:status 200 :body "Hello World"}}
        server-url)
  (let [r (client/get "http://localhost:11111/hello")]
    (is (= 200 (:status r)))
    (is (= "Hello World" (:body r)))))

(deftest find-requests-with-server-url
  (stub {:request  {:method "GET" :url "/hello"}
         :response {:status 200 :body "Hello World"}}
        server-url)
  (client/get "http://localhost:11111/hello")
  (let [requests (find-requests {:method "GET" :url "/hello"} server-url)
        first-request (first requests)
        is-equal #(is (= %1 (%2 first-request)))]
    (is-equal "GET" :method)
    (is-equal "http://localhost:11111/hello" :absoluteUrl)
    (is-equal "/hello" :url)))

(deftest count-requests-with-server-url
  (stub {:request  {:method "GET" :url "/hello"}
         :response {:status 200 :body "Hello World"}}
        server-url)
  (client/get "http://localhost:11111/hello")
  (is (= 1 (count-requests {:method "GET" :url "/hello"} server-url)))
  (client/get "http://localhost:11111/hello")
  (client/get "http://localhost:11111/hello")
  (is (= 3 (count-requests {:method "GET" :url "/hello"} server-url))))
