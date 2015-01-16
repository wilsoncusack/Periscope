(ns periscope.home
  (:require [compojure.core :refer [defroutes GET POST]]
            [models.articles :refer [spec]]
            [ring.util.response :as ring]
            [clojure.java.jdbc :as sql]))

(defn- getTopicArticles
  "getTopicArticles: int -> list of maps
  I/P: id, int, id of the topic
  O/P: all of the articles in the topic_articles table that have id
  as their topic_id"
  [id]
  (let [query "SELECT * FROM topic_articles WHERE topic_id = ?;"]
    (sql/query spec [query id])))

(defn- homepageTopics
  "homepageTopics: nil -> list
  {:image :description :date_posted :title :id :articles[article maps]}
  I/P: nil
  O/P:list of topic maps"
  []
  (let [queryTopics "SELECT * FROM topics ORDER BY id DESC LIMIT 10;"
        topics (sql/query spec [queryTopics])]
    (pmap #(assoc % :articles (getTopicArticles (:id %))) topics)))


(defroutes routes
  (GET "/topics" []
       (ring/response (homepageTopics))))
