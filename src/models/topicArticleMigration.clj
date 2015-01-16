(ns models.topicArticleMigration
  (:require [clojure.java.jdbc :as sql]
            [models.topicArticles :as topicArticles]))

(defn migrated? []
  (-> (sql/query topicArticles/spec
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='topic_articles'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands topicArticles/spec
                        (sql/create-table-ddl
                         :topic_articles
                         [:id :serial "PRIMARY KEY"]
                         [:topic_id :int "references topics (id)" "NOT NULL"]
                         [:title :varchar "NOT NULL"]
                         [:link :varchar "NOT NULL"]
                         [:author :varchar "NOT NULL"]
                         [:summary :text "NOT NULL"]
                         [:publication :varchar "NOT NULL" "DEFAULT 0"]
                         [:political_score :int "NOT NULL" "DEFAULT 0"]
                         [:pos_neg_score :int "NOT NULL" "DEFAULT 0"]
                         [:date_written :date "NOT NULL" "DEFAULT CURRENT_DATE"]))
    (println " done")))
