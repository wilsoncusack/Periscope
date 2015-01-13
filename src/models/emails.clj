(ns models.emails
  (:require [clojure.string :as str]
            [clojure.java.jdbc :as sql]
            [periscope.email :as sendEmail]))

(def spec (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/emails"))

(defn all []
  (into [] (sql/query spec ["select * from emails order by id desc"])))

(defn create [email]
  "create: String -> int
  I/P: email, string of users email, signing up for beta
  O/P: 1 if the string passes our basic santisaztion
      -1 otherwise"
  (if (and (Java.lang.String/.contains email "@")
           (Java.lang.String/.contains email ".")
           (not (Java.lang.String/.contains email ";"))
           (< (Java.lang.String/.length email) 40))
    (do
      (sql/insert! spec :emails [:body] [email])
      (sendEmail/betaSignup email)
      1)
    -1))

(defn query [email]
  (sql/query spec ["select * from emails where body = ?;" email]))
