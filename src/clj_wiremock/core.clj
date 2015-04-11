(ns clj-wiremock.core
  (:require [cheshire.core :refer :all])
  (:import com.github.tomakehurst.wiremock.WireMockServer
           com.github.tomakehurst.wiremock.core.WireMockConfiguration
           com.github.tomakehurst.wiremock.client.WireMock))

(defn wiremock-config 
  "Creates a new instance of WireMockConfiguration"
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
  "Create a new instance of WireMockServer"
  ([] (new WireMockServer))
  ([config] (new WireMockServer config)))

(defn reset-mappings 
  "Removes all stub mappings and deletes the request log"
  [server] 
  (.resetMappings server))

(defn start 
  "Starts the WireMockServer"
  [server] 
  (.start server))

(defn stop 
  "Stops the WireMockServer"
  [server] 
  (.stop server))

; WireMock Client

(defn configure-for 
  "Configures the WireMock client to use host and port provided"
  [host port]
  (WireMock/configureFor host port))

(defn get-all-mappings 
  "Calls server to returns all mappings, server must be running"
  [] 
  (parse-string (.toString (.getMappings (WireMock/listAllStubMappings))) true))


; (UrlMatchingStrategy) -> MappingBuilder
(defn GET 
  "MappingBuilder with a GET request for the UrlMatchingStrategy"
  [url-matching-strategy] 
  (WireMock/get url-matching-strategy))

(defn POST 
  "MappingBuilder with a POST request for the UrlMatchingStrategy"
  [url-matching-strategy] 
  (WireMock/post url-matching-strategy))

(defn PUT 
  "MappingBuilder with a PUT request for the UrlMatchingStrategy"
  [url-matching-strategy] 
  (WireMock/put url-matching-strategy))

(defn DELETE 
  "MappingBuilder with a DELETE request for the UrlMatchingStrategy"
  [url-matching-strategy] 
  (WireMock/delete url-matching-strategy))

; (defn PATCH [x] (WireMock/patch x))
; (defn HEAD [x] (WireMock/head x))
; (defn OPTIONS [x] (WireMock/options x))
; (defn TRACE [x] (WireMock/trace x))
; (defn ANY [x] (WireMock/any x))

; (String) -> UrlMatchingStrategy
(defn url-equal-to 
  "UrlMatchingStrategy for the url (String) exactly including any query parameters"
  [url] 
  (WireMock/urlEqualTo url))

(defn url-matching 
  "UrlMatchingStrategy for the pattern (String) e.g. '/thing/matching/[0-9]+'"
  [pattern] 
  (WireMock/urlMatching pattern))

(defn url-path-equal-to 
  "UrlMatchingStrategy for the path of the url (String) only, query parameters are ignored"
  [url] 
  (WireMock/urlPathEqualTo url))

; (String) -> ValueMatchingStrategy
; TODO: wrap the following methods
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


