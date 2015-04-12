(defproject clj-wiremock "0.2.0"
  :description "A clojure wrapper around wiremock"
  :url "https://github.com/alexanderjamesking/clj-wiremock"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.github.tomakehurst/wiremock "1.54"]
                 [cheshire "5.4.0"]]
  :profiles {:dev {:dependencies [[clj-http "1.1.0"]]}})
