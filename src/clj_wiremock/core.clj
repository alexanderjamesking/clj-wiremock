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
      (when-let [v (:port config-map)] (.port config (int v)))
      (when-let [v (:https-port config-map)] (.httpsPort config (int v)))
      (when-let [v (:keystore-path config-map)] (.keystorePath config v))
      (when-let [v (:keystore-password config-map)] (.keystorePassword config v))
      (when-let [v (:trust-store-path config-map)] (.trustStorePath config v))
      (when-let [v (:trust-store-password config-map)] (.trustStorePassword config v))
      (when-let [v (:need-client-auth config-map)] (.needClientAuth config v))
      config)))

(defn wiremock-server 
  [config] 
  (new com.github.tomakehurst.wiremock.WireMockServer config))

(defn reset-mappings [server] (.resetMappings server))
(defn start [server] (.start server))
(defn stop [server] (.stop server))

; WireMock Client

(defn configure-for [host port](WireMock/configureFor host port))

; (UrlMatchingStrategy) -> MappingBuilder
(defn GET [x] (WireMock/get x))
(defn POST [x] (WireMock/post x))
(defn PUT [x] (WireMock/put x))
(defn DELETE [x] (WireMock/delete x))

; (defn PATCH [x] (WireMock/patch x))
; (defn HEAD [x] (WireMock/head x))
; (defn OPTIONS [x] (WireMock/options x))
; (defn TRACE [x] (WireMock/trace x))
; (defn ANY [x] (WireMock/any x))

; (String) -> UrlMatchingStrategy
(defn url-equal-to [url] (WireMock/urlEqualTo url))
(defn url-matching [url] (WireMock/urlMatching url))
(defn url-path-equal-to [url] (WireMock/urlPathEqualTo url))

; (String) -> ValueMatchingStrategy
;equalTo
;equalToJson
;equalToJson (String value, JSONCompareMode jsonCompareMode) -> ValueMatchingStrategy
;equalToXml 
;matchingXPath
;containing
;matching
;notMatching
;matchingJsonPath

(defn stub-for [mapping-builder] (WireMock/stubFor mapping-builder))
(defn response 
  ([] (WireMock/aResponse))
  ([config-map] 
    (let [response (WireMock/aResponse)]
      (when-let [s (:status config-map)] (.withStatus response s))
      (when-let [b (:body config-map)] (.withBody response b))
      (when-let [headers (:headers config-map)] 
        (doseq [h headers]
          (.withHeader response (key h) (val h))))
      response)))

; ResponseDefinitionBuilder
; status
; headers
; bodyContent
; bodyFileName
; fixedDelayMilliseconds
; proxyBaseUrl
(defn will-return [req res] (.willReturn req res))
(defn with-body [response-builder body] (.withBody response-builder body))
(defn with-status [response-builder status] (.withStatus response-builder status))
(defn with-header [response-builder k v] (.withHeader response-builder k v))


