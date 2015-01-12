(ns controllers.email
  (:require [compojure.core :refer [defroutes GET POST]]
            [clojure.string :as str]
            [ring.util.response :as ring]
            [models.emails :as model]))

(defn create
  "When the email isn't blank, uses the model's create"
  [email]
  (when-not (str/blank? email)
    (model/create email)))
  ;(ring/redirect "/")) ; example had this, don't think its needed

  (defroutes routes
    "defining the routes used for email signups,
    any incoming request will try to be matched here first.
    Called in web.clj"
    (POST "/insertEmail" [:as request]
          (let [email (get-in request [:params :email])
                result (model/query email)]
            (if (empty? result)
              (ring/response {:status (create email)})
              (ring/response {:status 0})))))
