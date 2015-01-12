(ns models.articles
  (:require [clojure.string :as str]
            [clojure.java.jdbc :as sql]))

(def spec (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/emails"))

(defn all []
  (into [] (sql/query spec ["select * from articles order by id desc"])))

;(defn query [word]
 ; (sql/query spec ["select * from emails where body = ?" email]))
