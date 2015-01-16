(ns periscope.admin
  (:require [compojure.core :refer [defroutes GET POST]]
            [models.articles :refer [spec]]
            [ring.util.response :as ring]
            [clojure.java.jdbc :as sql]))

(defn- findFrequency
  "findFrequency: Hash set of string JSON -> Int
  I/P: toFind (hash set of Strings), articleJSON (JSON object of article)
  O/P: frequency of String in toFind in articleJSON's body and title"
  [toFind articleJSON]
  (let [articleWords (clojure.string/join [(:body articleJSON) (:title articleJSON)])]
  (->> (clojure.string/split articleWords #" ")
       (filter #(some toFind [%])) ;filters out words that don't match any in hashset
       count)))

(defn- searchDB
  ; I also have a recursive version of this, so we don't have to map and then concat, thoughts?
  "search: String -> vector
  I/P: word (string)
  O/P: JSON Object of DB response to String Query"
  [word]
  (let [query "SELECT title, body, link, author, publication, political_score, pos_neg_score, date_written FROM articles WHERE title like ? or body like ?;"
        searchWord (clojure.string/join ["%" word "%"])] ;; won't work like this, need to concat here and get ridof %'s above ^
    (sql/query spec [query searchWord searchWord])))

;SELECT word, count(*) FROM (SELECT regexp_split_to_table(body, '\s') as word FROM articles) t GROUP BY word;

(defn- getArticles
  "getArticles: String vector -> Vector of JSON Objects
  I/P: searchWords, vector of String to search for
  O/P: vector of JSON article objects (everything except body and ID),
  sorted by frequency of searched for words, from least to greatest"
  [searchWords]
  ; SHOULD WE CONVERT WORDS TO PLURAL/ROOT?
  (let [articles (pmap searchDB searchWords)
        searchSet (set searchWords)]
    ; should we check to make sure the DB responded with something?
    (->> articles
         (reduce concat)
         (sort-by #(findFrequency searchSet %))
         (map #(dissoc % :body))
         reverse)))

(defn- insertTopicArticle
  [id article]
  (let [query "INSERT INTO topic_articles (topic_id, title, link, author, summary, publication, political_score, pos_neg_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?);"
        title (:title article)
        link (:link article)
        author (:author article)
        summary (:summary article)
        publication (:publication article)
        political_score (Integer/parseInt (:political_score article)) ; probably a reason these aren't coming through right
        pos_neg_score (Integer/parseInt (:pos_neg_score article))]
    (sql/query spec [query id title link author summary publication political_score pos_neg_score])))
  ;(sql/query spec :topic_articles [:topic_id :title :link :author
   ;                                :summary :publication :political_score :pos_neg_score :date_written]
    ;           [id (:title article) (:link article) (:author article) (:summary article)
     ;           (:publication article) (:political_score article) (:pos_neg_score article) (:date_written article)]))

(defn- addTopic
  "addTopic: JSON topic objects-> nil
  {:title :description :image :articles[]}
  I/P:
  O/P:
  "
  [topic]
  (let [query "INSERT INTO topics (title, description, image) VALUES (?, ?, ?) RETURNING id;"
        id (-> (sql/query spec [query (:title topic) (:description topic) (:image topic)])
               first
               :id)
        articles (->> (:articles topic)
                      (into [])
                      (map second))]
    (pmap #(insertTopicArticle id %) articles)))

(defn- updateTopic
  "updateTopic: map -> updates topic in db (nil)
  I/P: Map of a topic
  O/P: updates the topic with the given ID in the topics table"
  [topic]
  (let [query "UPDATE topics SET title = ?, summary = ?, publication = ?, link = ? WHERE id = ?;"]))
    ;(sql/query topics/spec [query (:title topic) (:summary topic) (:publication topic) (:link topic) (:id topic)])))

(defroutes routes
  (GET "/articlesSearch" [:as request]
       (let [words (get-in request [:params :words])]
         (if (vector? words)
           (ring/response (getArticles words))
           (ring/response (getArticles [words]))))) ;not sure why I have to do this? should already be
  ;; NEED CHESHIRE to conver JSON to maps
  ;; Expecting JSON Object of the topic
  ;; Includes Title, Publication, Summary, Link
  (POST "/addTopic" [:as request]
        (let [topic (get-in request [:params :topic])]
          (addTopic topic)
          (ring/response {:status 200})))
  ;; Expecting JSON Object of the topic
  ;; Includes ID, Title, Description, Publication, Summary, Link
  ;; topics should have a date created so that we can later add to it an no what date our first
  ;; coverage was from
  (POST "/editTopic" [:as request]))
