# clj-wiremock

[![Build Status](https://travis-ci.org/alexanderjamesking/clj-wiremock.svg)](https://travis-ci.org/alexanderjamesking/clj-wiremock) 

A Clojure library that wraps [wiremock](https://github.com/tomakehurst/wiremock) by [@superaking](https://twitter.com/superaking)

[![Clojars Project](http://clojars.org/clj-wiremock/latest-version.svg)](http://clojars.org/clj-wiremock)

### Hello World in the REPL

#### Prerequisites
- You have an up to date version of [leiningen](https://github.com/technomancy/leiningen)
- You have cloned this git repo and run ```lein repl``` to start the REPL

```clojure
(require '[clj-wiremock.core :refer :all])

; create a new server on the default port 8080
(def wiremock-server (server))

; start the server - load http://localhost:8080/__admin/ in a browser to see it running
(start wiremock-server)

; set up a stub then refresh the __admin page to see your new mapping
; the stub is built using a clojure map which gets converted to JSON 
; and is posted to the wiremock API
(stub { :request { :method "GET" :url "/hello"} 
        :response { :status 200 :body "Hello World"}})

; load http://localhost:8080/hello in a browser

; reset the mappings - handy when you want to clear state between tests
(reset wiremock-server)

; stop the server
(stop wiremock-server)
```

###A practical example

See [alexanderjamesking/clj-wiremock-example](https://github.com/alexanderjamesking/clj-wiremock-example) for an example of using wiremock to test a webapp that makes a HTTP call to a stubbed server that returns JSON.

See the test sources in this project and the JSON examples on http://wiremock.org for more examples

###Examples as per [http://wiremock.org/stubbing.html](http://wiremock.org/stubbing.html)

```clojure

; Basic Stubbing
(stub { 
  :request { 
    :method "GET" 
    :url "/some/thing"
  } 
  :response { 
    :status 200 
    :body "Hello World!"
    :headers { 
      :Content-Type "text/plain" 
    }
  }})

; URL Matching with urlPattern
(stub { 
  :request { 
    :method "PUT" 
    :urlPattern "/thing/matching/[0-9]+"
  } 
  :response { 
    :status 200 
  }})

; URL Matching with urlPath (matches the path part of the URL only)
(stub { 
  :request { 
    :method "PUT" 
    :urlPath "/query"
  } 
  :response { 
    :status 200 
  }})


; Request Header Matching
(stub { 
  :request { 
    :method "POST" 
    :url "/with/headers"
    :headers {
      :Content-Type { :equalTo "text/xml" }
      :Accept { :matches "text/.*" }
      :etag { :doesNotMatch "abcd.*" }
      :X-Custom-Header { :contains "2134"}
    }
  } 
  :response { 
    :status 200 
  }})

; Query Parameter Matching
(stub { 
  :request { 
    :method "GET" 
    :url "/with/query"
    :queryParameters {
      :search { :contains "Some text" }
    }
  } 
  :response { 
    :status 200 
  }})

; Request Body Matching
(stub { 
  :request { 
    :method "POST" 
    :url "/with/body"
    :bodyPatterns [
      { :matches "<status>OK</status>" }
      { :doesNotMatch ".*ERROR.*" }
    ]
  } 
  :response { 
    :status 200 
  }})

; JSON body matching
(stub { 
  :request { 
    :method "POST" 
    :url "/with/json/body"
    :bodyPatterns [
      { :equalToJson "{ \"houseNumber\": 4, \"postcode\": \"N1 1ZZ\" }" }
      { :jsonCompareMode "LENIENT" }
    ]
  } 
  :response { 
    :status 200 
  }})

; JSONPath expressions
(stub { 
  :request { 
    :method "POST" 
    :url "/with/json/body"
    :bodyPatterns [
      { :matchesJsonPath "$.status" }
      { :matchesJsonPath "$.things[?(@.name == 'RequiredThing')]" }
    ]
  } 
  :response { 
    :status 200 
  }})

```
