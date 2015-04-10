(ns clj-wiremock.core-test
  (:require [clojure.test :refer :all]
            [clj-wiremock.core :refer :all]
            [cheshire.core :refer :all])
  (:import com.github.tomakehurst.wiremock.WireMockServer
           com.github.tomakehurst.wiremock.core.WireMockConfiguration
           com.github.tomakehurst.wiremock.client.WireMock))

(def wiremock-config (.port (new WireMockConfiguration) 22222))
(def wiremock-server (new com.github.tomakehurst.wiremock.WireMockServer wiremock-config))
 
(use-fixtures :once 
  (fn [f]
    (WireMock/configureFor "localhost" 22222)
    (start wiremock-server)
    (f)
    (stop wiremock-server)))
 
(use-fixtures :each 
  (fn [f]
    (reset-mappings wiremock-server)
    (f)))

(defn mapping [req res]
  (parse-string (.toString (.build (.willReturn req res))) true))

(deftest mappings-are-built-correctly
  (testing "wrapped methods create a mapping builder"
    (let [m (mapping (GET (url-equal-to "/hello")) 
                     (-> (create-response)
                         (with-body "Hello World")
                         (with-status 200)))]
      (is (= "/hello" (-> m :request :url)))
      (is (= "GET" (-> m :request :method)))
      (is (= 200 (-> m :response :status)))
      (is (= "Hello World" (-> m :response :body))))))

(deftest mappings-posted-to-wiremock
  (testing "mappings are posted to wiremock successfully"
    (stub-for (will-return (GET (url-equal-to "/hello-world")) 
                           (-> (create-response) 
                               (with-body "Hello World")
                               (with-status 200))))
    (let [m (first (get-all-mappings))]
      (is (= "/hello-world" (-> m :request :url)))
      (is (= "GET" (-> m :request :method)))
      (is (= 200 (-> m :response :status)))
      (is (= "Hello World" (-> m :response :body))))))

