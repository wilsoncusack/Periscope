(defproject periscope "1.0.0-SNAPSHOT"
  :description "Demo Clojure web app"
  :url "http://clojure-getting-started.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.3.5"]
                 [postgresql "9.1-901-1.jdbc4"]
                 ;[org.postgresql/postgresql "9.2-1002-jdbc4"]
                 [compojure "1.2.2"]
                 [environ "0.5.0"]
                 [korma "0.3.0"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-jetty-adapter "1.2.2"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.2.1"]]
  ;:ring {:handler acme.core.web/app}
  :hooks [environ.leiningen.hooks]
  :uberjar-name "periscope.jar"
  :profiles {:production {:env {:production true}}})
