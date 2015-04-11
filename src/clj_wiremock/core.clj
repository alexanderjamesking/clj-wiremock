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

(defn configure-for 
  "Configures the WireMock client to use host and port provided"
  [host port]
  (WireMock/configureFor host port))

(defn get-all-mappings 
  "Calls server to returns all mappings, server must be running"
  [] 
  (parse-string (.toString (.getMappings (WireMock/listAllStubMappings))) true))

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

(defn PATCH [x] (WireMock/patch x))
(defn HEAD [x] (WireMock/head x))
(defn OPTIONS [x] (WireMock/options x))
(defn TRACE [x] (WireMock/trace x))
(defn ANY [x] (WireMock/any x))

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

(defn equal-to 
  "ValueMatchingStrategy for the value (String)"
  [value] 
  (WireMock/equalTo value))

(defn equal-to-json 
  "ValueMatchingStrategy matching the JSON (String) semantically 
  see http://wiremock.org/stubbing.html#json-body-matching"
  ([value] 
    (WireMock/equalToJson value))
  ([value json-compare-mode]
    (WireMock/equalToJson value json-compare-mode)))

(defn equal-to-xml 
  "ValueMatchingStrategy for the XML (String)"
  [value] 
  (WireMock/equalToXml value))  

(defn matching-xpath
  "ValueMatchingStrategy for the XPath (String)
  see http://wiremock.org/stubbing.html#xpath-body-matching"
  [value] 
  (WireMock/matchingXPath value))  

(defn containing
  "ValueMatchingStrategy matching anything containing the value (String)"
  [value]
  (WireMock/containing value))

(defn matching
  "ValueMatchingStrategy matching the value (String) e.g. 'text/.*'"
  [value]
  (WireMock/matching value))

(defn not-matching
  "ValueMatchingStrategy not matching the value (String) e.g. 'text/.*'"
  [value]
  (WireMock/notMatching value))

(defn matching-json-path
  "ValueMatchingStrategy matching the json-path (String) e.g. '$.things[$(@.name == 'RequiredThing')]'"
  [json-path]
  (WireMock/matchingJsonPath json-path))

(defn stub-for 
  "Creates the MappingBuilder on the running wiremock-server"
  [mapping-builder] 
  (WireMock/stubFor mapping-builder))

(defn will-return 
  "Configures the request (MappingBuilder) to return the response (ResponseDefinitionBuilder)"
  [request response] 
  (.willReturn request response))

(defn with-body 
  "Configures the response-builder to return the body (String)"
  [response-builder body] 
  (.withBody response-builder body))

(defn with-status 
  "Configures the response-builder to return the status (int)"
  [response-builder status]
  (.withStatus response-builder status))

(defn with-header 
  "Configures the response-builder to return the header key (String) and value (String)"
  [response-builder k v] 
  (.withHeader response-builder k v))

(defn with-body-file [response-builder x] 
  (.withBodyFile response-builder x))

(defn with-fixed-delay [response-builder x] 
  (.withFixedDelay response-builder (int x)))

(defn proxied-from [response-builder x] 
  (.proxiedFrom response-builder x))

(defn with-transformers [response-builder & transformers] 
  (.withTransformers response-builder transformers))

(defn with-fault [response-builder fault] 
  (.withFault response-builder fault))

(defn response 
  "Creates a ResponseDefinitionBuilder"
  ([] (WireMock/aResponse))
  ([config-map] 
    (let [response (WireMock/aResponse)]
      (when-let [s (:status config-map)] (with-status response s))
      (when-let [b (:body config-map)] (with-body response b))
      (when-let [headers (:headers config-map)] 
        (doseq [h headers]
          (.withHeader response (key h) (val h))))
      response)))
