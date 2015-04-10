# clj-wiremock

[![Build Status](https://travis-ci.org/alexanderjamesking/clj-wiremock.svg)](https://travis-ci.org/alexanderjamesking/clj-wiremock)

A Clojure library that wraps wiremock, very much a work in progress - don't use this just yet! @superaking

Create a wiremock server on port 11111
```
(def server (wiremock-server (wiremock-config { :port 11111 })))
```

Starting and stopping the server
```
(start server)
(stop server)
```

Configure the WireMock client to post to 11111
```
(configure-for "localhost" 11111)
```

Stub an endpoint using map syntax
```
(stub-for (will-return (GET (url-equal-to "/hello")) 
                       (response { :status 200 :body "Hello World" })))
```

Stub an endpoint using the builder
```
(stub-for (will-return (GET (url-equal-to "/hello-world")) 
                       (-> (response) 
                           (with-body "Hello World")
                           (with-header "Content-Type" "text/plain")
                           (with-status 200))))
```
