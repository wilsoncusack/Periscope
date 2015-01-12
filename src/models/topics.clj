(ns models.topics
  (:require [clojure.string :as str]
            [clojure.java.jdbc :as sql]))

(def spec (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/wilsoncusack"))

(defn all []
  (into [] (sql/query spec ["select * from topics order by id desc"])))
