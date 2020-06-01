(ns movieApp.db
  (:require [clojure.java.jdbc :as sql]))

(def connection
  {:classname "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname "//localhost:3306/movies?autoReconnect=true&useSSL=false"

   :user "root"
   :password ""})

 (defn convert-boolean [arg]
   (if (= arg "true") 1 0))

(defn create-movie [name rating description genre recommendation image trailer]
  (sql/insert! connection :movie [:name :rating :description :genre :recommendation :image :trailer] [name rating description genre (convert-boolean recommendation) image trailer]))

(defn delete-movie [id]
 (sql/delete! connection :movie
            ["id = ?" id]))

(defn get-all-movies []
  (into [] (sql/query connection ["select * from movie"])))

(defn get-movie-by-id [id]
    (into [] (sql/query connection ["select * from movie where id = ?" id])))

(defn update-movie [id name rating description genre recommendation image trailer]

  (sql/update! connection :movie {:id id :name name :rating rating :description description :genre genre :recommendation (convert-boolean recommendation) :image image :trailer trailer} ["id = ?" id]))
