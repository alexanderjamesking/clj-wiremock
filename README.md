# clj-wiremock

[![Build Status](https://travis-ci.org/alexanderjamesking/clj-wiremock.svg)](https://travis-ci.org/alexanderjamesking/clj-wiremock) 


A Clojure library that wraps [wiremock](https://github.com/tomakehurst/wiremock) by [@superaking](https://twitter.com/superaking)

[![Clojars Project](http://clojars.org/clj-wiremock/latest-version.svg)](http://clojars.org/clj-wiremock)


### Hello World in the REPL
```clojure
(require '[clj-wiremock.core :refer :all])

; create a new server on the default port 8080
(def server (wiremock-server (wiremock-config)))

; start the server - load http://localhost:8080/__admin/ in a browser to see it running
(start server)

; set up a stub then refresh the __admin page to see your new mapping
(stub-for (will-return (GET (url-equal-to "/hello")) 
                       (response { :body "Hello World" })))

; load http://localhost:8080/hello in a browser

; reset the mappings - handy when you want to clear state between tests
(reset-mappings server)

; stop the server
(stop server)
```

### Basic Usage
Create a wiremock server on port 11111
```clojure
(def server (wiremock-server (wiremock-config { :port 11111 })))
```

Starting and stopping the server
```clojure
(start server)
(stop server)
```

Configure the WireMock client to post to 11111
```clojure
(configure-for "localhost" 11111)
```

Stub an endpoint using map syntax
```clojure
(stub-for (will-return (GET (url-equal-to "/hello")) 
                       (response { :status 200 :body "Hello World" })))
```

Stub an endpoint using the builders with the thread first macro
```clojure
(stub-for (will-return (GET (url-equal-to "/hello-world")) 
                       (-> (response) 
                           (with-body "Hello World")
                           (with-header "Content-Type" "text/plain")
                           (with-status 200))))
```
