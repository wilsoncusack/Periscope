(ns periscope.admin
  (:require [compojure.core :refer [defroutes GET POST]]
            [models.articles :as articles]
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
    (sql/query articles/spec [query searchWord searchWord])))

;SELECT word, count(*) FROM (SELECT regexp_split_to_table(body, '\s') as word FROM articles) t GROUP BY word;

(defn- getArticles
  "getArticles: String vector -> Vector of JSON Objects
  I/P: searchWords, vector of String to search for
  O/P: vector of JSON article objects (everything except body and ID),
  sorted by frequency of searched for words, from least to greatest"
  [searchWords]
  ; SHOULD WE CONVERT WORDS TO PLURAL/ROOT?
  (let [articles (map searchDB searchWords)
        searchSet (set searchWords)]
    ; should we check to make sure the DB responded with something?
    (->> articles
         (reduce concat)
         (sort-by #(findFrequency searchSet %))
         (map #(dissoc % :body))
         reverse)))

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
  (POST "/addTopic" [:as request])
  ;; Expecting JSON Object of the topic
  ;; Includes ID, Title, Description, Publication, Summary, Link
  ;; topics should have a date created so that we can later add to it an no what date our first
  ;; coverage was from
  (POST "/editTopic" [:as request]))
