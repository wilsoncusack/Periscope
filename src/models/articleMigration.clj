(ns models.articleMigration
  (:require [clojure.java.jdbc :as sql]
            [models.articles :as articles]))

(defn migrated? []
  (-> (sql/query articles/spec
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='articles'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands articles/spec
                        (sql/create-table-ddl
                         :articles
                         [:id :serial "PRIMARY KEY"]
                         [:title :varchar "NOT NULL"]
                         [:link :varchar "NOT NULL"]
                         [:author :varchar "NOT NULL"]
                         [:body :text "NOT NULL"]
                         [:publication :varchar "NOT NULL" "DEFAULT 0"]
                         [:political_score :int "NOT NULL" "DEFAULT 0"]
                         [:pos_neg_score :int "NOT NULL" "DEFAULT 0"]
                         [:date_written :date "NOT NULL" "DEFAULT CURRENT_DATE"]))
    (println " done")))
