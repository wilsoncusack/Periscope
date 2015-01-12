(ns models.topicMigration
  (:require [clojure.java.jdbc :as sql]
            [models.topics :as topics]))

(defn migrated? []
  (-> (sql/query topics/spec
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='articles'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands topics/spec
                        (sql/create-table-ddl
                         :topics
                         [:id :serial "PRIMARY KEY"]
                         [:title :varchar "NOT NULL"]
                         [:description :text "NOT NULL"]
                         [:image :varchar "NOT NULL"]
                         [:date_posted :date "NOT NULL" "DEFAULT CURRENT_DATE"]))
    (println " done")))
