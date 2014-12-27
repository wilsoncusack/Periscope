(ns periscope.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [clojure.java.io :as io]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [ring.adapter.jetty :as jetty]
            [models.emailMigration :as schema]
            [controllers.email :as signup]
            [environ.core :refer [env]]))

(defroutes app-routes
  signup/routes

  ; root, looks in public directory and returns index.html
  (GET  "/" [] (resource-response "index.html" {:root "public"}))
  ; example of a get request
  (GET  "/widgets" [] (response [{:name "Widget 1"} {:name "Widget 2"}]))
  ; receives the post request and parses input

  ; I think this is what is allowing all of the dependencies to be found,
  ; anything that's not specified by the above will just be looked for by
  ; it natural path, i.e. localhost:5000/public/templates/...
  (route/resources "/")
  ; if not found
  (route/not-found "Page not found"))

(def app
  "middlewear for HTTP
  from http://zaiste.net/2014/02/web_applications_in_clojure_all_the_way_with_compojure_and_om/"
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))

(defn -main [& [port]]
  "from Heroku's setup"
  (schema/migrate)
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
