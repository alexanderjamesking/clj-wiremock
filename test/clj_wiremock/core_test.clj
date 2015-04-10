(ns clj-wiremock.core-test
  (:require [clojure.test :refer :all]
            [clj-wiremock.core :refer :all]
            [cheshire.core :refer :all]))

(def server 
  (wiremock-server (wiremock-config { :port 22222 })))

(use-fixtures :once 
  (fn [f]
    (configure-for "localhost" 22222)
    (start server)
    (f)
    (stop server)))
 
(use-fixtures :each 
  (fn [f]
    (reset-mappings server)
    (f)))

(defn mapping [req res]
  (parse-string (.toString (.build (.willReturn req res))) true))

(deftest mappings-are-built-correctly
  (let [m (mapping (GET (url-equal-to "/hello")) 
                   (-> (create-response)
                       (with-body "Hello World")
                       (with-status 200)))]
    (is (= "/hello" (-> m :request :url)))
    (is (= "GET" (-> m :request :method)))
    (is (= 200 (-> m :response :status)))
    (is (= "Hello World" (-> m :response :body)))))

(deftest mappings-posted-to-wiremock
  (stub-for (will-return (GET (url-equal-to "/hello-world")) 
                         (-> (create-response) 
                             (with-body "Hello World")
                             (with-header "Content-Type" "text/plain")
                             (with-status 200))))
  (let [m (first (get-all-mappings))]
    (is (= "/hello-world" (-> m :request :url)))
    (is (= "GET" (-> m :request :method)))
    (is (= 200 (-> m :response :status)))
    (is (= "text/plain" (-> m :response :headers :Content-Type)))
    (is (= "Hello World" (-> m :response :body)))))

(defn string->integer 
  ([s] (string->integer s 10))
  ([s base] (Integer/parseInt s base)))

(deftest building-config-from-map
  (let [c (wiremock-config { :port 12345 })]
    (is (= 12345 (.portNumber c)))))



