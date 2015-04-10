(ns clj-wiremock.core
  (:require [cheshire.core :refer :all])
  (:import com.github.tomakehurst.wiremock.WireMockServer
           com.github.tomakehurst.wiremock.core.WireMockConfiguration
           com.github.tomakehurst.wiremock.client.WireMock))

(defn listAllStubMappings [] (WireMock/listAllStubMappings))
(defn get-all-mappings [] (parse-string (.toString (.getMappings (listAllStubMappings))) true))

(defn wiremock-config 
  ([] (new WireMockConfiguration))
  ([config-map] 
    (let [config (new WireMockConfiguration)]
    (when-let [port (:port config-map)] (.port config port))  
    config)))

(defn wiremock-server 
  [config] 
  (new com.github.tomakehurst.wiremock.WireMockServer config))

(defn reset-mappings [server] (.resetMappings server))
(defn start [server] (.start server))
(defn stop [server] (.stop server))

(defn configure-for [host port](WireMock/configureFor host port))

(defn GET [x] (WireMock/get x))

(defn url-equal-to [url] (WireMock/urlEqualTo url))
(defn url-matching [url] (WireMock/urlMatching url))
(defn url-path-equal-to [url] (WireMock/urlPathEqualTo url))
(defn stub-for [x] (WireMock/stubFor x))
(defn create-response [] (WireMock/aResponse))

(defn will-return [req res] (.willReturn req res))
(defn with-body [response-builder body] (.withBody response-builder body))
(defn with-status [response-builder status] (.withStatus response-builder status))
(defn with-header [response-builder k v] (.withHeader response-builder k v))
