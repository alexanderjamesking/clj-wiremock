# clj-wiremock

[![Build Status](https://travis-ci.org/alexanderjamesking/clj-wiremock.svg)](https://travis-ci.org/alexanderjamesking/clj-wiremock)

TODO: write a Clojure library that wraps wiremock

Example using wiremock in the REPL (lein repl)
```
(import com.github.tomakehurst.wiremock.WireMockServer)
(import com.github.tomakehurst.wiremock.core.WireMockConfiguration)
(import com.github.tomakehurst.wiremock.client.WireMock)

(def wiremock-config (.port (new WireMockConfiguration) 22222))
(def wiremock-config (new WireMockConfiguration))

(def wiremock-server (new com.github.tomakehurst.wiremock.WireMockServer wiremock-config))
(.start wiremock-server)
(WireMock/configureFor "localhost" 22222)


(defn GET [x] (WireMock/get x))
(defn urlEqualTo [x] (WireMock/urlEqualTo x))
(defn stubFor [x] (WireMock/stubFor x))
(defn aResponse [] (WireMock/aResponse))

(stubFor 
  (.willReturn 
    (GET (urlEqualTo "/hello")) 
    (.withBody 
      (.withStatus (aResponse) 200) 
      "Hello World")))


(stubFor 
  (.willReturn 
    (get (urlEqualTo "/hello")) 
    (.withBody 
      (.withStatus (WireMock/aResponse) 200) 
      "Hello World")))

(.stop wiremock-server)
```


## Usage

FIXME

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
