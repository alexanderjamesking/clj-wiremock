(ns clj-wiremock.core-test
  (:require [clojure.test :refer [deftest use-fixtures is]]
            [cheshire.core :refer [generate-string parse-string]]
            [clj-wiremock.core :refer [config server start stop reset stub count-requests find-requests]]
            [clj-http.client :as client]))

; create a server on the default port (8080)
(def wiremock-server (server))

; start the server at the start of the test suite, stop when done
(use-fixtures :once (fn [test-suite] (start wiremock-server) (test-suite) (stop wiremock-server)))

; reset mappings before each test 
(use-fixtures :each (fn [test-to-run] (reset wiremock-server) (test-to-run)))

(deftest build-config-from-map
  (let [c (config {:port                 12345
                   :https-port           9999
                   :keystore-path        "/some/path"
                   :keystore-password    "somepass"
                   :trust-store-path     "/some/trust/store/path"
                   :trust-store-password "sometruststorepass"
                   :need-client-auth     true})
        hs (.httpsSettings c)]
    (is (= 9999 (.port hs)))
    (is (= "/some/path" (.keyStorePath hs)))
    (is (= "somepass" (.keyStorePassword hs)))
    (is (= "/some/trust/store/path" (.trustStorePath hs)))
    (is (= "sometruststorepass" (.trustStorePassword hs)))
    (is (= true (.needClientAuth hs)))
    (is (= 12345 (.portNumber c)))))

(deftest http-get
  (stub {:request  {:method "GET" :url "/hello"}
         :response {:status 200 :body "Hello World"}})
  (let [r (client/get "http://localhost:8080/hello")]
    (is (= 200 (:status r)))
    (is (= "Hello World" (:body r)))))

(deftest http-get-with-json
  (stub {:request  {:method "GET" :url "/hello"}
              :response {:status  200
                         :body    (generate-string {:some "json"})
                         :headers {:Content-Type "application/json"}
                         }})
  (let [r (client/get "http://localhost:8080/hello")]
    (is (= 200 (:status r)))
    (is (= {:some "json"} (parse-string (:body r) true)))))

(deftest http-post
  (stub {:request  {:method       "POST"
                    :url          "/submit"
                    :bodyPatterns [{:equalTo "expected-body"}]}
         :response {:status 201 :body "Created"}})
  (let [r (client/post "http://localhost:8080/submit" {:body "expected-body"})]
    (is (= 201 (:status r)))
    (is (= "Created" (:body r)))))

(deftest test-count-requests
  (stub {:request  {:method "GET" :url "/hello"}
         :response {:status 200 :body "Hello World"}})
  (client/get "http://localhost:8080/hello")
  (is (= 1 (count-requests {:method "GET" :url "/hello"})))
  (client/get "http://localhost:8080/hello")
  (client/get "http://localhost:8080/hello")
  (is (= 3 (count-requests {:method "GET" :url "/hello"}))))

(deftest test-find-requests
  (stub {:request  {:method "GET" :url "/hello"}
         :response {:status 200 :body "Hello World"}})
  (client/get "http://localhost:8080/hello")
  (let [requests (find-requests {:method "GET" :url "/hello"})
        first-request (first requests)
        is-equal #(is (= %1 (%2 first-request)))]
    (is-equal "GET" :method)
    (is-equal "http://localhost:8080/hello" :absoluteUrl)
    (is-equal "/hello" :url)))
