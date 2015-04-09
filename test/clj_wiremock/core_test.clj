(ns clj-wiremock.core-test
  (:require [clojure.test :refer :all]
            [clj-wiremock.core :refer :all]
            [cheshire.core :refer :all]))

(defn mapping [req res]
  (parse-string (.toString (.build (.willReturn req res))) true))

(deftest mapping-builder
  (testing "wrapped methods create a mapping builder"
    (let [m (mapping (GET (urlEqualTo "/hello")) 
                     (-> (response)
                         (.withBody "Hello World")
                         (.withStatus 200)))]
      (is (= "/hello" (-> m :request :url)))
      (is (= "GET" (-> m :request :method)))
      (is (= 200 (-> m :response :status)))
      (is (= "Hello World" (-> m :response :body))))))
