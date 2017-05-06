(ns clj-sqlite.core
    (:use clojure.pprint)
    (:require [clojure.java.jdbc :as sql]))

(def testdata
    {:img nil,
     :name "Adam",
     :details "adamh.io"})

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/database.db"
   })

(defn create-db 
  "Drop the table then recreate it."
  []
  (sql/execute! db ["drop table if exists animals"])
  (try 
    (let [cs (sql/create-table-ddl :animals
                                    [:img :text]
                                    [:name :text]
                                    [:details :text])]
        (sql/execute! db [cs]))
    (catch Exception e (println e))))


(defn output
  [conn]
  (sql/query conn "select * from animals"))

(defn -main
  "Create DB, insert values, print stuff"
  [& args]
    (create-db)
    (sql/insert! db :animals testdata)
    (pprint (keys (first (output db))))
    (pprint (:name (first (output db)))))
