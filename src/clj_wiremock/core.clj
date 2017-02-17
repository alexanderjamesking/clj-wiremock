(ns clj-wiremock.core
  (:require [cheshire.core :refer [generate-string parse-string]]
            [clj-http.client :as client])
  (:import com.github.tomakehurst.wiremock.WireMockServer
           com.github.tomakehurst.wiremock.core.WireMockConfiguration))

(defn config
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

(defn server
  "Create a new instance of WireMockServer"
  ([] (new WireMockServer (config)))
  ([config] (new WireMockServer config)))

(defn start
  "Starts the WireMockServer"
  [server]
  (.start server))

(defn stop
  "Stops the WireMockServer"
  [server]
  (.stop server))

(defn reset
  "Removes all stub mappings and deletes the request log"
  [server]
  (.resetAll server))

(defn- admin-post [endpoint body & [base-url]]
  (let [base-url (or base-url "http://localhost:8080")
        admin-url (str base-url "/__admin/" endpoint)
        content-to-post {:body (generate-string body)}
        response (client/post admin-url content-to-post)]
    (parse-string (:body response) true)))

(defn count-requests [body & [base-url]]
  (:count (admin-post "requests/count" body base-url)))

(defn find-requests [body & [base-url]]
  (:requests (admin-post "requests/find" body base-url)))

(defn stub [body & [base-url]]
  (admin-post "mappings/new" body base-url))
