(ns clj-wiremock.core
  (:require [cheshire.core :refer :all])
  (:import com.github.tomakehurst.wiremock.WireMockServer
           com.github.tomakehurst.wiremock.core.WireMockConfiguration
           com.github.tomakehurst.wiremock.client.WireMock))

(defn listAllStubMappings [] (WireMock/listAllStubMappings))
(defn get-all-mappings []
  (parse-string (.toString (.getMappings (listAllStubMappings))) true))

(defn reset-mappings [wiremock-server] (.resetMappings wiremock-server))
(defn start [wiremock-server] (.start wiremock-server))
(defn stop [wiremock-server] (.stop wiremock-server))

(defn GET [x] (WireMock/get x))

(defn url-equal-to [url] (WireMock/urlEqualTo url))
(defn url-matching [url] (WireMock/urlMatching url))
(defn url-path-equal-to [url] (WireMock/urlPathEqualTo url))
(defn stub-for [x] (WireMock/stubFor x))
(defn create-response [] (WireMock/aResponse))

(defn will-return [req res] (.willReturn req res))
(defn with-body [response-builder body] (.withBody response-builder body))
(defn with-status [response-builder status] (.withStatus response-builder status))
