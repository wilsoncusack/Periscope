(ns models.emailMigration
  (:require [clojure.java.jdbc :as sql]
            [models.emails :as emails]))

(defn migrated? []
  (-> (sql/query emails/spec
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='emails'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands emails/spec
                        (sql/create-table-ddl
                         :emails
                         [:id :serial "PRIMARY KEY"]
                         [:body :varchar "NOT NULL"]
                         [:created_at :timestamp
                          "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]))
    (println " done")))