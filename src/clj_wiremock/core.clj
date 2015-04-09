(ns clj-wiremock.core
  (:import com.github.tomakehurst.wiremock.WireMockServer
           com.github.tomakehurst.wiremock.core.WireMockConfiguration
           com.github.tomakehurst.wiremock.client.WireMock))

(defn GET [x] (WireMock/get x))

(defn urlEqualTo [url] (WireMock/urlEqualTo url))
(defn urlMatching [url] (WireMock/urlMatching url))
(defn urlPathEqualTo [url] (WireMock/urlPathEqualTo url))
(defn stubFor [x] (WireMock/stubFor x))
(defn response [] (WireMock/aResponse))

