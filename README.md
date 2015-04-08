# clj-wiremock

TODO: write a Clojure library that wraps wiremock

Example using wiremock in the REPL (lein repl)
```
(import com.github.tomakehurst.wiremock.WireMockServer)
(import com.github.tomakehurst.wiremock.core.WireMockConfiguration)

(def wiremock-config (.port (new WireMockConfiguration) 11111))
(def wiremock-config (new WireMockConfiguration))

(def wiremock-server (new com.github.tomakehurst.wiremock.WireMockServer wiremock-config))
(.start wiremock-server)

(import com.github.tomakehurst.wiremock.client.WireMock)

(WireMock/stubFor 
  (.willReturn 
    (WireMock/get (WireMock/urlEqualTo "foo")) 
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
