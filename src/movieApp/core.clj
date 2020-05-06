(ns movieApp.core
  (:use compojure.core
        ring.util.json-response 
        ring.adapter.jetty
 hiccup.page hiccup.element)

  (:require [compojure.core :refer [defroutes GET POST]]
            [clojure.string :as str]     
            [ring.util.response :as ring]
            [hiccup.element :as element]
            [movieApp.view :as view]
            [movieApp.db :as db]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            ))

(defn display-all-movies []
  (view/index-page (db/get-all-movies)))

(defn create-movie [name year description genre recommendation]
  (when-not (str/blank? name)
    (db/create-movie name year description genre recommendation))
  (ring/redirect "/"))

(defn delete-movie [id]
  (when-not (str/blank? id)
    (db/delete-movie id))
  (ring/redirect "/"))

(defn show-update-view [id]
 (view/update-movie-form (db/get-movie-by-id id)))

(defn update-movie [id name rating description genre]
    (when-not (str/blank? id)
    (db/update-movie id name rating description genre))
   (view/index-page (db/get-all-movies)))

(defroutes my_routes
  (GET "/" [] (display-all-movies))
  (POST "/" [name rating description genre recommendation] (create-movie name rating description genre recommendation))
  (POST "/update-movie"  [id name rating description genre] (update-movie id name rating description genre))
  (GET "/update/:id" [id] (show-update-view id))
  (GET "/delete/:id" [id]  (delete-movie id))
  (GET "/api/all" [] (json-response (db/get-all-movies))))
 

(def app
  (wrap-defaults my_routes site-defaults))
