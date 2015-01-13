(ns periscope.email
  (:require
   [clojure.java.jdbc :as sql]
   [clj-mandrill.core :as mandrill]))

(defn signupResp
 []
 (mandrill/send-message {:html "<h1 style='color: red'> test </h1>" :subject "Just a note" :from_email "hello@periscope.com" :from_name "Wils"
                :to [{:email "cusackw@gmail.com"}]}))


(defn betaSignup
  [email]
  (mandrill/send-template "beta-signup" {:to [{:email email}] :preserve_recipients false}))
